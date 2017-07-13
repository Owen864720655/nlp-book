/**
 * Node.java
 * Version 1.0.0
 * Created on 2013-8-8
 * Copyright lietu.com
 *
 */
package bigramSeg;

/**
 * 组合节点
 * 
 * @author luogang
 * @2013-8-8
 */
public class Node {
	public int start; // 开始节点
	public int end; // 结束节点

	public double logProb; // 节点本身的概率
	public double nodeProb;// 节点累积概率
	public Node bestPrev; // 最佳前驱节点

	public Node(int s, int e, double p) {
		start = s;
		end = e;
		logProb = p;
	}

	public Node(CnToken t) {
		start = t.start;
		end = t.end;
		this.logProb = t.logProb;
	}

	@Override
	public int hashCode() {
		return start ^ end;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) // 判断传入对象的类型
			return false;
		Node that = (Node) o;

		return (this.start == that.start && this.end == that.end);
	}

	public String toString() {
		return start + ":" + end;
	}
}
