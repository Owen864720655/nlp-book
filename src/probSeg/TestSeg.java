package probSeg;

import java.io.IOException;
import java.util.Deque;

public class TestSeg {

	public static void main(String[] args) throws IOException {
		String sentence = "有意见分歧";
		//"中国成立了";// "大学生活动中心";
		Segmenter seg = new Segmenter();
		Deque<String> path = seg.split(sentence);

		//输出结果
		System.out.println("切分结果 ");
		for (String word : path) {
			System.out.print(word+" / ");
		}
	}
}
