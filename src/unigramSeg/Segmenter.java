package unigramSeg;

import java.util.ArrayDeque;

/**
 * 二元分词
 * 
 * @author luogang
 * @2013-8-7
 */
public class Segmenter {
	static TernarySearchTrie dict = TernarySearchTrie.getInstance(); // 得到词典
	static final double minValue = Double.NEGATIVE_INFINITY;

	Node startNode; // 开始词
	public Node endNode; // 结束词

	public ArrayDeque<Node> split(String sentence) {
		AdjList segGraph = getSegGraph(sentence); // 得到切分词图

		for (Node currentWord : segGraph) { // 从前往后遍历切分词图中的每个词
			// 得到当前词的前驱词集合
			Node[] prevWordList = segGraph
					.prevNodeSet(currentWord);
			double wordProb = minValue; // 侯选词概率
			Node minNode = null;
			if (prevWordList == null)
				continue;
			for (Node prevWord : prevWordList) {
				double currentProb = transProb(prevWord, currentWord)
						+ prevWord.nodeProb;
				if (currentProb > wordProb) {
					wordProb = currentProb;
					minNode = prevWord;
				}
			}
			currentWord.bestPrev = minNode; // 设置当前词的最佳前驱词
			currentWord.nodeProb = wordProb; // 设置当前词的词概率
		}

		return bestPath();
	}

	public ArrayDeque<Node> bestPath() {  //根据最佳前驱节点找切分路径
		ArrayDeque<Node> seq = new ArrayDeque<Node>(); // 切分出来的词序列

		for (Node t = endNode.bestPrev; t != startNode; t = t.bestPrev) { // 从右向左找最佳前驱节点
			seq.addFirst(t);
		}
		return seq;
	}

	public AdjList getSegGraph(String sentence) { // 返回切分词图
		int sLen = sentence.length();// 字符串长度
		AdjList g = new AdjList(sLen + 2);// 存储所有被切分的可能的词

		/*startNode = new CnToken(-1, 0, 0, "start"); // 句子开始词的结束位置是零
		g.addEdge(startNode);

		endNode = new CnToken(sLen, sLen + 1, 0, "end"); // 句子结束词的结束位置是句子长度加一
		System.out.println(endNode);
		g.addEdge(endNode);

		int j; // 词的结束位置
		TernarySearchTrie.PrefixRet wordMatch = new TernarySearchTrie.PrefixRet();

		// 生成切分词图
		for (int i = 0; i < sLen;) {
			boolean match = dict.getMatch(sentence, i, wordMatch);// 到词典中查询

			if (match)// 已经匹配上
			{
				for (WordEntry word : wordMatch.values) {
					// System.out.println(word);
					j = i + word.word.length();
					double logProb = Math.log(word.freq) - Math.log(dict.n);
					g.addEdge(new CnToken(i, j, logProb, word.word));
				}
				i = wordMatch.end;
			} else {
				j = i + 1;
				double logProb = Math.log(1) - Math.log(dict.n);
				g.addEdge(new CnToken(i, j, logProb, sentence.substring(i, j)));
				i = j;
			}
		}*/

		return g;
	}

	// 前后两个词的转移概率
	private double transProb(Node prevWord, Node currentWord) {
		// TODO prob
		//return prevWord.logProb;
		return 0.0;
	}

}
