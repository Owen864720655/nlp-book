package templateSeg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;

/**
 * 匹配图
 * 
 * @author luogang
 * @2014-1-26
 */
public class GraphMatcher {

	// private static Logger logger = Logger.getLogger(GraphMatcher.class);
	//状态对
	public static class StatePair {
		ArrayList<NodeType> path; //走过的词序列
		int s1; // 词图中的状态，也就是位置
		TrieNode s2; // 词性规则Trie树中的状态，也就是节点

		public StatePair(ArrayList<NodeType> p, int state1, TrieNode state2) {
			path = p;
			this.s1 = state1;
			this.s2 = state2;
		}

		@Override
		public String toString() {
			return "StatePair [path=" + path + ", s1=" + s1 + ", s2=" + s2
					+ "]";
		}
	}

	public static class NextInput {// 有限状态机中的下一个输入
		int end; // 词的结束位置,词图中的下一个状态编号
		String type; // 经过的类型,规则树中下一个状态的依据

		public NextInput(int e, String t) {
			this.end = e;
			this.type = t;
		}
	}

	public static final class MatchValue {
		public ArrayList<NodeType> posSeq; // 词性序列
		public int start;// 开始
		public int end;// 结束

		public MatchValue(ArrayList<NodeType> p, int begin, int e) {
			posSeq = p;
			start = begin;
			end = e;
		}

		public String toString() {
			return "词性序列:" + posSeq.toString() + " 开始位置:" + start + " 结束位置:"
					+ end;
		}
	}

	/**
	 * 取得词图和规则树都可以向前进的步骤
	 * 
	 * @param edges
	 *            词图上的边
	 * @param s
	 *            规则树上的类型
	 * @return 共同的有效输入
	 */
	public static ArrayList<NextInput> intersection(CnTokenLinkedList edges,
			Set<String> s) {
		ArrayList<NextInput> tmp = new ArrayList<NextInput>();
		for (CnToken x : edges) {
			// System.out.println("cn token "+x);
			for (POSType t : x.types) {
				if (s.contains(t.pos)) { // 规则树上的类型包含词所属的类型
					tmp.add(new NextInput(x.end, t.pos));
				}
			}
		}
		return tmp;
	}

	/**
	 * 图的指定位置开始和trie树匹配
	 * 
	 * @param g
	 *            人名特征词图
	 * @param offset
	 *            开始位置
	 * @return 匹配结果
	 */
	public static MatchValue intersect(AdjList g, int offset,
			Trie ruleTrie) {
		MatchValue candidateMatch = null; //候选值，用于最长匹配
		Deque<StatePair> stack = new ArrayDeque<StatePair>(); // 存储遍历状态的堆栈
		ArrayList<NodeType> path = new ArrayList<NodeType>(); // 类型序列

		stack.add(new StatePair(path, offset, ruleTrie.rootNode));
		while (!stack.isEmpty()) { // 堆栈内容不空
			StatePair stackValue = stack.pop(); // 弹出堆栈

			// 取出图中当前节点对应的边
			CnTokenLinkedList edges = g.edges(stackValue.s1);
			if (edges == null) {
				// logger.debug("edges is null");
				continue;
			}

			// 取出树中当前节点对应的类型
			Set<String> types = stackValue.s2.getChildren().keySet();
			// logger.debug("types "+types);
			// logger.debug("edges "+edges);
			// logger.debug("trieNode "+stackValue.s2);
			ArrayList<NextInput> ret = intersection(edges, types); // 输入求交集
			if (ret == null)
				continue;
			for (NextInput edge : ret) { // 遍历每个有效的输入
				// 向下遍历树
				TrieNode state2 = stackValue.s2.next(edge.type);
				// logger.debug("edge.type "+edge.type);
				// logger.debug("stackValue.s2.getChildren() "+stackValue.s2.getChildren());
				// logger.debug("stackValue.s2.getChildren().get(edge.type) "+stackValue.s2.getChildren().get("询问农药p-0"));
				// logger.debug("state2 "+state2);
				// 向前遍历图上的边
				int end = edge.end;
				ArrayList<NodeType> p = new ArrayList<NodeType>(stackValue.path); // 复制一个新的数组
				NodeType newNodeType = new NodeType(edge.type,stackValue.s1, end);
				p.add(newNodeType); // 把当前的Type加入到路径

				stack.add(new StatePair(p, end, state2)); // 压入堆栈
				if (state2.isTerminal()) { // 是可以结束的节点
					candidateMatch = getLongest(new MatchValue(p, offset, end),candidateMatch);
				}
			}
		}

		return candidateMatch;
	}
	
	//TODO 如果长度相同，则取 logP(wi) 最大的
	public static MatchValue getLongest(MatchValue toMatch,MatchValue candidateMatch){
		if(candidateMatch==null){
			return toMatch;
		}
		if(toMatch.end>candidateMatch.end){
			return toMatch;
		}
		return candidateMatch;
	}

}
