package com.lietu.EventExtract;

/**
 * 转移概率
 */
public class ContextStatDoc {

	private static int[][] transProbs;

	private static ContextStatDoc cs = new ContextStatDoc();

	public static ContextStatDoc getInstance() {
		return cs;
	}

	private ContextStatDoc() {
		int matrixLength = DocType.values().length;
		transProbs = new int[matrixLength][];

		for (int i = 0; i < DocType.values().length; ++i) {
			transProbs[i] = new int[matrixLength];
		}
	}

	public void addTrob(DocType prev, DocType cur, int val) {
		transProbs[prev.ordinal()][cur.ordinal()] = val;
	}

	/**
	 * get context possibility
	 * 
	 * @param nPrev
	 *            the previous POS
	 * @param nCur
	 *            the current POS
	 * @return the context possibility between two POSs
	 */
	public int getContextPossibility(DocType prev, DocType cur) {
		return transProbs[prev.ordinal()][cur.ordinal()];
	}

	public static int[][] getTransProbs() {
		return transProbs;
	}
}
