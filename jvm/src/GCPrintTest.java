import java.util.HashMap;

public class GCPrintTest {
//    static List<Object> list=new ArrayList<>();
    static void newObject(){
        for (int i = 0; i <= 10000; i++) {
          new Object();
          new HashMap<>();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (;;) {
            newObject();
        }
    }
}
