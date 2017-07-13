package templateSeg;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 解析规则
 * @author luogang
 *
 */
public class RuleParser {
	public final static String PositionSplit = "p-"; //保证规则部件名称的唯一性
	static int seq = 0; // 用来保证规则名称唯一性
	
	public static Rule parse(String pattern){
		String uniqueName = String.valueOf(seq); // 规则唯一的名字
		Rule rightRule = RuleParser.parse(uniqueName, pattern);
		seq++;
		return rightRule;
	}
	
	/**
	 * 
	 * @param ruleName
	 * @param pattern 例如: 为<A>求情
	 * @return
	 */
	public static Rule parse(String ruleName,String pattern){
		if (pattern == null)
			return null;
		//right = right.trim();
		
		Rule rule = new Rule();
		
		int senLen = pattern.length();// 首先计算出传入的这句话的字符长度
		int i = 0;// i是用来控制匹配的起始位置的变量

		while (i < senLen)// 如果i小于此句话的长度就进入循环
		{
			//i = matchSpace(right, i); // 跳过空格
			//词的类型
			String semName = ruleName + PositionSplit + i; // 根据规则加上编号生成语义类的名字
			int offset = matchOptionWords(pattern, i,  semName,rule);
			if (offset > i) {
				i = offset + 1;
				rule.typeSeq.add(semName);
				continue;
			}
			offset = matchNormalWord(pattern, i);// 普通的词
			if (offset > i)// 已经匹配上
			{
				// 下次匹配点在这个词之后
				int start = i;
				int end = offset;
				String word = pattern.substring(start, end);
				//logger.debug("word: " + word);
				rule.addWord(word, semName);
				i = offset;
				rule.typeSeq.add(semName);
				continue;
			}
			StringBuilder content = new StringBuilder();
			offset = matchRuleName(pattern, i, content);
			if (offset > i)// 匹配上规则
			{
				// 下次匹配点在这个词之后
				String rightRuleName = content.toString();
				//logger.debug("规则名: " + rightRuleName);
				i = offset + 1;
				rule.typeSeq.add(rightRuleName);
				continue;
			}
		}

		return rule;
	}

	public static int matchRuleName(String sentence, int offset,
			StringBuilder bracketContent) {
		if (sentence.charAt(offset) == '<') {
			bracketContent.setLength(0); // 方括号中的内容
			int i = offset + 1;
			boolean endWithBracket = false;
			while (i < sentence.length()) {
				char c = sentence.charAt(i);
				if (c == '>') {
					endWithBracket = true;
					break;
				}
				bracketContent.append(c);
				i++;
			}

			if (endWithBracket && bracketContent.length() > 0) {
				return i;
			}
		}

		return offset;
	}

	public static int matchNormalWord(String sentence, int offset) {
		int i = offset;
		while (i < sentence.length()) {
			char c = sentence.charAt(i);
			if (c == '[' || c == '<') {
				break;
			}
			i++;
		}
		return i;
	}

	// 一个小的语义类
	//[平台|系统]
	public static int matchOptionWords(String sentence, int offset, String semName, Rule rule) {
		if (sentence.charAt(offset) == '[') {
			StringBuilder bracketContent = new StringBuilder(); // 方括号中的内容
			int i = offset + 1;
			boolean endWithBracket = false;
			while (i < sentence.length()) {
				char c = sentence.charAt(i);
				if (c == ']') {
					endWithBracket = true;
					break;
				}
				bracketContent.append(c);
				i++;
			}

			if (endWithBracket && bracketContent.length() > 0) {
				ArrayList<String> words = getOptionWords(bracketContent
						.toString());
				addWords(words, semName, rule);
				return i;
			}
		}

		return offset;
	}

	public static void addWords(ArrayList<String> words,
			String semName, Rule rule) {
		for (String w : words) {
			String ruleName = getRuleName(w);
			if (ruleName == null) {
				String type = semName; // 语义类的名字

				// logger.debug("TO add Word " + w);
				rule.addWord(w, type); // 终结符放入词典三叉树
			} else {
				//TODO
				//rule.addRule(new NonTerminal(semName), ruleName); // 非终结符放入文法树
			}
		}
	}

	public static String getRuleName(String lhs) {
		int i = 0;
		boolean beginWithBracket = false;
		while (i < lhs.length()) {
			char c = lhs.charAt(i);
			if (c == '<') {
				beginWithBracket = true;
				break;
			}
			i++;
		}
		if (!beginWithBracket) {
			return null;
		}

		i++;
		StringBuilder bracketContent = new StringBuilder();
		boolean endWithBracket = false;
		while (i < lhs.length()) {
			char c = lhs.charAt(i);
			if (c == '>') {
				endWithBracket = true;
				break;
			}
			bracketContent.append(c);
			i++;
		}

		if (endWithBracket && bracketContent.length() > 0) {
			return bracketContent.toString();
		}
		return null;
	}

	// 得到可选终结符和非终结符
	public static ArrayList<String> getOptionWords(String t) {
		// logger.debug("input " + t);
		ArrayList<String> words = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(t, "|"); // |分隔
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			//logger.debug("get OptionWord " + s);
			words.add(s);
		}
		return words;
	}

	public static int matchSpace(String sentence, int offset) {
		int first = offset;

		for (; first < sentence.length(); first++)
			if (!Character.isWhitespace(sentence.charAt(first)))
				break;

		return first;
	}

}
