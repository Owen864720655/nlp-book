package uk.co.jbothma.terms;

import java.util.Comparator;

public class CValueComparator implements Comparator<Candidate> {

	@Override
	public int compare(Candidate o1, Candidate o2) {
		double c1 = o1.getCValue();
		double c2 = o2.getCValue();
		if (c1 < c2)
			return -1;
		else if (c2 == c1)
			return 0;
		else
			return 1;
	}
	
}
