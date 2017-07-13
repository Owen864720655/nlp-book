package dp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Segmenter {
	final static double minValue = -1000000.0;
	private static final SuffixTrie dic = SuffixTrie.getInstance();
	String text;
	int[] prevNode;
	
	Segmenter(String t){
		text = t;
	}
	
	public List<String> split() {
		prevNode = new int[text.length() + 1];// 最佳前驱节点数组
		double[] prob = new double[text.length() + 1]; // 节点概率
		prevNode[0] = 0;

		//用来存放前驱词的集合
		ArrayList<WordType> prevWords = new ArrayList<WordType>();
		
		// 求出每个节点的最佳前驱节点
		for (int i = 1; i < prevNode.length; i++){ //从前往后
			
			double maxProb = minValue; // 候选节点概率
			//System.out.println("minValue "+minValue);
			
			int maxNode = 0; // 候选最佳前驱节点

			//从词典中查找前驱词的集合
			dic.matchAll(text, i - 1,prevWords);
			
			//根据前驱词集合挑选最佳前趋节点
			for (WordType word : prevWords) {
				double wordProb = Math.log(word.freq) - Math.log(dic.wordCount);
				//System.out.println("wordProb "+wordProb);
				int start = i - word.word.length(); // 候选前驱节点
				double nodeProb = prob[start] + wordProb;// 候选节点概率
				
				//System.out.println("nodeProb "+nodeProb);
				
				if (nodeProb > maxProb) {// 概率最大的算作最佳前趋
					maxNode = start;
					maxProb = nodeProb;
				}
			}
			
			prob[i] = maxProb;// 节点概率
			prevNode[i] = maxNode;// 最佳前驱节点

			//System.out.println("i"+prob[i] + " prevNode[i]"+maxNode);
		}
		
		return bestPath();
	}
	
	public List<String> bestPath(){ // 根据最佳前驱节点数组回溯求解词序列
		Deque<Integer> path = new ArrayDeque<Integer>(); //最佳节点序列
		// 从后向前回朔最佳前驱节点
		for (int i = text.length(); i > 0; i = prevNode[i]){
			//System.out.println("i"+i);
			path.push(i);
		}
		List<String> words = new ArrayList<String>(); //切分出来的词序列
		int start = 0;
		for (Integer end : path) {
			words.add(text.substring(start, end));
			//System.out.println("word"+text.substring(start, end));
			start = end;
		}
		return words;
	}

	public static void main(String[] args) {
		String sentence = //"他很聪明,很活跃";
			//"当前节点为空,说明词典中找不到对应的词,则将单个字符返回";
		 //"中国成立了";// "大学生活动中心"; //
			"有意见分歧";
		Segmenter seg = new Segmenter(sentence);
		List<String> ret = seg.split();

		for (String end : ret) {
			System.out.print(end+" / ");
		}
	}
}
