package deepcopy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class t {
    public static void main(String[] args) throws CloneNotSupportedException ,InterruptedException{
        cloneTest cd = new cloneTest(1,"cloneTest");
        System.out.println("-------------这是浅拷贝-------------");
        cloneTest cd2 =(cloneTest) cd.clone();
        System.out.println(cd == cd2);
        System.out.println(cd.b == cd2.b);
        cd.b= "7";
        System.out.println(cd.b == cd2.b);

        System.out.println("-------------这是深拷贝-------------");
        cloneDeep cloneDeep =   new cloneDeep(1,"deepclone");
        cloneDeep cloneDeep2 = (cloneDeep) cloneDeep.clone();
        System.out.println(cloneDeep2.b == cloneDeep.b);

        System.out.println("-------------这是使用正常的引用复制-------------");
        cloneTest cloneTest = new cloneTest(3,"normal test");
        cloneTest cloneTest1 = cloneTest;
        System.out.println(cloneTest1 == cloneTest);
        cloneTest1.b = "4";
        System.out.println(cloneTest1.b == cloneTest.b);

        System.out.println("-----------------数组拷贝------------------");
        int[] arr = {1, 2, 3};
        int[] copy = Arrays.copyOf(arr, arr.length);
        System.out.println(copy[0] == arr[0]);
        String[] strArr = {"a", "b"};
        String[] strCopy = Arrays.copyOf(strArr, strArr.length);
        System.out.println(strCopy[0] == strArr[0]);

        System.out.println("-----------深层拷贝---------");
        Item field = new Item("1");
        a ta= new a(field);
        a ta2 = (a) ta.clone();
        System.out.println(ta.field.name == ta2.field.name);
        System.out.println("modify");
        ta2.field = new Item("3");
        System.out.println(ta.field.name == ta2.field.name);
        ta2.field = new Item("1");
        System.out.println(ta.field.name == ta2.field.name);

        System.out.println("集合的拷贝");
        List<String> l = new ArrayList<>(Arrays.asList("1","2","3"));
        List<String> l1 = new ArrayList<>(l);
        System.out.println("浅拷贝");
        System.out.println(l.get(0) == l1.get(0));
        Thread.sleep(100000);
    }
    static class a implements Cloneable {
        public Item field;
        public a(Item field) {
            this.field = field;
        }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            a copy = (a) super.clone();
            copy.field = new Item(this.field.name); // 深拷贝 Item
            return copy;
        }
    }
    static class Item {
        String name;
        Item(String name) { this.name = name; }
    }
}
class cloneTest implements Cloneable{
    public int a ;
    public String b;
    public cloneTest(int a, String b){
        this.a = a;
        this.b = b;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
//        @IntrinsicCandidate
//        protected native Object clone() throws CloneNotSupportedException;
    }
}
class cloneDeep implements Cloneable{
    public int a ;
    public String b;
    public cloneDeep(int a, String b){
        this.a = a;
        this.b = b;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        cloneDeep copy = (cloneDeep) super.clone();
        copy.b = new String(this.b);
        return copy;
    }
}