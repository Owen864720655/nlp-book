package trigramSeg;

import java.util.ArrayDeque;
import java.util.List;

/**
 * @author luogang
 * @see
 * @2013-8-5
 */
public class TestSegmenter {

	public static void main(String[] args) {
		String sentence = "中国成立了";// "大学生活动中心";

		Segmenter seg = new Segmenter(sentence);
		ArrayDeque<Node> path = seg.split();
		for (Node n : path) {
			System.out.print(n + " ");
			System.out.print(' ');
		}
		System.out.println(' ');
		List<String> words = seg.bestPath(path);

		for (String word : words) {
			System.out.print(word + " / ");
		}
	}

}
