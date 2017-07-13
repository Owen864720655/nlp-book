package segInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.lietu.fst.FST;
import com.lietu.fst.FSTFactory;
import com.lietu.fst.Token;

public class Segmenter2 {
	public static final class TSTNode {
		protected TSTNode left;
		protected TSTNode mid;
		protected TSTNode right;

		public char splitChar;
		protected String nodeValue; // 是否可以结束

		public TSTNode(char key) {
			// System.out.println("create node:"+key);
			this.splitChar = key;
		}

		public String toString() {
			return "spliter是" + splitChar;
		}
	}

	private static TSTNode root;

	static {//加载词典
		String fileName = "SDIC.txt";//"WordList.txt";
		try {
			//FileReader filereadnew = new FileReader(fileName);
			//BufferedReader read = new BufferedReader(filereadnew);
			FileInputStream file = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(file, "GBK"));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(line, "\t");
					String key = st.nextToken();

					TSTNode currentNode = createNode(key);
					currentNode.nodeValue = key;
					// System.out.println(currentNode);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				in.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 创建给定词相关的节点并返回对应的叶结点
	public static TSTNode createNode(String key) {
		int charIndex = 0; // 当前要比较的字符在查询词中的位置
		char currentChar = key.charAt(charIndex); // 当前要比较的字符
		if (root == null) {
			root = new TSTNode(currentChar);
		}
		TSTNode currentNode = root;
		while (true) {
			// 比较词的当前字符与节点的当前字符
			int compa = currentChar - currentNode.splitChar;
			if (compa == 0) { // 词中的字符与节点中的字符相等
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}
				currentChar = key.charAt(charIndex);
				if (currentNode.mid == null) {
					currentNode.mid = new TSTNode(currentChar);
				}
				currentNode = currentNode.mid; // 向下找
			} else if (compa < 0) { // 词中的字符小于节点中的字符
				if (currentNode.left == null) {
					currentNode.left = new TSTNode(currentChar);
				}
				currentNode = currentNode.left; // 向左找
			} else { // 词中的字符大于节点中的字符
				if (currentNode.right == null) {
					currentNode.right = new TSTNode(currentChar);
				}
				currentNode = currentNode.right; // 向右找
			}
		}
	}

	String text = null; //切分文本
	int offset; //已经处理到的位置
	FST fst = null;

	public Segmenter2(String text) throws Exception {
		this.text = text;
		offset = 0;
		fst = FSTFactory.createAll();
	}

	public String nextWord() { //得到下一个词
		String word = null;
		if (text == null || root == null) {
			return word;
		}
		
		//首先用FST找未登录串,例如日期
		Token t = fst.matchLong(text, offset);
		if(t!=null){
			System.out.println("token:"+t);
			offset = t.end; //下次匹配点
			return t.termText; //返回fst匹配出来的词
		}

		if (offset >= text.length()) //已经处理完毕-+
			return word;
		TSTNode currentNode = root; //当前节点
		int charIndex = offset; //当前位置
		while (true) {
			if (currentNode == null) {//已经匹配完毕
				if(word==null){ //没有匹配上，则按单字切分
					word = text.substring(offset,offset+1);
					offset++;
				}
				return word;
			}
			int charComp = text.charAt(charIndex) - currentNode.splitChar;

			if (charComp == 0) {
				charIndex++;

				if (currentNode.nodeValue != null) { //可以结束
					word = currentNode.nodeValue; // 候选最长匹配词
					offset = charIndex;
				}
				if (charIndex == text.length()) {
					return word; // 已经匹配完
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
