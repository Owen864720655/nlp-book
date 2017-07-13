package trigramSeg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TernarySearchTrie {

	private static TernarySearchTrie dic = null;
	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static TernarySearchTrie getInstance()
	{
		if (dic == null)
		{
			dic = new TernarySearchTrie("UnigramDic.txt");
		}
		return dic;
	}
	
	public final class TSTNode {
		public WordEntry data=null;
		
		protected TSTNode loNode;
		protected TSTNode eqNode;
		protected TSTNode hiNode;
		
		public char splitChar;

		public TSTNode(char key) {
			this.splitChar = key;
		}
		
		public String toString(){
			return "data  是"+data+"   spliter是"+splitChar;
		}
	}
	
	public TSTNode rootNode;
	public long n = 0;//语料库中的词的总数
	public static void main(String[] args) {
		TernarySearchTrie dic=new TernarySearchTrie("SDIC.txt");
		
		String sentence = "大学生活动中心";
		int offset = 0;
		WordEntry ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		//System.out.println(dic.rootNode.getPath());
	}

	public TernarySearchTrie(String fileName) {
		try {
			//FileReader filereadnew = new FileReader(fileName);
			//BufferedReader read = new BufferedReader(filereadnew);
			InputStream file = new FileInputStream(new File(fileName));  //打开输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(file,"GBK"));
			String line;
			try {
				while ( ((line = in.readLine()) != null)) {
					if("".equals(line)) 
							continue;
					
					StringTokenizer st = new StringTokenizer(line,"\t");
					String key = st.nextToken();
					int freq;
					try {
						freq = Integer.parseInt(st.nextToken());
					} catch (NumberFormatException e) {
						freq = 1;
					}
					addWord(key,freq);
					n = n + freq;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addWord(String key,int freq)
	{
		if (rootNode == null) {
			rootNode = new TSTNode(key.charAt(0));
		}
		
		int charIndex = 0;
		TSTNode currentNode = rootNode;
		while (true) {
			int compa = (key.charAt(charIndex) - currentNode.splitChar);
			if (compa == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					currentNode.data = new WordEntry(key, freq);
					break;
				}
				if(currentNode.eqNode == null){
					currentNode.eqNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.eqNode;
			} else if (compa < 0) {
				if(currentNode.loNode == null){
					currentNode.loNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.loNode;
			} else {
				if(currentNode.hiNode == null){
					currentNode.hiNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.hiNode;
			}
		}
	}
	
	public WordEntry matchLong(String key,int offset) {
		WordEntry ret = null;
		if (key == null || rootNode == null || "".equals(key)) {
			return ret;
		}
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				return ret;
			}
			int charComp = key.charAt(charIndex) - currentNode.splitChar;
			
			if (charComp == 0) {
				charIndex++;

				if(currentNode.data != null){
					ret = currentNode.data; //候选最长匹配词
				}
				if (charIndex == key.length()) {
					return ret; //已经匹配完
				}
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}
	
	public boolean matchEnglish(int start, String sentence, PrefixRet prefix) {
		int i = start;
		for (; i < sentence.length();) {
			char c = sentence.charAt(i);
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
				++i;
			} else {
				break;
			}
		}

		if (i != start) {
			prefix.end = i;
			prefix.values = new ArrayList<WordEntry>();
			prefix.values.add(new WordEntry(sentence.substring(start, i),10));	// 为什么赋值为10
			return true;
		}
		return false;
	}

	public boolean matchNum(int start, String sentence, PrefixRet prefix) {
		int i = start;
		for (; i < sentence.length();) {
			char c = sentence.charAt(i);
			if (c >= '0' && c <= '9') {
				++i;
			} else {
				break;
			}
		}

		if (i != start) {
			prefix.end = i;
			prefix.values = new ArrayList<WordEntry>();
			prefix.values.add(new WordEntry(sentence.substring(start, i),10));
			return true;
		}
		return false;
	}

	public static class PrefixRet {
		public ArrayList<WordEntry> values;
		public int end; //记录下次匹配的开始位置
	}
	
	//如果匹配上则返回true，否则返回false
	public boolean getMatch(String sentence, int offset, PrefixRet prefix) {
		if (sentence == null || rootNode == null || "".equals(sentence)) {
			return false;
		}
		boolean match = matchEnglish(offset, sentence, prefix);
		if (match) {
			return true;
		}

		match = matchNum(offset, sentence, prefix);
		if (match) {
			return true;
		}
		prefix.end = offset+1;//下次匹配的开始位置
		ArrayList<WordEntry> ret = new ArrayList<WordEntry>();//指定开始位置匹配的词
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				prefix.values = ret;
				if (ret.size() > 0)
				{
					return true;
				}
				return false;
			}
			int charComp = sentence.charAt(charIndex) - currentNode.splitChar;
			if (charComp == 0) {
				charIndex++;
				if (currentNode.data != null) {
					System.out.println("data = " + currentNode.data) ;
					ret.add(currentNode.data);
				}
				if (charIndex == sentence.length()) {
					prefix.values = ret;
					if (ret.size() > 0)
					{
						return true;
					}
					return false;
				}
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}

}
