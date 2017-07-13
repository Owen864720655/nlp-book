package tagger;

import java.util.Iterator;

public class WordTypes implements Iterable<WordTypes.WordTypeInf> {
	public static class WordTypeInf {
		public PartOfSpeech pos; // 类型
		public long weight = 0; // 频率

		public WordTypeInf(PartOfSpeech p, long f) {
			pos = p;
			weight = f;
		}

		public String toString() {
			return pos + ":" + weight;
		}

		public long getWeight() {
			return weight;
		}

		public void setWeight(long weight) {
			this.weight = weight;
		}

		public PartOfSpeech getPos() {
			return pos;
		}
	}

	public static class LinkNode {
		public WordTypeInf item;
		public LinkNode next;

		public LinkNode(WordTypeInf data, LinkNode next) {
			item = data;
			this.next = next;
		}
	}

	private LinkNode head;

	public WordTypes() {
		head = null;
	}

	public WordTypes(WordTypeInf item) {
		head = new LinkNode(item, null);
	}

	public void put(WordTypeInf item) {
		head = new LinkNode(item, head);
		
	}

	/*public void insert(WordTypeInf item) {
		// one element
		if (head == tail) {
			if (head.item.pos.compareTo(item.pos) > 0) {
				Node t = head;
				head = new Node(item);
				head.next = t;
			} else {
				Node t = tail;
				tail = new Node(item);
				t.next = tail;
			}
			return;
		}
		Node t = head;

		while (t.next != null) {
			if (t.next.item.pos.compareTo(item.pos) > 0)
				break;
			t = t.next;
		}
		Node p = t.next;
		t.next = new Node(item);
		t.next.next = p;
	}*/

	public LinkNode getHead() {
		return head;
	}

	/*public WordTypeInf findType(PartOfSpeech toFind) {
		if (head == null)
			return null;
		Node t = head;
		while (t != null && t.item != null) {
			if (t.item.pos.equals(toFind)) {
				return t.item;
			}
			t = t.next;
		}

		return null;
	}*/

	/**
	 * @return
	 */
	public int size() {
		int count = 0;

		// if (head == null)
		// return count;
		LinkNode t = head;
		while (t != null) {
			count++;
			t = t.next;
		}

		return count;
	}

	public long totalCost() {
		long cost = 0;

		LinkNode t = head;
		while (t != null) {
			cost += t.item.weight;
			t = t.next;
		}

		return cost;
	}

	public Iterator<WordTypes.WordTypeInf> iterator() {
		// System.out.println("iterator");
		return new LinkIterator(head);
	}

	/** Adapter to provide descending iterators via ListItr.previous */
	private class LinkIterator implements Iterator<WordTypes.WordTypeInf> {
		LinkNode itr;

		public LinkIterator(LinkNode begin) {
			itr = begin;
		}

		public boolean hasNext() {
			return itr != null;
		}

		public WordTypeInf next() {
			LinkNode cur = itr;
			itr = itr.next;
			return cur.item;
		}

		public void remove() {
			// itr.remove();
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		LinkNode pCur = head;

		while (pCur != null) {
			buf.append(pCur.item.toString());
			buf.append(' ');
			pCur = pCur.next;
		}

		return buf.toString();
	}

}
