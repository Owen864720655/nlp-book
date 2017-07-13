package templateSeg;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * 规则的分词
 * 动态规划概率分词 加 文法树
 * 
 * @author luogang
 *
 */
public class RuleSegmenter {
	TernarySearchTrie dic = null; //基本词
	public Trie rule = new Trie(); // 文法树  TODO 文法自动机
	static int seq = 0; // 用来保证唯一性
	int[] sucNode; // 最佳后继节点
	double[] prob;

	public RuleSegmenter() {
		DicFactory dicFactory = // new DicFileFactory();
		new DicDBFactory();
		dic = dicFactory.create();
	}

	public RuleSegmenter(DicFactory dicFactory) {
		dic = dicFactory.create();
	}

	// 增加一个切分规则
	public void addRule(String pattern) {
		String uniqueName = String.valueOf(seq); // 唯一的名字
		Rule rightRule = RuleParser.parse(uniqueName, pattern);
		rule.addRule(rightRule.typeSeq);
		dic.addWords(rightRule.words);
		seq++;
	}

	/**
	 * 
	 * @param pos
	 * @return AdjListDoc 表示一个列表图
	 */
	public AdjList getLattice(String sentence) {
		if (sentence == null || sentence.length() == 0) {
			return null;
		}
		int atomCount;
		int start = 0;
		atomCount = sentence.length();
		// TODO 原子切分
		AdjList g = new AdjList(atomCount + 1); // 初始化在Dictionary中词组成的图
		
		while (true) {// 在这里开始进行分词
			TernarySearchTrie.PrefixRet prefix = new TernarySearchTrie.PrefixRet();
			boolean matchRet = dic.matchAll(sentence, start, prefix);
			if (matchRet) {// 匹配上
				for (WordEntry word : prefix.values) {
					int end = start + word.word.length();// 词的结束位置
					double logProb = Math.log(word.freq) - Math.log(dic.n);
					System.out.println(word.word + " word.freq:" + word.freq);
					CnToken tokenInf = new CnToken(start, end, logProb,
							word.word, word.types);
					g.addEdge(tokenInf);
				}
				start++;
			} else { // 没匹配上
				double logProb = Math.log(1) - Math.log(dic.n);
				g.addEdge(new CnToken(start, start + 1, logProb, sentence
						.substring(start, start + 1), null));
				start++;
			}
			if (start >= atomCount) {
				break;
			}
		}

		return g;
	}

	public ArrayDeque<Integer> bestPath(AdjList g) {
		ArrayDeque<Integer> path = new ArrayDeque<Integer>();
		// 通过回溯发现最佳切分路径
		// 从前往后找最佳后继节点
		for (int i = sucNode[0]; i < (g.verticesNum - 1); i = sucNode[i]) {
			path.add(i);
		}
		path.add(g.verticesNum - 1);
		return path;
	}

	public AdjList combineSuc(String sentence) {
		//词典匹配
		AdjList g = getLattice(sentence); //得到词图
		System.out.println("词图:" + g);
		
		//一元概率分词的动态规划求解
		sucNode = new int[g.verticesNum];
		prob = new double[g.verticesNum]; // 节点概率
		// 按节点求最佳后继
		// 从后往前算
		for (int index = g.verticesNum - 2; index >= 0; index--) {
			// 求出最大后继
			getBestSuc(g, index);
		}

		//如果匹配上规则，就采用规则的切分结果
		for (int offset = 0; offset < sentence.length(); ++offset) {
			//匹配规则Trie树
			//词图和规则做交集 看看能否匹配上规则
			GraphMatcher.MatchValue match = GraphMatcher.intersect(g, offset,
					rule);

			// 如果匹配上规则，就为匹配上的这几个节点设置最佳后继节点
			if (match != null) {
				System.out.println("匹配规则结果:" + match);
				for (NodeType n : match.posSeq) {
					sucNode[n.start] = n.end; //设置最佳后继节点
				}
				offset = match.end;
			}
		}

		return g;
	}

	public ArrayDeque<Integer> split(String sentence) {
		AdjList g = combineSuc(sentence);

		ArrayDeque<Integer> path = bestPath(g);
		return path;
	}

	// 计算节点i的最佳后继节点
	void getBestSuc(AdjList g, int i) {
		Iterator<CnToken> it = g.getSuc(i);// 得到前驱词集合，从中挑选最佳前趋词
		double maxProb = Double.NEGATIVE_INFINITY;
		int maxNode = -1;

		System.out.println("node id " + i);

		while (it.hasNext()) {
			CnToken itr = it.next();
			double nodeProb = prob[itr.end] + itr.logProb;// 候选节点概率

			System.out.println("nodeProb is " + nodeProb);

			if (nodeProb > maxProb)// 概率最大的算作最佳前趋
			{
				maxNode = itr.end;
				maxProb = nodeProb;
				System.out.println("best nodeProb is" + nodeProb);
			}
		}
		// if(sucNode[i]==0){
		prob[i] = maxProb;// 节点概率
		sucNode[i] = maxNode;// 最佳后继节点
		// }
		System.out.println("node " + i + " best Suc is" + maxNode);
	}

}
