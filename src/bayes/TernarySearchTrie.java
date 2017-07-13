package bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TernarySearchTrie {

	private static TernarySearchTrie dic = null;
	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static TernarySearchTrie getInstance()
	{
		if (dic == null)
		{
			dic = new TernarySearchTrie("SDIC.txt");
		}
		return dic;
	}
	
	public final class TSTNode {
		public String data;
		
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
		TernarySearchTrie dic=new TernarySearchTrie("WordList.txt");
		
		String sentence = "大学生活动中心";
		int offset = 0;
		String ret = dic.matchLong(sentence,offset);
		System.out.println(sentence+" match:"+ret);
		//System.out.println(dic.rootNode.getPath());
	}

	public TernarySearchTrie(String fileName) {
		try {
			//FileReader filereadnew = new FileReader(str);
			//BufferedReader read = new BufferedReader(filereadnew);
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
							currentNode = currentNode.eqNode;
						} else if (compa < 0) {
							currentNode = currentNode.loNode;
						} else {
							currentNode = currentNode.hiNode;
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
					if (node.loNode != null) {
						newlist.add(node.loNode);// 添加左节点
					}
					if (node.hiNode != null) {
						newlist.add(node.hiNode);// 添加右节点
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
}
