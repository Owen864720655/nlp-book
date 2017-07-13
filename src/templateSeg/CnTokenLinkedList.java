package templateSeg;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * 单词类的线性列表类
 * 
 * @author luogang
 * @2010-3-18
 */
public class CnTokenLinkedList implements Iterable<CnToken> {
	public static class Node {
		public CnToken item;
		public Node next;

		public Node(CnToken data, Node next) {
			item = data;
			this.next = next;
		}
	}

	private Node head;

	public CnTokenLinkedList() {
		head = null;
	}

	public void put(CnToken item) {
		head = new Node(item, head);
	}

	public Node getHead() {
		return head;
	}

	public int size() {
		int count = 0;

		Node t = head;
		while (t != null) {
			count++;
			t = t.next;
		}

		return count;
	}

	public Iterator<CnToken> iterator() {
		return new LinkIterator(head);
	}

	private class LinkIterator implements Iterator<CnToken> {
		Node itr;

		public LinkIterator(Node begin) {
			//System.out.println("LinkIterator");
			itr = begin;
		}

		public boolean hasNext() {
			return itr != null;
		}

		public CnToken next() {
			if (itr == null) {
				throw new NoSuchElementException();
			}
			//System.out.println("LinkIterator next");
			CnToken cur = itr.item;  //当前位置存储的值
			itr = itr.next;  //下一个位置
			return cur;
		}

		public void remove() {
			// itr.remove();
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		Node currentNode = head;

		while (currentNode != null) {
			buf.append(currentNode.item.toString());
			buf.append('\t');
			currentNode = currentNode.next;
		}

		return buf.toString();
	}
}