package probSeg2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

/**
 * 一元概率分词 第二个版本
 * 从后往前算最佳后继
 * 
 * @author 罗刚
 *
 */
public class Segmenter {
	double[] prob; // 节点概率

	// 计算节点i的最佳后继节点
	public void getBestSuc(AdjList g, int i, String[] bestSucWords) {
		Iterator<CnToken> it = g.getSuc(i);// 得到后继词集合，从中挑选最佳后继节点

		double maxProb = Double.NEGATIVE_INFINITY;
		String bestSucWord = null; // 候选最佳后继词

		//System.out.println("node id " + i);

		while (it.hasNext()) {
			CnToken itr = it.next();
			double nodeProb = itr.logProb + prob[itr.end];// 候选节点概率

			//System.out.println("nodeProb is " + nodeProb);

			if (nodeProb > maxProb) {// 概率最大的算作最佳后继
				bestSucWord = itr.termText;
				maxProb = nodeProb;
				//System.out.println("best nodeProb is" + nodeProb);
			}
		}
		prob[i] = maxProb;// 节点概率
		bestSucWords[i] = bestSucWord; // 最佳后继词
		//System.out.println("node " + i + " best Suc is" + bestSucWord);
	}

	// 计算出最佳切分词序列
	public Deque<String> maxProb(AdjList g) {
		String[] bestSucWords = new String[g.verticesNum]; // 最佳后继词
		prob = new double[g.verticesNum]; // 节点概率
		prob[g.verticesNum - 1] = 0;// 最后一个节点的初始概率是1,取log后是0

		// 按节点求最佳后继
		// 从后往前算
		for (int index = g.verticesNum - 2; index >= 0; index--) {
			// 求出最佳后继
			getBestSuc(g, index, bestSucWords);
		}

		return bestPath(bestSucWords);
	}

	public Deque<String> split(String sentence) {
		AdjList g = getLattice(sentence); // 得到词图
		//System.out.println(g.toString());
		return maxProb(g);
	}

	/**
	 * 得到词图
	 * 
	 * @param sentence
	 * @return 词图
	 */
	public AdjList getLattice(String sentence) {
		TernarySearchTrie dict = TernarySearchTrie.getInstance(); // 得到词典
		int sLen = sentence.length();// 字符串长度
		AdjList g = new AdjList(sLen + 1);// 存储所有被切分的可能的词
		int j; // 结束位置

		// 用来存放后继词的集合
		ArrayList<WordEntry> sucWords = new ArrayList<WordEntry>(); // 可以剪枝

		// 形成切分词图
		for (int i = 0; i < sLen;) {
			boolean match = dict.matchAll(sentence, i, sucWords);// 到词典中查询

			if (match) {// 已经匹配上
				for (WordEntry word : sucWords) {
					// System.out.println(word);
					j = i + word.term.length();
					double logProb = Math.log(word.freq) - Math.log(dict.n);
					// 把词放入词图
					g.addEdge(new CnToken(i, j, logProb, word.term));
				}
				i++;
			} else {
				j = i + 1;
				double logProb = Math.log(1) - Math.log(dict.n); // Math.log(1)=0
				// 把单字放入词图
				g.addEdge(new CnToken(i, j, logProb, sentence.substring(i, j)));
				i = j;
			}
		}
		return g;
	}

	// 根据最佳前驱节点数组回溯求解词序列
	public static Deque<String> bestPath(String[] bestSucWords) {
		Deque<String> path = new ArrayDeque<String>(); // 最佳词序列
		int i = 0;
		while (bestSucWords[i] != null) {
			//System.out.println("节点" + i + "的最佳后继词:" + bestSucWords[i]);
			path.add(bestSucWords[i]);
			i = i + bestSucWords[i].length();
		}

		return path;
	}
}
