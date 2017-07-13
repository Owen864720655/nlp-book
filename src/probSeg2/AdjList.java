/*
 * Created on 2005-9-7
 *
 */
package probSeg2;

/**
 *  正向邻接链表表示的切分词图
 *  正向AdjList 找最佳后继词
 * @author luogang
 * @2010-3-18
 */
import java.util.Iterator;

public class AdjList implements Iterable<CnToken> {
	public CnTokenLinkedList list[];// AdjList的图的结构
	public int verticesNum;

	/**
	 * Constructor: Allocates right amount of space
	 */
	public AdjList(int verticesNum) {
		this.verticesNum = verticesNum;
		list = new CnTokenLinkedList[verticesNum]; // 新建数组

		// initialize all of the linked lists in the array
		for (int index = 0; index < verticesNum; index++) {
			list[index] = new CnTokenLinkedList();
		}
	}

	/**
	 * add an edge to the graph
	 */
	public void addEdge(CnToken newEdge) {
		list[newEdge.start].put(newEdge); // 正向邻接链表
	}

	/**
	 * 取得后继词集合
	 * 
	 * @param start
	 *            开始点
	 * @return
	 */
	public Iterator<CnToken> getSuc(int start) {
		CnTokenLinkedList ll = list[start];
		if (ll == null)
			return null;
		return ll.iterator();
	}

	public String toString() {
		StringBuilder temp = new StringBuilder();

		for (int index = 0; index < verticesNum; index++) { // 每个节点一行
			if (list[index] == null) {
				continue;
			}
			temp.append("node:");
			temp.append(index);
			temp.append(": ");
			temp.append(list[index].toString());

			temp.append("\n");
		}

		return temp.toString();
	}

	@Override
	public Iterator<CnToken> iterator() {
		return new AdjIterator();
	}

	private class AdjIterator implements Iterator<CnToken> {
		int pos = 0; // 遍历到第几个链表
		Iterator<CnToken> it;

		public AdjIterator() {
			it = list[pos].iterator();
		}

		public boolean hasNext() {
			if (it.hasNext())
				return true;
			while (true) {
				pos++;
				if (pos >= verticesNum) {
					return false;
				}
				it = list[pos].iterator();
				if (it.hasNext())
					return true;
			}
		}

		public CnToken next() {
			if (!hasNext()) {
				return null;
			}
			CnToken t = it.next();
			return t;
		}

		public void remove() {
			throw new UnsupportedOperationException(); // 不支持删除当前元素这个操作
		}
	}

	public CnTokenLinkedList edges(int pos) {
		if (pos >= list.length) {
			return null;
		}
		return list[pos];
	}

}