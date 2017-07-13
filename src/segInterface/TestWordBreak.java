package segInterface;

import java.text.BreakIterator;
import java.util.Locale;

public class TestWordBreak {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String stringToExamine = "产品和服务";
		BreakIterator boundary = BreakIterator.getWordInstance(Locale.CHINESE);
		boundary.setText(stringToExamine);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			System.out.println(stringToExamine.substring(start, end));
		}
	}

}
