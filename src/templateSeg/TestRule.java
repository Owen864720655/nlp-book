package templateSeg;

import junit.framework.TestCase;

public class TestRule extends TestCase {

	public static void main(String[] args) throws Exception {
		String rule = "<adj>的<n>";
		String ruleName = "1";
		System.out.println(RuleParser.parse(ruleName, rule));
	}
	
	public static void testPOS(){
		String rule = "<p><n><v>";//词性序列  也就是非终结符
		String ruleName = "1";
		System.out.println(RuleParser.parse(ruleName, rule));
	}
	
	public static void testWord(){
		String rule = "为<nr>求情"; //为+人名+求情
		String ruleName = "1";
		System.out.println(RuleParser.parse(ruleName, rule));
	}

	public static void testWord2(){
		String rule = "从<n><f>下来"; //从马上下来
		String ruleName = "1";
		System.out.println(RuleParser.parse(ruleName, rule));
	}
	
	public static void testCL(){ //词林
		//同义词词林中的义项：		An04D01=毒贩 毒枭 贩毒者
		String pattern = "为<An04D01>求情"; //为+人名+求情
		//String ruleName = "1";
		Rule r = RuleParser.parse(pattern);
		System.out.println(r);
	}
	//<p><An04D01><v> => 为<An04D01>求情
}
