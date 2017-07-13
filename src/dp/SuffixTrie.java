package dp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SuffixTrie {

	public final class TSTNode {
		public WordType data = null;

		protected TSTNode loNode;
		protected TSTNode eqNode;
		protected TSTNode hiNode;

		public char splitChar;

		public TSTNode(char key) {
			this.splitChar = key;
		}

		public String toString() {
			return "data:" + data + "   spliter:" + splitChar;
		}
	}

	public static class SuffixRet {
		public ArrayList<WordType> values;
		public int nextIndex; // 记录下次匹配的开始位置

		public SuffixRet(ArrayList<WordType> values, int nextIndex) {
			this.values = values;
			this.nextIndex = nextIndex;
		}
	}

	private static final String DIC_LOCATION = "SDIC.txt";

	private static SuffixTrie dic = null;

	public TSTNode root;

	public double wordCount;

	public static SuffixTrie getInstance() {
		if (dic == null)
			synchronized (DIC_LOCATION) {
				dic = new SuffixTrie(DIC_LOCATION);
			}
		return dic;
	}

	private SuffixTrie(String fileName) {
		try {
			InputStream file = new FileInputStream(new File(fileName));
			BufferedReader in = new BufferedReader(new InputStreamReader(file,"GBK"));
			
			String line;
			try {
				while ( ((line = in.readLine()) != null)) {
					if("".equals(line)) 
							continue;
					
					StringTokenizer st = new StringTokenizer(line,"\t");
					String word = st.nextToken();
					int freq;
					try {
						freq = Integer.parseInt(st.nextToken());
					} catch (NumberFormatException e) {
						freq = 1;
					}
					addWord(word,freq);
					wordCount += freq;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addWord(String key, int freq) {
		//System.out.println(key);
		if (key==null)
			throw new IllegalArgumentException();
		int charIndex = key.length() - 1;
		if (root == null)
			root = new TSTNode(key.charAt(charIndex));
		TSTNode currNode = root;
		while (true) {
			int compa = (key.charAt(charIndex) - currNode.splitChar);
			if (compa == 0) {
				if (charIndex <= 0) {
					currNode.data = new WordType(key, freq);
					break;
				}
				charIndex--;
				if (currNode.eqNode == null)
					currNode.eqNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.eqNode;
			} else if (compa < 0) {
				if (currNode.loNode == null)
					currNode.loNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.loNode;
			} else {
				if (currNode.hiNode == null)
					currNode.hiNode = new TSTNode(key.charAt(charIndex));
				currNode = currNode.hiNode;
			}
		}
	}

	public void matchAll(String sentence, int offset,ArrayList<WordType> ret) {
		ret.clear(); //清空返回数组中的词
		if ("".equals(sentence) || root == null || offset < 0)
			return;

		// 匹配英文
		WordType enWord = matchEnglish(sentence, offset);
		if (enWord != null) {
			ret.add(enWord);
			return ;
		}
		// 匹配数字
		WordType dgWord = matchDigit(sentence, offset);
		if (dgWord != null) {
			ret.add(dgWord);
			return ;
		}
		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {// 当前节点为空,说明词典中找不到对应的词,则将单个字符返回
				if(ret.size() == 0)
					ret.add(new WordType(sentence.substring(offset,offset+1), 1));
				return;
			}
			int charComp = sentence.charAt(charIndex) - currentNode.splitChar;

			if (charComp == 0) {
				if (currentNode.data != null) {
					ret.add(currentNode.data) ; // 候选最长匹配词
				}
				if (charIndex <= 0) {
					if(ret.size() == 0)
						ret.add(new WordType(sentence.substring(offset,offset+1), 1));
					return; // 已经匹配完
				}
				charIndex--; //继续往前找
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}

	private WordType matchEnglish(String sentence, int offset) {
		int index = offset;
		// 匹配英文
		for (; index >= 0; index--) {
			char c = sentence.charAt(index);
			if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
				break;
		}
		if (index != offset)
			return new WordType(sentence.substring(++index, offset + 1), 1);
		return null;
	}

	private WordType matchDigit(String sentence, int offset) {
		int index = offset;
		// 匹配数字
		for (; index >= 0; index--) {
			char c = sentence.charAt(index);
			if (!(c >= '0' && c <= '9'))
				break;
		}

		if (index != offset)
			return new WordType(sentence.substring(++index, offset + 1), 1);
		return null;
	}

	public static void main(String[] args) {
		SuffixTrie trie = SuffixTrie.getInstance();
		//System.out.println(trie.matchLong("大学生abc活动中心", 5).word);
		//SuffixRet suffixRet = trie.matchAll("有意见分歧123",7);
		//System.out.println(suffixRet.values);
		//System.out.println(suffixRet.nextIndex);
		
		String txt = "有意见分歧123";
		ArrayList<WordType> ret = new ArrayList<WordType>();
		for (int index = 1; index<txt.length();++index) {
			trie.matchAll(txt, index,ret);
			for (WordType word : ret) {
				double logProb = Math.log(word.freq) - Math.log(trie.wordCount);
				System.out.println(word.word+logProb);
			}
		}
	}
}
