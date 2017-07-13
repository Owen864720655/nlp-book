/*
 * Created on 2005-9-7
 *
 */
package trigramSeg;

/**
 *  邻接矩阵表示的切分词图
 * @author luogang
 * @2010-3-18
 */
import java.util.HashMap;
import java.util.Iterator;

public class AdjList implements Iterable<Node> {
	private CnTokenLinkedList list[];// AdjList的图的结构
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
		list[newEdge.end].put(newEdge);
	}

	/**
	 * Returns an iterator that contains the Edges leading to adjacent nodes
	 */
	public Iterator<CnToken> getPrev(int vertex) {
		CnTokenLinkedList ll = list[vertex];
		if (ll == null)
			return null;
		return ll.iterator();
	}

	/**
	 * Feel free to make this prettier, if you'd like
	 */
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
	public Iterator<Node> iterator() {
		return new AdjIterator();
	}

	private class AdjIterator implements Iterator<Node> {
		int pos;
		Iterator<CnToken> it1;
		CnToken t;
		Iterator<CnToken> it2;

		public AdjIterator() {
			pos = 1; // 去掉开始节点
			it1 = list[pos].iterator();

			if (it1.hasNext()){
				t = it1.next();
				it2 = list[t.start].iterator();
				if(it2.hasNext())
					return;
			}
			while (pos < (verticesNum - 1)) {
				pos++;
				it1 = list[pos].iterator();
				while (it1.hasNext()){
					t = it1.next();
					it2 = list[t.start].iterator();
					if(it2.hasNext())
						return;
				}
			}
		}

		public boolean hasNext() {
			if (it2.hasNext())
				return true;
			while (pos < (verticesNum - 1)) {
				pos++;
				it1 = list[pos].iterator();
				while (it1.hasNext()){
					t = it1.next();
					it2 = list[t.start].iterator();
					if(it2.hasNext())
						return true;
				}
			}
			return false;
		}

		public Node next() {
			if (!hasNext()) {
				return null;
			}
			CnToken t2 = it2.next();
			return getNode(t2,t);
		}
		
		@Override
		public void remove(){
			//throw new Exception("not support");
		}
	}

	public Node[] prevNodeSet(Node n) {
		System.out.println("prevNodeSet:"+n);
		if(n.start<0)
			return null;
		CnTokenLinkedList ll = list[n.start];
		Node[] nodes = new Node[ll.size()];

		int i = 0;
		for (CnToken t : ll) {
			nodes[i] = getNode(t.start, n.start, n.mid,t.logProb);
			i++;
		}
		return nodes;
	}
	
	HashMap<Node,Node> cache = new HashMap<Node,Node>();  //节点缓存
	
	public Node getNode(int s, int m, int e,double p){
		Node test = new Node(s,m,e,p);
		Node old = cache.get(test); //看是否已经创建过这个节点
		if(old !=null)
			return old; //如果已经创建过，就返回原来的节点
		//如果还没有创建过，就返回新的节点，并把新节点放入缓存
		cache.put(test, test);
		return test;
	}

	public Node getNode(CnToken t1, CnToken t2){
		Node test = new Node(t1,t2);
		Node old = cache.get(test);
		if(old !=null)
			return old;
		cache.put(test, test);
		return test;
	}
}
