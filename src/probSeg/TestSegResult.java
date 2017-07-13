package probSeg;

import java.util.ArrayDeque;

public class TestSegResult {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sentence = "有意见分歧"; //待切分句子

		int[] prevNode = new int[6]; // 最佳前驱节点
		prevNode[1] = 0;
		prevNode[2] = 0;
		prevNode[3] = 1;
		prevNode[4] = 3;
		prevNode[5] = 3;

		ArrayDeque<Integer> path = new ArrayDeque<Integer>();
		// 通过回溯发现最佳切分路径
		for (int i = 5; i > 0; i = prevNode[i]) { // 从右向左找最佳前驱节点
			path.addFirst(i);
		}

		//输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(sentence.substring(start, end) + "/ ");
			start = end;
		}
	}

}
