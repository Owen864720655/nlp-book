package segInterface;

import java.io.*;
import java.util.ArrayList;

public class FMMSegmentC{
	// 定义了一个Dictionary类型的全局变量，这里存放了整个我们需要的字典。
	TernarySearchTrieC dic = new TernarySearchTrieC("SDIC.txt");
	

	public FMMSegmentC() {
	}

	/*public String[] split(String sentence)// 传入一个字符串作为要处理的对象。
	{
		int senLen = sentence.length();// 首先计算出传入的这句话的字符长度
		int i = 0;// i是用来控制匹配的起始位置的变量
		ArrayList<String> result = new ArrayList<String>(senLen);

		while (i < senLen)// 如果i小于此句话的长度就进入循环
		{
			String word = dic.matchLong(sentence, i);// 正向最大长度匹配
			if (word != null)// 已经匹配上
			{
				// 下次匹配点在这个词之后
				i += word.length();
				// 如果这个词是词库中的那么就打印出来
				//System.out.print(word + " ");
				result.add(word);
			} else// 如果在我们所处理的范围内一直都没有找到匹配上的词，就按单字切分
			{
				word = sentence.substring(i, i + 1);
				// 打印一个字
				//System.out.print(word + " ");
				result.add(word);
				++i;// 下次匹配点在这个字符之后
			}
		}
		
		return result.toArray(new String[result.size()]);
	}*/
	
	public ArrayList<char[]> split(char[] sentence,int length) throws IOException// 传入一个字符串作为要处理的对象。
	{
		//int senLen = sentence.length();// 首先计算出传入的这句话的字符长度
		int i = 0;// i是用来控制匹配的起始位置的变量
		ArrayList<char[]> result = new ArrayList<char[]>();

		while (i < length)// 如果i小于此句话的长度就进入循环
		{
			char[] word = dic.matchLong(sentence,i,length);// 正向最大长度匹配
			if (word != null)// 已经匹配上
			{
				// 如果这个词是词库中的那么就打印出来
				//System.out.print(word + " ");
				result.add(word);
				i += word.length;
			}
			else
			{
				word = new char[]{sentence[i]};
				result.add(word);
				++i;// 下次匹配点在这个字符之后
			}
		}
		
		return result;
	}

	public static void main(String[] args) throws IOException {
		FMMSegmentC seg = new FMMSegmentC();
		// TernarySearchTrie dic=new TernarySearchTrie("SDIC.txt");

		String sentence = "大学生活动中心";
		ArrayList<char[]> ret = seg.split(sentence.toCharArray(),sentence.length());
		for(char[] word:ret)
		{
			//System.out.print(word + " ");
		    for (int i = 0; i < word.length; i++) {
		    	System.out.print(word[i]);
		    }
		    System.out.print(' ');
		}
		//long start = System.currentTimeMillis();
		//for(int i=0;i<1000;++i)
		//{
		//	seg.split(sentence);
		//}
		//long end = System.currentTimeMillis();
		//System.out.print("time: "+(end - start));
	}
}
