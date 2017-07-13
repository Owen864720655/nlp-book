package fmseg2;

import java.util.ArrayList;

import com.lietu.automSeg.FSTSGraph;
import com.lietu.automSeg.SplitPoints;

public class FMMSegment {
	// 定义了一个Dictionary类型的全局变量，这里存放了整个我们需要的字典。
	static TernarySearchTrie dic = TernarySearchTrie.getInstance();
	static FSTSGraph fstSeg;

	static {
		try {
			fstSeg = new FSTSGraph();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> split(String sentence) {
		// 原子切分
		SplitPoints splitPoints = fstSeg.splitPoints(sentence);
		int senLen = sentence.length();// 首先计算出传入的这句话的字符长度
		ArrayList<String> words = new ArrayList<String>(senLen);

		int offset = 0;// 用来控制匹配的起始位置的变量
		while (true) {// 如果i小于此句话的长度就进入循环
			String word = dic
					.matchLong(sentence, offset, splitPoints.endPoints);// 正向最大长度匹配
			if (word != null)// 已经匹配上
			{
				// 如果这个词是词库中的那么就打印出来
				// System.out.print(word + " ");
				words.add(word);
				// 下次匹配点在这个词之后
				offset += word.length();
				if(offset >= senLen){
					break;
				}
			} else {// 如果在我们所处理的范围内一直都没有找到匹配上的词，就按原子切分
				int end = splitPoints.endPoints.nextSetBit(offset + 1);
				word = sentence.substring(offset, end);
				// 打印一个字
				// System.out.print("word " + word + " ");
				words.add(word);
				offset = splitPoints.startPoints.nextSetBit(offset + 1);// 下次匹配点在这个字符之后
				System.out.println("单字 offset  "+offset);
				if(offset<0){
					break;
				}
			}
		}

		return words;
	}

}
