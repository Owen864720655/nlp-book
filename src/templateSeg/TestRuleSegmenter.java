package templateSeg;

import java.util.ArrayDeque;
import java.util.Arrays;

import junit.framework.TestCase;

public class TestRuleSegmenter extends TestCase {

	public static void main(String[] args) {
		RuleSegmenter seg = new RuleSegmenter();
		String pattern = "<p><n><v><v><n><v>";
		seg.addRule(pattern);

		pattern = "<v><n><v>";
		seg.addRule(pattern);

		String text = "菲律宾副总统欲访华为毒贩求情遭中方拒绝";// "为毒贩求情";//总统亲自

		ArrayDeque<Integer> path = seg.split(text);

		// 输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(text.substring(start, end) + "/ ");
			start = end;
		}
	}

	public static void test1() {
		RuleSegmenter seg = new RuleSegmenter();
		String pattern = "从<n><f>下来";//"<p><n><f><v>";//从<n><f>下来
		//"<p><n><f><v>"; //从<n>上|下|来
		seg.addRule(pattern);

		String text = "从马上下来";

		ArrayDeque<Integer> path = seg.split(text);

		// 输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(text.substring(start, end) + "/ ");
			start = end;
		}
		//<n>支付系统
	}

	public static void test2() {
		RuleSegmenter seg = new RuleSegmenter();
		//String pattern = "<d><n><v><n>";
		//seg.addRule(pattern);

		String text = "从中学到知识";

		ArrayDeque<Integer> path = seg.split(text);

		// 输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(text.substring(start, end) + "/ ");
			start = end;
		}
	}

	public static void testBasic() {
		RuleSegmenter seg = new RuleSegmenter();
		String pattern = "<p><n><v>";
		seg.addRule(pattern);

		String text = "为毒贩求情"; //"亲自为毒贩求情";// 
		AdjList g = seg.getLattice(text);
		System.out.println("图 " + g);
		System.out.println("最佳后继节点 " + Arrays.toString(seg.sucNode));
		ArrayDeque<Integer> path = seg.bestPath(g);

		// 输出结果
		int start = 0;
		for (Integer end : path) {
			System.out.print(text.substring(start, end) + "/ ");
			start = end;
		}
	}

	public static void testSucNode() {
		RuleSegmenter seg = new RuleSegmenter();
		String pattern = "<p><n><v>";
		seg.addRule(pattern);

		String text = "亲自为毒贩求情";// "为毒贩求情";
		AdjList g = seg.getLattice(text);
		System.out.println("图 " + g);
		System.out.println("最佳后继节点 " + Arrays.toString(seg.sucNode));
	}

	public static void testcombineSuc() {
		RuleSegmenter seg = new RuleSegmenter();
		String pattern = "<p><n><v>";
		seg.addRule(pattern);

		String text = "亲自为毒贩求情";// "为毒贩求情";
		AdjList g = seg.combineSuc(text);
		System.out.println("图 " + g);
		System.out.println("最佳后继节点 " + Arrays.toString(seg.sucNode));
	}

}