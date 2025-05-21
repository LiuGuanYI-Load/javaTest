package chat;import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ChatServer {
    // 保存所有客户端连接
    private static final Set<Channel> clients = new HashSet<>();
    // 随机中文名字库
    private static final String[] CHINESE_WORDS = {"张", "伟", "杰", "李", "明", "华", "王", "强", "丽", "芳", "陈", "阳", "刘", "欣", "宇"};

    public static void main(String[] args) throws Exception {
        // 1. 创建 Boss 和 Worker 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 2. 创建 ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 3. 设置线程组
            bootstrap.group(bossGroup, workerGroup)
                    // 4. 指定 Channel 类型
                    .channel(NioServerSocketChannel.class)
                    // 5. 设置 Channel 配置
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 6. 设置子 Channel 的 Handler
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            // 7. 配置 ChannelPipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            pipeline.addLast(new SimpleChatHandler());
                        }
                    });

            // 8. 绑定端口，启动服务器
            ChannelFuture future = bootstrap.bind(8080).sync();
            System.out.println("服务器启动，监听端口: 8080");

            // 9. 等待服务器关闭
            future.channel().closeFuture().sync();
        } finally {
            // 10. 释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    // 自定义 Handler 处理 WebSocket 消息
    private static class SimpleChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        private String username;

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 客户端连接时生成随机用户名并加入集合
            username = generateRandomName();
            clients.add(ctx.channel());
            String connectMsg = username + " 加入聊天室";
            broadcast(new TextWebSocketFrame(connectMsg));
            System.out.println(connectMsg + ": " + ctx.channel().remoteAddress());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            // 客户端断开时移除并广播
            String disconnectMsg = username + " 离开聊天室";
            clients.remove(ctx.channel());
            broadcast(new TextWebSocketFrame(disconnectMsg));
            System.out.println(disconnectMsg + ": " + ctx.channel().remoteAddress());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
            // 接收客户端消息并广播
            String message = username + ": " + msg.text();
            System.out.println("收到消息: " + message);
            broadcast(new TextWebSocketFrame(message));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            clients.remove(ctx.channel());
            ctx.close();
        }

        // 广播消息到所有客户端
        private void broadcast(TextWebSocketFrame msg) {
            for (Channel client : clients) {
                client.writeAndFlush(msg.retainedDuplicate());
            }
        }

        // 生成随机三个中文汉字名字
        private String generateRandomName() {
            Random random = new Random();
            return CHINESE_WORDS[random.nextInt(CHINESE_WORDS.length)] +
                    CHINESE_WORDS[random.nextInt(CHINESE_WORDS.length)] +
                    CHINESE_WORDS[random.nextInt(CHINESE_WORDS.length)];
        }
    }
}