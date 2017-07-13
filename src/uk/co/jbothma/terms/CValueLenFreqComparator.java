package uk.co.jbothma.terms;

import java.util.Comparator;

public class CValueLenFreqComparator implements Comparator<Candidate> {

	@Override
	public int compare(Candidate o1, Candidate o2) {
		if (o1.getLength() < o2.getLength()) {
			return -1;
		} else if (o1.getLength() == o2.getLength()) {
			if (o1.getFrequency() < o2.getFrequency()) {
				return -1;
			} else if (o1.getFrequency() == o2.getFrequency()) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}

	}

}
