package templateSeg;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;

/**
 * 词性标注 Trie tree
 * TODO 
 * <n>+v<n>+

n n v n

<n>+v<n>+

<n>+  part1 

v part2

<n>+ part3

rule1  part1
<n>+  part1 
 * @author luogang
 * 
 */
public class Trie {
	
	//private static Logger logger = Logger.getLogger(Trie.class);
	
	public TrieNode rootNode = new TrieNode(); // 根节点
	int nodeNum=1;

	// 放入键/值对
	public void addRule(ArrayList<String> rhs) {
		//System.out.println("To add Trie  left: "+ruleName +" right: "+rhs);
		TrieNode currNode = rootNode; // 当前节点
		for (int i = 0; i < rhs.size(); ++i) { // 从前往后找键中的类型
			String c = rhs.get(i);
			Map<String, TrieNode> map = currNode.getChildren();
			currNode = map.get(c); // 向下移动当前节点
			if (currNode == null) {
				currNode = new TrieNode();
				nodeNum++;
				map.put(c, currNode);
			}
		}
		currNode.ruleName=true; // 设置成可以结束的节点
	}
	
	// 根据键查找对应的值
	public boolean find(ArrayList<String> key) {
		TrieNode currNode = rootNode; // 当前节点
		for (int i = 0; i < key.size(); ++i) { // 从前往后找
			String c = key.get(i);
			currNode = currNode.getChildren().get(c);// 向下移动当前节点
			if (currNode == null) {
				return false;
			}
		}
		if (currNode.isTerminal()) {
			return true;
		}
		return false;
	}

	public Set<String> edges(TrieNode currNode) {
		return currNode.getChildren().keySet();
	}
	
	public Trie() {
	}

	public TrieNode next(TrieNode currNode, String edge) {
		return currNode.getChildren().get(edge);
	}
}
