package probSeg2;

import java.util.ArrayList;

public class TestSegResult {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sentence = "有意见分歧"; // 待切分句子

		int[] sucNode = new int[6]; // 最佳后继节点
		// 从后往前得到值
		sucNode[5] = 0;
		sucNode[4] = 5;
		sucNode[3] = 5;
		sucNode[2] = 3;
		sucNode[1] = 3;
		sucNode[0] = 1;

		ArrayList<Integer> path = new ArrayList<Integer>();
		// 通过回溯发现最佳切分路径
		// 从前往后找最佳后继节点
		for (int i = sucNode[0]; i < sentence.length(); i = sucNode[i]) {
			path.add(i);
		}
		path.add(sentence.length());
		// 输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(sentence.substring(start, end) + "/ ");
			start = end;
		}

	}

}
