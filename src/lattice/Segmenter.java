package lattice;

import java.util.ArrayList;

import com.lietu.automSeg.FSTSGraph;
import com.lietu.automSeg.SplitPoints;

public class Segmenter {
	static FSTSGraph fstSeg;

	static {
		try {
			fstSeg = new FSTSGraph();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到词图
	 * 
	 * @param sentence
	 * @return 词图
	 */
	public AdjList getLattice(String sentence) {
		// 原子切分
		SplitPoints splitPoints = fstSeg.splitPoints(sentence);
		
		TernarySearchTrie dict = TernarySearchTrie.getInstance(); // 得到词典
		int sLen = sentence.length();// 字符串长度
		AdjList g = new AdjList(sLen + 1);// 存储所有被切分的可能的词
		
		// 用来存放后继词的集合
		ArrayList<WordEntry> sucWords = new ArrayList<WordEntry>(); // 可以剪枝
		int start = 0;
		int currentEnd = splitPoints.endPoints.nextSetBit(0);
		
		while (start >= 0 && currentEnd>=0){// 在这里开始进行分词
			boolean match = dict.matchAll(sentence, start, sucWords,splitPoints.endPoints);// 到词典中查询
			if (match) {// 匹配上
				for (WordEntry word : sucWords) {
					int end = start + word.term.length();// 词的结束位置
					double logProb = Math.log(word.freq) - Math.log(dict.n);
					System.out.println(word.term +" word.freq:" + word.freq);
					CnToken tokenInf = new CnToken(start, end, logProb, word.term);
					g.addEdge(tokenInf);
				}
				currentEnd = splitPoints.endPoints.nextSetBit(currentEnd + 1);
				start = splitPoints.startPoints.nextSetBit(start + 1);
			} else { // 没匹配上
				double logProb = Math.log(1) - Math.log(dict.n);
				currentEnd = splitPoints.endPoints.nextSetBit(currentEnd + 1);
				System.out.println("currentEnd:" + currentEnd);
				g.addEdge(new CnToken(start, currentEnd,logProb, sentence.substring(
						start, currentEnd)));
				start = splitPoints.startPoints.nextSetBit(start + 1);
			}
			if (start >= sLen) {
				break;
			}
		}
		
		return g;
	}

}
