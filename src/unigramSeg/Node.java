/**
 * Node.java
 * Version 1.0.0
 * Created on 2013-8-8
 * Copyright lietu.com
 *
 */
package unigramSeg;

/**
 * 一元节点
 * 
 * @author luogang
 * @2013-8-8
 */
public class Node {
	public int nodeId; // 节点编号

	public double nodeProb;// 节点累积概率
	public Node bestPrev; // 最佳前驱节点

	public Node(int s) {
		nodeId = s;
	}

	@Override
	public int hashCode() {
		return nodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) // 判断传入对象的类型
			return false;
		Node that = (Node) o;

		return (this.nodeId == that.nodeId);
	}

	public String toString() {
		return String.valueOf(nodeId);
	}
}
