package segInterface;

public class WordLinkedList {
	class Node {
		public char item; 	// 每个节点存储词中的一个字符
		public Node next; // 每一个节点里记录下一个节点对象

		public Node(char item) {
			this.item = item;
			next = null;
		}
	}

	private Node head; // 记录第一个节点
	private Node tail; // 记录最后一个节点

	public WordLinkedList() {
		head = null;
		tail = null;
	}

	public void put(String item) {
		for (int i = 0; i < item.length(); i++) {
			put(item.charAt(i));
		}
	}

	public void put(char item) {
		Node t = tail;
		tail = new Node(item);
		if (head == null)
			head = tail;
		else
			t.next = tail;
	}

	public String toString() { // 输出链表中的内容
		StringBuilder buf = new StringBuilder();
		Node pCur = head;

		while (pCur != null) {
			buf.append(pCur.item);
			buf.append('\t');
			pCur = pCur.next;
		}

		return buf.toString();
	}

	public static void main(String args[]) {
		WordLinkedList c = new WordLinkedList();
		c.put("中华人民共和国");
		System.out.println(c.toString());
	}
}
