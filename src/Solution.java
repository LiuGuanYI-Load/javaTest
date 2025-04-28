import java.util.ArrayList;
import java.util.List;

class Solution {
    public static  ListNode sortList(ListNode head) {
        if (head==null){
            return null;
        }
        List<ListNode> list=new ArrayList<>();
        while(head!=null){
            list.add(head);
            head=head.next;
        }
        list.sort((a,b)-> a.val-b.val);
        ListNode ansPre=new ListNode(-1);
        ListNode rt=ansPre;
        for(int i=0;i<list.size();i++){
            ansPre.next=list.get(i);
            ansPre=ansPre.next;
        }
        ansPre.next=null;
        return rt.next;
    }

    public static void main(String[] args) {
        ListNode n1=new ListNode(2);
        ListNode n2=new ListNode(4);
        ListNode n3=new ListNode(5);
        ListNode n4=new ListNode(3);
        ListNode n5=new ListNode(7);
        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;

        printList(sortList(n1));



    }
 static class ListNode {
         int val;
         ListNode next;
         ListNode() {}
         ListNode(int val) { this.val = val; }
         ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     }
    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
        System.out.println();
    }
}