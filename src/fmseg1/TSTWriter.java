package fmseg1;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import fmseg1.TernarySearchTrie.TSTNode;

public class TSTWriter {

	//生成词典文件
	public void compileDic(File file,TernarySearchTrie tst) throws IOException {
		String charSet = "UTF-8";
		FileOutputStream outFile = new FileOutputStream(file);
		BufferedOutputStream buffer = new BufferedOutputStream(outFile);
		DataOutputStream dataOut = new DataOutputStream(buffer);
		TSTNode currNode = tst.rootNode;
		if (currNode == null){
			outFile.close();
			return;
		}

	//	Long currNodeNo = 1; // 当前节点编号
	//	int maxNodeNo = currNodeNo;
		
		// 用于存放节点数据的队列
		Deque<TSTNode> queueNode = new ArrayDeque<TSTNode>();
		queueNode.addFirst(currNode);

		// 用于存放节点编号的队列
		Deque<Long> queueNodeIndex = new ArrayDeque<Long>();
		queueNodeIndex.addFirst(-1L);

		//int nodeCount=nodeNums();
		//dataOut.writeInt(nodeCount); //Trie树节点总数

		long fileOffset = 0;
		
		// 广度优先遍历所有树节点，将其加入至数组中
		while (!queueNode.isEmpty()) { 
			// 取出队列第一个节点
			currNode = queueNode.pollFirst();
			Long currPosition = queueNodeIndex.pollFirst();
			
			//在父节点的相应引用位置中设置本节点所在的位置
			//fseek
			// 写入本节点的编号信息
			//dataOut.writeInt(currNodeNo);

			byte[] splitCharByte = String.valueOf(currNode.spliter).getBytes(charSet);

			// 记录字节数组的长度
			dataOut.writeInt(splitCharByte.length);
			fileOffset +=4;
			
			// 写入splitChar字节数组
			dataOut.write(splitCharByte);
			fileOffset +=4;
			if (currNode.data != null) {// 是结束节点,data域不为空
				//保存词性
				//currNode.data.save(dataOut);
			} else { // 不是结束节点,data域为空
				dataOut.writeInt(0); // 写入字符串的长度
			}

			// 处理左子节点
			int leftNodeNo = 0; // 当前节点的左孩子节点编号
			if (currNode.left != null) {
				//maxNodeNo++;
				//leftNodeNo = maxNodeNo;
				queueNode.addLast(currNode.left);
				queueNodeIndex.addLast(fileOffset);
			}

			// 写入左孩子节点的编号信息
			dataOut.writeInt(leftNodeNo);

			// 处理中间子节点
			int middleNodeNo = 0; // 当前节点的中间孩子节点编号
			if (currNode.mid != null) {
				//maxNodeNo++;
				//middleNodeNo = maxNodeNo;
				queueNode.addLast(currNode.mid);
				//queueNodeIndex.addLast(middleNodeNo);
			}

			// 写入中孩子节点的编号信息
			dataOut.writeInt(middleNodeNo);

			// 处理右子节点
			int rightNodeNo = 0; // 当前节点的右孩子节点编号
			if (currNode.right != null) {
				//maxNodeNo++;
				//rightNodeNo = maxNodeNo;
				queueNode.addLast(currNode.right);
				//queueNodeIndex.addLast(rightNodeNo);
			}

			// 写入右孩子节点的编号信息
			dataOut.writeInt(rightNodeNo);
		}
		//System.out.println("maxNodeNo:"+maxNodeNo);
		dataOut.close();
		outFile.close();	
	}
}
