package fmseg2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;

public class TernarySearchTrie {
	private static TernarySearchTrie dic = null;

	public static TernarySearchTrie getInstance() {
		if (dic == null) {
			DicFileFactory dicFactory = new DicFileFactory();
			dic = dicFactory.create();
		}
		return dic;
	}

	public final class TSTNode {
		public String data;
		
		protected TSTNode left;
		protected TSTNode mid;
		protected TSTNode right;
		
		public char spliter;

		public TSTNode(char key) {
			this.spliter = key;
		}
		
		public TSTNode() {
			// TODO Auto-generated constructor stub
		}

		public String toString(){
			return "data  是"+data+"   spliter是"+spliter;
		}
	}
	
	public TSTNode rootNode;
	
	public TernarySearchTrie(String fileName) {
		try {
			InputStream file = new FileInputStream(new File(fileName)); 
			BufferedReader in = new BufferedReader(new InputStreamReader(file,"GBK"));
			String temstr = "";
			try {
				while ((temstr = in.readLine()) != null) {
					//System.out.println(temstr);
					TSTNode node = null;
					//StringTokenizer st = new StringTokenizer(temstr,"\t");
					String key = temstr;//st.nextToken();
					if (rootNode == null) {
						rootNode = new TSTNode(key.charAt(0));
					}
					int charIndex = 0;
					TSTNode currentNode = rootNode;
					while (true) {
						if (currentNode == null) {
							break;
						}
						int compa = (key.charAt(charIndex) - currentNode.spliter);
						if (compa == 0) {
							charIndex++;
							if (charIndex == key.length()) {
								node = currentNode;
								break;
							}
							currentNode = currentNode.mid;
						} else if (compa < 0) {
							currentNode = currentNode.left;
						} else {
							currentNode = currentNode.right;
						}
					}
					if (node == null) {
						currentNode=creatTSTNode(key);						
						currentNode.data = key;
						//System.out.println(currentNode);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public TernarySearchTrie() {
		// TODO Auto-generated constructor stub
	}

	// 根据该节点的获取与该节点同级的所有节点
	public ArrayList<TSTNode> getEqNodes(TSTNode tstNode) {
		ArrayList<TSTNode> childNodes = new ArrayList<TSTNode>();
		if (tstNode != null) {
			ArrayList<TSTNode> nodes = new ArrayList<TSTNode>();
			nodes.add(tstNode);//添加节点
			childNodes = nodes;
			while (true) {
				ArrayList<TSTNode> newlist = new ArrayList<TSTNode>();
				for (TSTNode node : nodes) {
					if (node.left != null) {
						newlist.add(node.left);// 添加左节点
					}
					if (node.right != null) {
						newlist.add(node.right);// 添加右节点
					}
				}
				nodes = newlist;
				if (newlist.size() == 0) {
					return childNodes;
				}
				childNodes.addAll(nodes);
			}
		}
		return childNodes;
	}
	
	//创建一个结点
	public TSTNode creatTSTNode(String key)throws NullPointerException,IllegalArgumentException {
		if(key==null){
			throw new NullPointerException("空指针异常");
		}
		int charIndex = 0;
		TSTNode currentNode = rootNode;
		if (rootNode == null) {
			rootNode = new TSTNode(key.charAt(0));
		}
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
	
	public String matchLong(String key,int offset, BitSet endPoints) {
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

				if(currentNode.data != null && endPoints.get(charIndex)){
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

	public void addWord(String key) {
		TSTNode node = null;
		if (rootNode == null) {
			rootNode = new TSTNode(key.charAt(0));
		}
		int charIndex = 0;
		TSTNode currentNode = rootNode;
		while (true) {
			if (currentNode == null) {
				break;
			}
			int compa = (key.charAt(charIndex) - currentNode.spliter);
			if (compa == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					node = currentNode;
					break;
				}
				currentNode = currentNode.mid;
			} else if (compa < 0) {
				currentNode = currentNode.left;
			} else {
				currentNode = currentNode.right;
			}
		}
		if (node == null) {
			currentNode=creatTSTNode(key);						
			currentNode.data = key;
			//System.out.println(currentNode);
		}
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

	static Charset charset = Charset.forName("utf-8"); // 得到字符集

	public void compileDic(File binFile) {
		try{
		String charSet = "UTF-8";
		FileOutputStream outFile = new FileOutputStream(binFile);
		BufferedOutputStream buffer = new BufferedOutputStream(outFile);
		DataOutputStream dataOut = new DataOutputStream(buffer);
		TSTNode currNode = rootNode;
		if (currNode == null){
			outFile.close();
			return;
		}

		int currNodeNo = 1; // 当前节点编号
		int maxNodeNo = currNodeNo;
		
		// 用于存放节点数据的队列
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode);

		// 用于存放节点编号的队列
		Deque<Integer> queueNodeIndex = new ArrayDeque<Integer>();
		queueNodeIndex.addFirst(currNodeNo);

		int nodeCount=nodeNums();
		dataOut.writeInt(nodeCount); //Trie树节点总数

		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) {
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();
			currNodeNo = queueNodeIndex.pollFirst();

			// 写入本节点的编号信息
			dataOut.writeInt(currNodeNo);

			// 处理左子节点
			int leftNodeNo = 0; // 当前节点的左孩子节点编号
			if (currNode.left != null) {
				maxNodeNo++;
				leftNodeNo = maxNodeNo;
				queueNode.addLast(currNode.left);
				queueNodeIndex.addLast(leftNodeNo);
			}

			// 写入左孩子节点的编号信息
			dataOut.writeInt(leftNodeNo);

			// 处理中间子节点
			int middleNodeNo = 0; // 当前节点的中间孩子节点编号
			if (currNode.mid != null) {
				maxNodeNo++;
				middleNodeNo = maxNodeNo;
				queueNode.addLast(currNode.mid);
				queueNodeIndex.addLast(middleNodeNo);
			}

			// 写入中孩子节点的编号信息
			dataOut.writeInt(middleNodeNo);

			// 处理右子节点
			int rightNodeNo = 0; // 当前节点的右孩子节点编号
			if (currNode.right != null) {
				maxNodeNo++;
				rightNodeNo = maxNodeNo;
				queueNode.addLast(currNode.right);
				queueNodeIndex.addLast(rightNodeNo);
			}

			// 写入右孩子节点的编号信息
			dataOut.writeInt(rightNodeNo);

			byte[] splitCharByte = String.valueOf(currNode.spliter).getBytes(charSet);

			// 记录字节数组的长度
			dataOut.writeInt(splitCharByte.length);

			// 写入splitChar字节数组
			dataOut.write(splitCharByte);
			
			if (currNode.data != null) {// 是结束节点,data域不为空
				//保存词
				//TODO
				//currNode.data.save(dataOut);
				CharBuffer cBuffer = CharBuffer.wrap(currNode.data);
				ByteBuffer bb = charset.encode(cBuffer);

				// 写入词的长度
				dataOut.writeInt(bb.limit());
				// 写入词的内容
				for (int i = 0; i < bb.limit(); ++i)
					dataOut.write(bb.get(i));
			} else { // 不是结束节点,data域为空
				dataOut.writeInt(0); // 写入字符串的长度
			}
		}
		//System.out.println("maxNodeNo:"+maxNodeNo);
		dataOut.close();
		outFile.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void loadBinaryDataFile(File binFile) {
		try{
		Charset charset = Charset.forName("utf-8"); // 得到字符集
		InputStream file_input = new FileInputStream(binFile);

		// 读取二进制文件
		BufferedInputStream buffer = new BufferedInputStream(file_input);
		DataInputStream data_in = new DataInputStream(buffer);

		// 获取节点数量
		int nodeCount = data_in.readInt();
		// System.out.print("nodeId:" + nodeId);

		TSTNode[] nodeList = new TSTNode[nodeCount + 1];// nodeId从1开始
		// 要预先创建出来所有的节点，因为当前节点要指向后续节点
		for (int i = 0; i < nodeList.length; i++) {
			nodeList[i] = new TSTNode();
		}

		for (int index = 1;index <= nodeCount;index++) {
			int currNodeIndex = data_in.readInt(); // 获得当前节点编号
			int leftNodeIndex = data_in.readInt(); // 获得当前节点左子节点编号
			int middleNodeIndex = data_in.readInt(); // 获得当前节点中子节点编号
			int rightNodeIndex = data_in.readInt(); // 获得当前节点右子节点编号
			
			TSTNode currNode = nodeList[currNodeIndex];
			// 获取splitchar值
			int length = data_in.readInt();
			byte[] bytebuff = new byte[length];
			data_in.read(bytebuff);
			currNode.spliter = charset.decode(
					ByteBuffer.wrap(bytebuff)).charAt(0);
			
			length = data_in.readInt();
			if(length>0){
				// 创建TSTNode节点
				//currNode.data = new WordEntry(data_in,length);
				bytebuff = new byte[length];
				data_in.read(bytebuff);
				currNode.data = new String(bytebuff, "UTF-8"); // 记录每一个词语
			}
			// 生成树节点之间的对应关系，左、中、右子树
			if (leftNodeIndex > 0) {
				currNode.left = nodeList[leftNodeIndex];// 建立引用关系，下次循环进行内容填充
			}

			if (middleNodeIndex > 0) {
				currNode.mid = nodeList[middleNodeIndex];
			}

			if (rightNodeIndex > 0) {
				currNode.right = nodeList[rightNodeIndex];
			}
		}

		data_in.close();
		buffer.close();
		file_input.close();

		rootNode = nodeList[1];
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
