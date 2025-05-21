package chat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        // 1. 创建 EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 2. 创建 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            // 3. 设置线程组
            bootstrap.group(group)
                    // 4. 指定 Channel 类型
                    .channel(NioSocketChannel.class)
                    // 5. 设置 Handler
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            // 6. 配置 ChannelPipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            URI uri = null;
                            try {
                                uri = new URI("ws://localhost:8080/ws");
                            } catch (URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
                            pipeline.addLast(new WebSocketClientHandler(handshaker));
                        }
                    });

            // 7. 连接服务器
            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
            Channel channel = future.channel();
            System.out.println("客户端连接成功: " + channel.remoteAddress());

            // 8. 读取用户输入并发送消息
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }

            // 9. 等待通道关闭
            channel.closeFuture().sync();
        } finally {
            // 10. 释放资源
            group.shutdownGracefully();
        }
    }

    private static class WebSocketClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        private final WebSocketClientHandshaker handshaker;
        private ChannelPromise handshakeFuture;

        public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            handshakeFuture = ctx.newPromise();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            handshaker.handshake(ctx.channel());
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
            System.out.println("收到服务器消息: " + msg.text());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}