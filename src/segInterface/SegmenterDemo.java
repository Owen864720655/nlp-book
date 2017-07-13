package segInterface;

public class SegmenterDemo {

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
			return "Node spliter:" + splitChar+" 可以结束?"+(nodeValue!=null);
		}
	}

	private static TSTNode root;

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
	
	public static TSTNode addWord(){
		return null;
	}

	String text = null; //切分文本
	int offset; //已经处理到的位置
	
	public SegmenterDemo(String text) throws Exception {
		this.text = text;
		offset = 0;
	}

	public String nextWord() { //得到下一个词
		System.out.println("offset "+offset);
		String word = null; //候选词
		if (text == null || root == null) {
			return word;
		}
		
		if (offset >= text.length()) //已经处理完毕-+
			return null;
		TSTNode currentNode = root; //当前节点
		int charIndex = offset; //当前位置
		while (true) {
			System.out.println("当前字符 "+charIndex+" :"+text.charAt(charIndex)+" 当前节点:"+currentNode);
			
			if (currentNode == null) {//已经匹配完毕
				if(word==null){ //没有匹配上词典上的任何词，则按单字切分
					word = text.substring(offset,offset+1);
					offset++;
				}
				return word;
			}
			//待切分文本中的当前字符和当前节点中的字符比较
			int charComp = text.charAt(charIndex) - currentNode.splitChar;

			if (charComp == 0) {
				charIndex++;

				if (currentNode.nodeValue != null) { //可以结束节点
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
