package segInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class TernarySearchTrieC {

	public final class TSTNode {
		public char[] data=null;

		protected TSTNode paNode;
		protected TSTNode loNode;
		protected TSTNode eqNode;
		protected TSTNode hiNode;
		
		public char spliter;

		public TSTNode(char key, TSTNode parent) {
			this.spliter = key;
			paNode = parent;
		}
		
		public String toString(){
			return "data  是"+String.valueOf(data)+"   spliter是"+spliter;
		}

		public String getPath()
		{
			StringBuilder sb = new StringBuilder();
			sb.append(spliter);
			TSTNode parent = paNode;
			while (parent!=null)
			{
				//System.out.println("splitchar:"+parent.spliter);
				sb.append(parent.spliter);
				parent = parent.paNode;
			}
			return sb.reverse().toString();
		}
	}
	
	private static TernarySearchTrieC dic = null;
	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static TernarySearchTrieC getInstance()
	{
		if (dic == null)
		{
			dic = new TernarySearchTrieC("SDIC.txt");
		}
		return dic;
	}
	
	public TSTNode rootNode;
	
	public static void main(String[] args) {
		TernarySearchTrieC dic=new TernarySearchTrieC("SDIC.txt");
		
		String sentence = "大学生活动中心";
		
		int offset = 0;
		char[] ret = dic.matchLong(sentence.toCharArray(),offset,sentence.length());
		System.out.print(sentence+" match:");
	    for (int i = 0; i < ret.length; i++) {
	    	System.out.print(ret[i]);
	    }
	}

	public TernarySearchTrieC(String fileName) {
		try {
			FileReader filereadnew = new FileReader(fileName);
			BufferedReader read = new BufferedReader(filereadnew);
			String line;
			try {
				while ((line = read.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line,"\t");
					//String[] tem = temstr.split("\t");
					String word = st.nextToken();
					char[] key = word.toCharArray();
					int keyLength = word.length();
					//int cost = Integer.parseInt(tem[1]);
					
					TSTNode currentNode=creatTSTNode(key,keyLength);						
					currentNode.data = key;
					//System.out.println(currentNode);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//创建一个结点
	public TSTNode creatTSTNode(char[] key,int keyLength)throws NullPointerException,IllegalArgumentException {
		if(key==null){
			throw new NullPointerException("空指针异常");
		}
		if (rootNode == null) {
			rootNode = new TSTNode(key[0], null);
		}
		int charIndex = 0;
		TSTNode currentNode = rootNode;
		while (true) {
			int compa = (key[charIndex] - currentNode.spliter);
			if (compa == 0) {
				charIndex++;
				if (charIndex == keyLength) {
					return currentNode;				
				}
				if (currentNode.eqNode == null) {
					currentNode.eqNode = new TSTNode(key[charIndex],
							currentNode);
				}
				currentNode = currentNode.eqNode;
			} else if (compa < 0) {
				if (currentNode.loNode == null) {
					currentNode.loNode = new TSTNode(key[charIndex],
							currentNode);
				}
				currentNode = currentNode.loNode;
			} else {
				if (currentNode.hiNode == null) {
					currentNode.hiNode = new TSTNode(key[charIndex],
							currentNode);
				}
				currentNode = currentNode.hiNode;
			}
		}				
	}
	
	public int matchNum (int start, char[] key, int length)
	{
		int i = start;
		while(i<length)
		{
			char c = key[i];
			if( c>='0'&&c<='9' )
			{
				++i;
			}
			else
			{
				break;
			}
		}
		
		return i;
	}

	/**
	 * 
	 * @param start 开始位置
	 * @param key 关键词
	 * @param length 长度
	 * @return 返回第一个不是英文的位置
	 */
	public int matchEnglish (int start, char[] key, int length)
	{
		int i = start;
		while(i<length)
		{
			char c = key[i];
			if( c>='a'&&c<='z' || c>='A'&&c<='Z' )
			{
				++i;
			}
			else
			{
				break;
			}
		}		
		
		return i;
	}

	public char[] matchLong(char[] key,int offset,int length) {
		char[] ret = null;
		
		int numEnd = matchNum(offset,key,length);
		if(numEnd>offset)
		{
			//number
			char[] num = new char[numEnd - offset];
			System.arraycopy(key, offset, num, 0, num.length);
			return num;
		}

		int englishEnd = matchEnglish(offset,key,length);
		if(englishEnd>offset)
		{
			//english
			char[] english = new char[englishEnd - offset];
			System.arraycopy(key, offset, english, 0, english.length);
			return english;
		}
		
		//if (key == null || rootNode == null || "".equals(key)) {
		//	return ret;
		//}
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				return ret;
			}
			int charComp = key[charIndex] - currentNode.spliter;
			
			if (charComp == 0) {
				charIndex++;

				if(currentNode.data != null){
					ret = currentNode.data; //候选最长匹配词
				}
				if (charIndex == length) {
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
}
