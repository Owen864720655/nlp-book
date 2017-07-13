package com.lietu.split;

public class TernarySearchTrie {

	public final static class TSTNode {
		public String data;

		protected TSTNode left;
		protected TSTNode mid;
		protected TSTNode right;

		public char spliter;

		public TSTNode(char key) {
			this.spliter = key;
		}

		public TSTNode() {
		}

		public String toString() {
			return "data: " + data + "   spliter:" + spliter;
		}

		public void addWord(String word) {
			if(data==null){
				data = word;
			}
		}

		public boolean isTerminal() {
			return (data!=null);
		}
	}

	public TSTNode rootNode;

	public void add(String word) {
		TSTNode currentNode = creatTSTNode(word);
		currentNode.addWord(word);
	}

	// 创建一个结点
	public TSTNode creatTSTNode(String key) throws NullPointerException,
			IllegalArgumentException {
		if (key == null) {
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

	public static class MatchResult {
		public int position; // 位置
		public String strFound; // 匹配到的符号

		public MatchResult(int p, String f) {
			position = p;
			strFound = f;
		}

		@Override
		public String toString() {
			return "MatchResult [position=" + position + ", strFound="
					+ strFound + "]";
		}
	}

	public String matchLong(String sentence, int offset) {
		String ret = null;
		//int pos = offset;
		if (sentence == null || rootNode == null || "".equals(sentence)) {
			return null;
		}
		//System.out.println("offset "+offset) ;
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				return ret;
			}
			int charComp = sentence.charAt(charIndex) - currentNode.spliter;
			if (charComp == 0) {
				if (currentNode.data != null) {
					ret = currentNode.data; //候选最长匹配词
					//pos = charIndex;
				}
				//System.out.println("charIndex "+charIndex) ;
				charIndex++; // 继续往前找
				if (charIndex >= sentence.length()) {
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
