package trigramSeg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * 三元分词
 * 
 * @author luogang
 * @2013-8-7
 */
public class Segmenter {
	static TernarySearchTrie dict = TernarySearchTrie.getInstance(); // 得到词典
	static final double minValue = Double.NEGATIVE_INFINITY / 100;

	private Node endNode; // 结束节点
	String text;

	public Segmenter(String t) {
		text = t;
	}

	public ArrayDeque<Node> split() { // 返回最佳节点序列
		AdjList wordGraph = getWordGraph(text); // 得到切分词图

		for (Node currentNode : wordGraph) { // 从前往后遍历切分词图中的每个节点
			// 得到当前节点的前驱节点集合
			Node[] prevNodes = wordGraph.prevNodeSet(currentNode);
			double nodeProb = minValue; // 侯选词概率
			Node minNode = null;
			if (prevNodes == null)
				continue;
			for (Node prevNode : prevNodes) {
				double currentProb = transProb(prevNode, currentNode)
						+ prevNode.nodeProb;
				if (currentProb > nodeProb) {
					nodeProb = currentProb;
					minNode = prevNode;
					System.out.println("find best prev:" + currentNode + " "
							+ currentProb);
				}
			}
			currentNode.bestPrev = minNode; // 设置当前词的最佳前驱词
			currentNode.nodeProb = nodeProb; // 设置当前词的词概率
		}

		ArrayDeque<Node> seq = new ArrayDeque<Node>(); // 切分出来的节点序列

		System.out.println("end:" + endNode);
		for (Node t = endNode.bestPrev; t.start > -1; t = t.bestPrev) { // 从右向左找最佳前驱节点
			System.out.println("path:" + t);
			seq.addFirst(t);
		}
		return seq;
	}

	public List<String> bestPath(ArrayDeque<Node> path) { // 根据最佳前驱节点数组回溯求解词序列

		List<String> words = new ArrayList<String>(); // 切分出来的词序列
		int start = 0;
		for (Node end : path) {
			words.add(text.substring(start, end.mid));
			// System.out.println("word"+text.substring(start, end));
			start = end.mid;
		}
		return words;
	}

	public AdjList getWordGraph(String sentence) { // 返回切分词图
		int sLen = sentence.length();// 字符串长度
		AdjList g = new AdjList(sLen + 3);// 存储所有被切分的可能的词

		CnToken startWord = new CnToken(-1, 0, 0, "start");
		g.addEdge(startWord);

		CnToken endWord1 = new CnToken(sLen, sLen + 1, 0, "end1");
		g.addEdge(endWord1);

		CnToken endWord2 = new CnToken(sLen + 1, sLen + 2, 0, "end2");
		g.addEdge(endWord2);

		endNode = g.getNode(endWord1, endWord2);

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
		}

		return g;
	}

	// 前后两个词的转移概率
	private double transProb(Node prevWord, Node currentWord) {
		// TODO prob
		return prevWord.logProb;
	}

}
