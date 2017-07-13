package uk.co.jbothma.terms;

import java.util.ArrayList;
import java.util.Collections;

public class Demo {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] phrases = {
				"a", "a", "a",
				"b", "b",
				"c",
				"b a", "b a",
				"c b a",
		};
		CValueSess cvals;
		ArrayList<Candidate> candList; //一个短语就是一个候选术语

		cvals = new CValueSess();



		for (String phrase : phrases ) {

			cvals.observe(phrase);
		}

		cvals.calculate();

		candList = new ArrayList<Candidate>(cvals.getCandidates());
		Collections.sort(candList, new CValueComparator());

		for (Candidate cand : candList) {
			String resultLine = 
					cand.getLength() + " " + 
							cand.getFrequency() + "  " + 
							cand.getNesterCount() + " " +
							cand.getFreqNested() + " " +
							cand.getString() + " " +
							cand.getCValue();
			System.out.println(resultLine);
		}
	}
}

