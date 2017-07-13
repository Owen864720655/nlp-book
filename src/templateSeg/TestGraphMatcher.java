package templateSeg;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestGraphMatcher extends TestCase {

	public static void main(String[] args) {
		String sentence = "一把圆珠笔";
		RuleSegmenter seg = new RuleSegmenter();
		AdjList g = seg.getLattice(sentence);//得到切分词图
		System.out.println("图 " + g);
		Trie rule = new Trie(); //规则Trie树
		ArrayList<String> rhs = new ArrayList<String>();
		rhs.add("m");
		rhs.add("q");
		rhs.add("n");
		rule.addRule(rhs); //增加规则

		int offset = 0;

		// 匹配规则Trie树
		GraphMatcher.MatchValue match = GraphMatcher.intersect(g, offset, rule);
		System.out.println("匹配结果:" + match);
	}
	
	public static void test1(){
		//p n f v
		//转移概率 作为权重
		String sentence = "从马上下来"; 
		
		RuleSegmenter seg = new RuleSegmenter();
		AdjList g = seg.getLattice(sentence);//得到切分词图
		System.out.println("图 " + g);
		//比较两个  p(w1)*p(w2)*p(w3)*p(w4)  的值
		Trie rule = new Trie(); //规则Trie树
		ArrayList<String> rhs = new ArrayList<String>();
		rhs.add("p");
		rhs.add("n");
		rhs.add("f");
		rhs.add("v");
		rule.addRule(rhs); //增加规则
		
		int offset = 0;

		// 匹配规则Trie树
		GraphMatcher.MatchValue match = GraphMatcher.intersect(g, offset, rule);
		System.out.println("匹配结果:" + match);
	}

}
