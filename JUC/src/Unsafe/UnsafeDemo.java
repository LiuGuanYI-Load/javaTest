package Unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

 class UnSafeDemo {
    public  static  void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        // 通过反射得到theUnsafe对应的Field对象
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        // 设置该Field为可访问
        field.setAccessible(true);
        // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
        Unsafe unsafe = (Unsafe) field.get(null);
        System.out.println(unsafe);

        //通过allocateInstance直接创建对象
        Demo demo = (Demo) unsafe.allocateInstance(Demo.class);

        Class demoClass = demo.getClass();
        Field str = demoClass.getDeclaredField("str");
        Field i = demoClass.getDeclaredField("i");
        Field staticStr = demoClass.getDeclaredField("staticStr");

        //获取实例变量str和i在对象内存中的偏移量并设置值
        unsafe.putInt(demo,unsafe.objectFieldOffset(i),1);
        unsafe.putObject(demo,unsafe.objectFieldOffset(str),"Hello Word!");

        // 返回 User.class
        Object staticField = unsafe.staticFieldBase(staticStr);
        System.out.println("staticField:" + staticStr);

        //获取静态变量staticStr的偏移量staticOffset
        long staticOffset = unsafe.staticFieldOffset(demoClass.getDeclaredField("staticStr"));
        //获取静态变量的值
        System.out.println("设置前的Static字段值:"+unsafe.getObject(staticField,staticOffset));
        //设置值
        unsafe.putObject(staticField,staticOffset,"Hello Java!");
        //再次获取静态变量的值
        System.out.println("设置后的Static字段值:"+unsafe.getObject(staticField,staticOffset));
        //调用toString方法
        System.out.println("输出结果:"+demo.toString());

        long data = 1000;
        byte size = 1; //单位字节

        //调用allocateMemory分配内存,并获取内存地址memoryAddress
        long memoryAddress = unsafe.allocateMemory(size);
        //直接往内存写入数据
        unsafe.putAddress(memoryAddress, data);
        //获取指定内存地址的数据
        long addrData = unsafe.getAddress(memoryAddress);
        System.out.println("addrData:"+addrData);

        /**
         * 输出结果:
         sun.misc.Unsafe@0f18aef2
         staticField:class com.demo.Demo
         设置前的Static字段值:Demo_Static
         设置后的Static字段值:Hello Java!
         输出USER:Demo{str='Hello Word!', i='1', staticStr='Hello Java!'}
         addrData:1000
         */

    }
}

class Demo{
    public Demo(){
        System.out.println("我是Demo类的构造函数，我被人调用创建对象实例啦....");
    }
    private String str;
    private int i;
    private static String staticStr = "Demo_Static";

    @Override
    public String toString() {
        return "Demo{" +
                "str = '" + str + '\'' +
                ", i = '" + i +'\'' +
                ", staticStr = " + staticStr +'\'' +
                '}';
    }
}
