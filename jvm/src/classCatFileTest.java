import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


class Test {
/*    静态引用 obj：是的，obj（一个引用，指向堆中的 Object 实例）存储在 Metaspace 的静态变量区域，与 a 的存储位置相同。
    静态字段 a 的值：a 的值（1）不在字节码中，而是存储在 Metaspace 的静态变量区域。字节码只包含初始化的指令（如 iconst_1; putstatic），告诉 JVM 在运行时如何设置 a 的值。*/
    public static int a = 1;
    public static Object obj=new Object();
    public int b = 6;
    private Test(){

    }
}

class MyClass {
    static int x = 42;
    static Object obj = new Object();
    static {
        System.out.println("Static block");
    }
}

public class classCatFileTest {
    public static void main(String[] args) {
        try {
            // 获取 Test 类的 Class 对象
            Class<?> testClass = Test.class;
            System.out.println("类名: " + testClass.getName());

            Class<?> testClass2 = Class.forName("Test");
            System.out.println("类名 (forName): " + testClass2.getName());

            // 获取所有公共字段
            System.out.println("公共字段:");
            Field[] fields = testClass2.getFields();
            for (Field field : fields) {
                System.out.println(field);
            }

            // 获取静态字段 a 的值
            Field aField = testClass2.getField("a");
            //从那个引用里面得到值
            int aValue = aField.getInt(null); // 静态字段可以用 null
            System.out.println("a 的值: " + aValue);

            // 创建 Test 实例以获取实例字段 b 的值
            Constructor<?> constructor = testClass2.getDeclaredConstructor();
            constructor.setAccessible(true);
/*            getConstructor()	公共构造器（包括继承的）	仅public	需要外部调用的场景（如反射创建实例）
            getDeclaredConstructor()	当前类声明的构造器	无限制	需要访问私有或其他非public构造器的场景*/
            Test testInstance = (Test) constructor.newInstance();
            Field bField = testClass2.getField("b");
            int bValue = bField.getInt(testInstance); // 实例字段需要实例
            System.out.println("b 的值: " + bValue);

            // 打印 Test 实例和 hashCode
            System.out.println("Test 实例: " + testInstance);
            System.out.println("实例 hashCode: " + testInstance.hashCode());
            System.out.println("静态字段 a 直接访问: " + Test.a);


            Class<?> c1 = Test.class;
            Class<?> c2 = Class.forName("Test");
            System.out.println(c1 == c2); // true
        } catch (ClassNotFoundException e) {
            System.err.println("类未找到: " + e.getMessage());
        } catch (NoSuchFieldException e) {
            System.err.println("字段未找到: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("无法访问字段: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("构造器未找到: " + e.getMessage());
        } catch (InstantiationException | InvocationTargetException e) {
            System.err.println("实例化失败: " + e.getMessage());
        }
    }
}

/*
Class 对象存储着什么信息  --看一眼便知
jav@LGuanYi ~/c/J/t/T/src> javap -v Test.class
Classfile /home/jav/code/Java/threadPoolTest/Thread/src/Test.class
Last modified Apr 11, 2025; size 298 bytes
SHA-256 checksum 537da1ef052ed817e54ab1ae64432267bc8b9be0352f77dc56c5185304bc1846
Compiled from "classCatFileTest.java"
class Test
  minor version: 0
major version: 61
flags: (0x0020) ACC_SUPER
this_class: #8                          // Test
super_class: #2                         // java/lang/Object
interfaces: 0, fields: 2, methods: 2, attributes: 1
Constant pool:
        #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
        #2 = Class              #4             // java/lang/Object
        #3 = NameAndType        #5:#6          // "<init>":()V
        #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // Test.b:I
        #8 = Class              #10            // Test
        #9 = NameAndType        #11:#12        // b:I
        #10 = Utf8               Test
  #11 = Utf8               b
  #12 = Utf8               I
  #13 = Fieldref           #8.#14         // Test.a:I
        #14 = NameAndType        #15:#12        // a:I
        #15 = Utf8               a
  #16 = Utf8               Code
  #17 = Utf8               LineNumberTable
  #18 = Utf8               <clinit>
  #19 = Utf8               SourceFile
  #20 = Utf8               classCatFileTest.java
{
    public static int a;
    descriptor: I
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC

    public int b;
    descriptor: I
    flags: (0x0001) ACC_PUBLIC

    static {};
    descriptor: ()V
    flags: (0x0008) ACC_STATIC
    Code:
    stack=1, locals=0, args_size=0
    0: iconst_1
    1: putstatic     #13                 // Field a:I
    4: return
        LineNumberTable:
    line 62: 0
}
***
* @Author: Jav
* @Date: 2025/4/11
* @Description:
* @Param:
* @return:  ****************************************************************************************************
*                                                       带着注释版本的字节码
**************************************************************************************************************
*/
/*
// 文件路径，指定字节码文件的位置
Classfile /home/jav/code/Java/threadPoolTest/Thread/src/Test.class
        // 最后修改时间和文件大小，反映文件元数据
Last modified Apr 11, 2025; size 298 bytes
// SHA-256 校验和，用于验证文件完整性
SHA-256 checksum 537da1ef052ed817e54ab1ae64432267bc8b9be0352f77dc56c5185304bc1846
// 源文件名，说明字节码从 classCatFileTest.java 编译而来
Compiled from "classCatFileTest.java"
// 类声明，定义 Test 类
class Test
        // 次版本号，通常为 0
    minor version: 0
// 主版本号，61 对应 JDK 17
major version: 61
// 类访问标志，0x0020 表示 ACC_SUPER（支持超类调用）
flags: (0x0020) ACC_SUPER
// 当前类，指向常量池 #8（Test）
this_class: #8                          // Test
// 父类，指向常量池 #2（java/lang/Object）
super_class: #2                         // java/lang/Object
// 接口数量，0 表示没有实现接口
interfaces: 0
// 字段数量，2 个字段
fields: 2
// 方法数量，2 个方法
methods: 2
// 类属性数量，1 个属性
attributes: 1

// 常量池开始，存储类、方法、字段等引用
Constant pool:
        // #1: 方法引用，指向 Object 的无参构造函数
        #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
        // #2: 类引用，指向 java/lang/Object
        #2 = Class              #4             // java/lang/Object
        // #3: 方法名和描述符，构造函数 <init>，无参返回 void
        #3 = NameAndType        #5:#6          // "<init>":()V
        // #4: 字符串，java/lang/Object 类名
        #4 = Utf8               java/lang/Object
// #5: 字符串，构造函数方法名
   #5 = Utf8               <init>
// #6: 字符串，方法描述符，无参返回 void
   #6 = Utf8               ()V
// #7: 字段引用，Test 类的 b 字段，类型 int
   #7 = Fieldref           #8.#9          // Test.b:I
        // #8: 类引用，Test 类
        #8 = Class              #10            // Test
        // #9: 字段名和类型，b 字段，类型 int
        #9 = NameAndType        #11:#12        // b:I
        // #10: 字符串，Test 类名
        #10 = Utf8               Test
// #11: 字符串，字段名 b
   #11 = Utf8               b
// #12: 字符串，int 类型
   #12 = Utf8               I
// #13: 字段引用，Test 类的 a 字段，类型 int
   #13 = Fieldref           #8.#14         // Test.a:I
        // #14: 字段名和类型，a 字段，类型 int
        #14 = NameAndType        #15:#12        // a:I
        // #15: 字符串，字段名 a
        #15 = Utf8               a
// #16: 字符串，Code 属性名
   #16 = Utf8               Code
// #17: 字符串，LineNumberTable 属性名
   #17 = Utf8               LineNumberTable
// #18: 字符串，静态初始化方法名 <clinit>
   #18 = Utf8               <clinit>
// #19: 字符串，SourceFile 属性名
   #19 = Utf8               SourceFile
// #20: 字符串，源文件名 classCatFileTest.java
   #20 = Utf8               classCatFileTest.java

// 类主体开始，定义字段和方法
{
    // 字段：public static int a
    public static int a;
    // 字段描述符，I 表示 int
    descriptor: I
    // 字段标志，0x0009 = ACC_PUBLIC | ACC_STATIC
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC

    // 字段：public int b
    public int b;
    // 字段描述符，I 表示 int
    descriptor: I
    // 字段标志，0x0001 = ACC_PUBLIC
    flags: (0x0001) ACC_PUBLIC

    // 方法：静态初始化块 <clinit>
    static {};
    // 方法描述符，无参返回 void
    descriptor: ()V
    // 方法标志，0x0008 = ACC_STATIC
    flags: (0x0008) ACC_STATIC
    // Code 属性，包含字节码指令
    Code:
    // 栈最大深度 1，无局部变量，无参数
    stack=1, locals=0, args_size=0
    // 指令：加载常量 1 到栈顶
    0: iconst_1
    // 指令：将栈顶值存储到 Test.a 字段
    1: putstatic     #13                 // Field a:I
    // 指令：方法返回
    4: return
        // 行号表，指令偏移量 0 对应源代码第 62 行
        LineNumberTable:
    line 62: 0
}

// 类属性：源文件名
SourceFile: classCatFileTest.java*/
