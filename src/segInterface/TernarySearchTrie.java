package segInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class TernarySearchTrie {
	public final class TSTNode {
		public String data;

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
			return "data  是"+data+"   spliter是"+spliter;
		}
	}
	
	public TSTNode rootNode;
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		TernarySearchTrie dic=new TernarySearchTrie("WordList.txt");
		
		String sentence = "大学生活动中心";
		int offset = 0;
		String ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		System.out.print(dic.numNodes());
	}

	public TernarySearchTrie(String fileName) throws UnsupportedEncodingException {
		try {
			//FileReader filereadnew = new FileReader(fileName);
			//BufferedReader read = new BufferedReader(filereadnew);
			FileInputStream file = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(file, "GBK"));
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
			rootNode = new TSTNode(key.charAt(0), null);
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
					currentNode.eqNode = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.eqNode;
			} else if (compa < 0) {
				if (currentNode.loNode == null) {
					currentNode.loNode = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.loNode;
			} else {
				if (currentNode.hiNode == null) {
					currentNode.hiNode = new TSTNode(key.charAt(charIndex),
							currentNode);
				}
				currentNode = currentNode.hiNode;
			}
		}				
	}
	
	public String matchLong(String key,int offset) {
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
				currentNode = currentNode.eqNode;
			} else if (charComp < 0) {
				currentNode = currentNode.loNode;
			} else {
				currentNode = currentNode.hiNode;
			}
		}
	}
	

	/**
	 *  Returns the total number of nodes in the subtrie below and including the 
	 *  starting Node. The method counts nodes whether or not they have data.
	 *
	 *@param  startingNode  The top node of the subtrie. The node that defines the subtrie.
	 *@return               The total number of nodes in the subtrie.
	 */
	protected int numNodes() {
		return recursiveNodeCalculator(rootNode, 0);
	}

	/**
	 *  Recursivelly visists each node to calculate the number of nodes.
	 *
	 *@param  currentNode  The current node.
	 *@param  checkData    If true we check the data to be different of <code>null</code>.
	 *@param  numNodes2    The number of nodes so far.
	 *@return              The number of nodes accounted.
	 */
	private int recursiveNodeCalculator(
		TSTNode currentNode,
		int numNodes2) {
		if (currentNode == null) {
			return numNodes2;
		}
		int numNodes =
			recursiveNodeCalculator(
				currentNode.loNode,
				numNodes2);
		numNodes =
			recursiveNodeCalculator(
				currentNode.eqNode,
				numNodes);
		numNodes =
			recursiveNodeCalculator(
				currentNode.hiNode,
				numNodes);
		numNodes++;
		return numNodes;
	}
}
