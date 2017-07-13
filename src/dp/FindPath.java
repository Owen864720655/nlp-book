package dp;

import java.util.ArrayDeque;

public class FindPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sentence = "有意见分歧"; //待切分句子
		
		int[] prevNode = new int[6]; // 最佳前驱节点
		//最佳前驱节点中的数据要通过动态规划计算出来，先直接赋值模拟计算结果
		prevNode[1] = 0; //节点1的最佳前驱节点是0
		prevNode[2] = 0; //节点2的最佳前驱节点是0
		prevNode[3] = 1; //节点3的最佳前驱节点是1
		prevNode[4] = 3; //节点4的最佳前驱节点是3
		prevNode[5] = 3; //节点5的最佳前驱节点是3

		ArrayDeque<Integer> path = new ArrayDeque<Integer>(); //记录最佳切分路径
		// 通过回溯发现最佳切分路径
		for (int i = 5; i > 0; i = prevNode[i]){ // 从右向左找最佳前驱节点
		    path.push(i); //入栈
		}

		//输出结果
		int start = 0;
		for (int end : path){
		    System.out.println(sentence.substring(start, end) + "/ ");
		    start = end;
		}

	}

}
