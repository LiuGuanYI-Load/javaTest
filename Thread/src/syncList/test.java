package syncList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Collections.synchronizedList(list);

        List<Object> obj = new CopyOnWriteArrayList();
/*      final transient Object lock = new Object();
        public boolean add(E e) {
            synchronized(this.lock) {
                Object[] es = this.getArray();
                int len = es.length;
                es = Arrays.copyOf(es, len + 1);
                es[len] = e;
                this.setArray(es);
                return true;
            }
        }*/

    }

}
