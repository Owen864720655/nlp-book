package nvgram;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * 词典树
 * @author luogang
 *
 */
public class TernarySearchTrie implements Iterable<String>{

	public final static class TSTNode {
		public String data;  //TODO

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

	public TernarySearchTrie() {
	}
	
	public void addWord(String word) {
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

	public String matchWord(String key, int offset, BitSet splitPoints) {
		String wordEntry = null; //词类型
		
		if (key == null || rootNode == null || "".equals(key)) {
			return null;
		}
		TSTNode currentNode = rootNode;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				return wordEntry;
			}
			int charComp = key.charAt(charIndex) - currentNode.spliter;

			if (charComp == 0) {
				if (currentNode.data != null) {
					if(splitPoints.get(charIndex)){
						wordEntry = currentNode.data; // 候选最长匹配词
					}
					//ret.end = charIndex;
				}
				charIndex++;
				if (charIndex == key.length()) {
					return wordEntry; // 已经匹配完
				}
				currentNode = currentNode.mid;
			} else if (charComp < 0) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
		}
	}
	
	public boolean contain(String word){
		TSTNode currentNode = rootNode;
		int charIndex = 0;
		boolean matchPrefix = false;
		while (true) {
			if (currentNode == null) {
				return false;
			}
			int charComp = word.charAt(charIndex) - currentNode.spliter;

			if (charComp == 0) {
				charIndex++;

				if (currentNode.data != null) { // 候选最长匹配词
					matchPrefix = true;
				}
				if (charIndex == word.length()) {
					return matchPrefix; // 已经匹配完
				}
				currentNode = currentNode.mid;
			} else if (charComp < 0) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
		}
		//return false;
	}

	public int nodeNums(){
		TSTNode currNode = rootNode;
		if (currNode == null)
			return 0;
		int count = 0;
		//System.out.println("count ");
		// 用于存放节点数据的队列
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode);

		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) {
			count++;
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();

			// 处理左子节点
			if (currNode.left != null) {
				queueNode.addLast(currNode.left);
			}

			// 处理中间子节点
			if (currNode.mid != null) {
				queueNode.addLast(currNode.mid);
			}

			// 处理右子节点
			if (currNode.right != null) {
				queueNode.addLast(currNode.right);
			}
		}
		
		return count;
	}

	private Set<Character> edges(TSTNode currNode) {
		if(currNode==null||currNode.mid==null)
			return null;
		HashSet<Character> ret = new HashSet<Character>();
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode.mid);
		
		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) {
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();
			ret.add(currNode.spliter);

			// 处理左子节点
			if (currNode.left != null) {
				queueNode.addLast(currNode.left);
			}

			// 处理中间子节点
			if (currNode.mid != null) {
				queueNode.addLast(currNode.mid);
			}

			// 处理右子节点
			if (currNode.right != null) {
				queueNode.addLast(currNode.right);
			}
		}
		
		return ret;
	}
	
	//找出一个节点所有的兄弟
	public static Set<TSTNode> brothers(TSTNode currNode){
		if(currNode==null)
			return null;

		HashSet<TSTNode> ret = new HashSet<TSTNode>();
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode);
		
		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) {
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();
			ret.add(currNode);

			// 处理左子节点
			if (currNode.left != null) {
				queueNode.addLast(currNode.left);
			}

			// 处理右子节点
			if (currNode.right != null) {
				queueNode.addLast(currNode.right);
			}
		}
		
		return ret;
	}
	
	public static Set<TSTNode> children(TSTNode parNode){
		TSTNode currNode = parNode.mid;
		if(currNode==null)
			return null;
		
		HashSet<TSTNode> ret = new HashSet<TSTNode>();
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode);
		
		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) {
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();
			ret.add(currNode);

			// 处理左子节点
			if (currNode.left != null) {
				queueNode.addLast(currNode.left);
			}

			// 处理右子节点
			if (currNode.right != null) {
				queueNode.addLast(currNode.right);
			}
		}
		
		if(ret.size()==0){
			return null;
		}
		
		return ret;
	}

	public TSTNode next(TSTNode stackValue, char edge) {
		TSTNode currentNode = stackValue.mid;
		while (true) {
			if (currentNode == null) {
				return null;
			}
			int charComp = edge - currentNode.spliter;

			if (charComp == 0) {
				return currentNode;
			} else if (charComp < 0) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
		}
	}
	
	private final class TrieIterator implements Iterator<String> { // 用于迭代的类
		Stack<Iterator<TSTNode>> stack = new Stack<Iterator<TSTNode>>(); // 记录当前遍历到的位置
		Stack<Iterator<String>> values = new Stack<Iterator<String>>(); //TODO
		//ArrayDeque<String> values = new ArrayDeque<String>();

		public TrieIterator(final TernarySearchTrie.TSTNode begin,final TernarySearchTrie t) { // 构造方法
			Set<TSTNode> set = brothers(begin);
			
			stack.add(set.iterator());
			
			ArrayList<String> list = new ArrayList<String>();
			//values.add(e);
		}

		public boolean hasNext() { // 是否还有更多的元素可以遍历
			if(stack.isEmpty())
				return false;

			//Iterator<TSTNode> itr = stack.peek();
			//if(itr.hasNext())
			//	return true;
			//return false;
			return true;
		}

		public String next() { // 向下遍历
			//System.out.println("call next");
			
			Iterator<TSTNode> itr = stack.peek();
			
			while(true){
				if(itr.hasNext()){
					TSTNode n = itr.next();
					Set<TSTNode> children = children(n);
					
					
					if(children!=null){
						stack.add(children.iterator());
					}
					
					if (n.isTerminal()) {//可结束状态
						//values.add(rhs.toString());
						System.out.println("return Terminal node");
						return String.valueOf(n.spliter);
					}
					else{
						itr = stack.peek();//children.iterator();
					}
				}
				else {
					stack.pop();
					if(stack.isEmpty())
						return null;
					itr = stack.peek();
				}
			}
		}
		
		public void remove() { // 从集合中删除当前元素
			throw new UnsupportedOperationException(); // 不支持删除当前元素这个操作
		}
	}

	public final static class StackValue {
		public TSTNode node;
		public StringBuilder rhs;

		public StackValue(TSTNode n) {
			node = n;
			rhs = new StringBuilder();
			rhs.append(n.spliter);
		}

		public StackValue(TSTNode n, StringBuilder r) {
			node = n;
			rhs = r;
		}

		public String toString() {
			return node.toString() + " rhs:" + rhs;
		}
	}

	@Override
	public Iterator<String> iterator() {
		return new TrieIterator(this.rootNode,this);
	}

}
