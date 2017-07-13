package fmseg1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class TernarySearchTrie {
	public final class TSTNode {
		public String data;

		protected TSTNode left;
		protected TSTNode mid;
		protected TSTNode right;
		
		public char spliter;

		public TSTNode(char key) {
			this.spliter = key;
		}
		
		public String toString(){
			return "data  是"+data+"   spliter是"+spliter;
		}
	}
	
	public TSTNode rootNode;
	
	public static void main(String[] args) throws Exception {
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

	public TernarySearchTrie(String fileName) throws UnsupportedEncodingException {
		try {
			FileInputStream file = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(file,
					"GBK"));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line,"\t");
					String key = st.nextToken();
					
					TSTNode currentNode=creatTSTNode(key);						
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
				if (currentNode.mid == null) {
					currentNode.mid = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.mid;
			} else if (compa < 0) {
				if (currentNode.left == null) {
					currentNode.left = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.left;
			} else {
				if (currentNode.right == null) {
					currentNode.right = new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.right;
			}
		}				
	}
	
	public String matchLong(String key,int offset) {
		if (offset >= key.length()) //已经处理完毕-+
			return null;
		
		String ret = null;
		if (key == null || rootNode == null || "".equals(key)) {
			return ret;
		}
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				return ret;
			}
			int charComp = key.charAt(charIndex) - currentNode.spliter;
			
			if (charComp == 0) {
				charIndex++;

				if(currentNode.data != null){
					ret = currentNode.data; //候选最长匹配词
				}
				if (charIndex == key.length()) {
					return ret; //已经匹配完
				}
				currentNode = currentNode.mid;
			} else if (charComp < 0) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
		}
	}
}