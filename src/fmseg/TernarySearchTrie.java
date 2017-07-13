package fmseg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class TernarySearchTrie {
	public final class TSTNode {
		public Word data;

		protected TSTNode loNode;
		protected TSTNode eqNode;
		protected TSTNode hiNode;
		
		public char spliter;

		public TSTNode(char key) {
			this.spliter = key;
		}
		
		public String toString(){
			return "data  是"+data+"   spliter是"+spliter;
		}
	}
	
	public TSTNode rootNode;
	
	public static void main(String[] args) {
		TernarySearchTrie dic=new TernarySearchTrie("SDIC.txt");
		
		String sentence = "大学生活动中心";
		int offset = 0;
		System.out.println("offset:"+offset);
		String ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		
		offset = offset + ret.length();
		System.out.println("offset:"+offset);
		ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		
		offset = offset + ret.length();
		System.out.println("offset:"+offset);
		ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		
		offset = offset + ret.length();
		System.out.println("offset:"+offset);
		ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
	}

	public TernarySearchTrie(String fileName) {
		try {
			FileInputStream file = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(file, "GBK"));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line,"\t");
					String key = st.nextToken();
					WordType type = WordType.normal;
					if(isAllNumber(key)) //判断是否全数字组成的词
						type = WordType.Number;
					else if(isAllEnglish(key)) //判断是否全英文字母组成的词
						type = WordType.English;
					TSTNode currentNode=creatTSTNode(key);
					currentNode.data = new Word(key,type);
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
	
	//创建一个结点
	public TSTNode creatTSTNode(String key)throws NullPointerException,IllegalArgumentException {
		if(key==null){
			throw new NullPointerException("空指针异常");
		}
		int charIndex = 0;
		if (rootNode == null) {
			rootNode = new TSTNode(key.charAt(0));
		}
		TSTNode currentNode = rootNode;
		while (true) {
			int compa = (key.charAt(charIndex) - currentNode.spliter);
			if (compa == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;				
				}
				if (currentNode.eqNode == null) {
					currentNode.eqNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.eqNode;
			} else if (compa < 0) {
				if (currentNode.loNode == null) {
					currentNode.loNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.loNode;
			} else {
				if (currentNode.hiNode == null) {
					currentNode.hiNode = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.hiNode;
			}
		}				
	}
	
	public Word matchWord(String key,int offset) {
		if (key == null || rootNode == null || "".equals(key)) {
			return null;
		}
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		Word ret = null;
		
		//System.out.println("matchWord");
		while (true) {
			if (currentNode == null) {
				return ret;
			}
			int charComp = key.charAt(charIndex) - currentNode.spliter;
			
			if (charComp == 0) {
				charIndex++;
				//System.out.println("newOffset"+newOffset);
				if(currentNode.data != null){
					//newOffset = charIndex; //候选最长匹配词
					ret = currentNode.data;
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
	
	public String matchLong(String key,int offset){
		Word word = matchWord(key, offset);//尝试匹配词典中的词
		if(word == null){ //没有匹配上
			int newOffset = matchEnglish(key, offset); //继续尝试匹配英文串
			if(newOffset>offset)
				return key.substring(offset,newOffset);
			newOffset = matchNumber(key, offset); //继续尝试匹配数字串
			if(newOffset>offset)
				return key.substring(offset,newOffset); //返回匹配结果
			return null;
		}
		if(word.type == WordType.English){
			int oldOffset = offset+word.term.length();
			int newOffset = matchEnglish(key, oldOffset); //继续尝试匹配英文串
			if(newOffset>oldOffset)
				return key.substring(offset,newOffset); //返回匹配结果
		}
		else if(word.type == WordType.Number){
			int oldOffset = offset+word.term.length();
			int newOffset = matchNumber(key, oldOffset); //继续尝试匹配数字串
			if(newOffset>oldOffset)
				return key.substring(offset,newOffset); //返回匹配结果
		}
		return word.term;
	}

	//匹配数字
	private int matchNumber(String key, int offset) {
		return 1;
	}

	//匹配英文
	private int matchEnglish(String key, int offset) {
		return 1;
	}
	
	private boolean isAllNumber(String word) {
		for (int index = 0; index < word.length(); index++) {
			char c = word.charAt(index);
			if (!(c >= '0' && c <= '9')) // 匹配数字
				return false;
		}
		return true;
	}

	private boolean isAllEnglish(String word) {
		// 匹配英文
		for (int index = 0; index < word.length(); index++) {
			char c = word.charAt(index);
			if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'))
				return false;
		}
		return true;
	}

}