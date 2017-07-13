/*
 * Created on 2005-9-7
 *
 */
package templateSeg;

/**
 *  正向邻接链表表示的切分词图
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
		list = new CnTokenLinkedList[verticesNum];

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

	public String toString() {
		StringBuilder temp = new StringBuilder();

		for (int index = 0; index < verticesNum; index++) {
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
		int pos = 0;
		Iterator<CnToken> it;

		public AdjIterator() {
			it = list[pos].iterator();
		}

		public boolean hasNext() {
			if (it.hasNext())
				return true;
			while (pos < (verticesNum - 1)) {
				pos++;
				it = list[pos].iterator();
				if (it.hasNext())
					return true;
			}
			return false;
		}

		public CnToken next() {
			if (!hasNext()) {
				return null;
			}
			CnToken t = it.next();
			return t;
		}

		public void remove() {
			it.remove();
			//throw new UnsupportedOperationException(); // 不支持删除当前元素这个操作
		}
	}

	public CnTokenLinkedList edges(int pos) {
		if(pos>=list.length){
			return null;
		}
		return list[pos];
	}

	//取得后继节点集合
	public Iterator<CnToken> getSuc(int start) {
		CnTokenLinkedList ll = list[start];
		if(ll == null)
			return null;
		return ll.iterator();
	}

}