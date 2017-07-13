package segInterface;

import java.text.BreakIterator;
import java.util.Locale;

public class TestSegSentence {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// "Is it? Yes, it is. OK.";
		String stringToExamine = "可那是什么啊?1946年，卡拉什尼科夫开始设计突击步枪。在这种半自动卡宾枪的基础上设计出一种全自动步枪，并送去参加国家靶场选型试验。样枪称之为AK-46，即1946年式自动步枪。";
		
		BreakIterator boundary = BreakIterator
				.getSentenceInstance(Locale.CHINESE);
		boundary.setText(stringToExamine);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			System.out.println(stringToExamine.substring(start, end));
		}

	}

}
