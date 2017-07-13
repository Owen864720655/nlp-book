/**
 * Node.java
 * Version 1.0.0
 * Created on 2013-8-8
 * Copyright lietu.com
 *
 */
package trigramSeg;

/**
 * 组合节点
 * 
 * @author luogang
 * @2013-8-8
 */
public class Node {

	public int start; // 开始节点
	public int mid; // 中间节点
	public int end; // 结束节点

	public double logProb; // 节点本身的概率
	public double nodeProb;// 节点累积概率
	public Node bestPrev; // 最佳前驱节点

	public Node(int s, int m, int e, double p) {
		start = s;
		mid = m;
		end = e;
		logProb = p;
	}

	public Node(CnToken t1, CnToken t2) {
		start = t1.start;
		mid = t1.end;
		end = t2.end;
		this.logProb = t1.logProb;
	}

	@Override
	public int hashCode() {
		return start ^ mid ^ end;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) // 判断传入对象的类型
			return false;
		Node that = (Node) o;

		return (this.start == that.start && this.mid == that.mid && this.end == that.end);
	}

	public String toString() {
		return start + ":" + mid + ":" + end;
	}
}
