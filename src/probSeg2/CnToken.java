package probSeg2;

/**
 * 
 * 有向边，边的权重不能是负数。
 * 
 * This class models an edge from vertex X to vertex Y -- pretty
 * straightforward.
 * 
 * It is a little wasteful to keep the starting vertex, since the adjacency list
 * will do this for us -- but it makes the code neater in other places (makes
 * the Edge independent of the Adj. List
 */
public class CnToken {
	public String termText; //词
	public int start;
	public int end;
	public double logProb;

	public CnToken(int vertexFrom, int vertexTo, double logP, String word) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		logProb = logP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		long temp;
		temp = Double.doubleToLongBits(logProb);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + start;
		result = prime * result
				+ ((termText == null) ? 0 : termText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CnToken other = (CnToken) obj;
		if (end != other.end)
			return false;
		if (Double.doubleToLongBits(logProb) != Double
				.doubleToLongBits(other.logProb))
			return false;
		if (start != other.start)
			return false;
		if (termText == null) {
			if (other.termText != null)
				return false;
		} else if (!termText.equals(other.termText))
			return false;
		return true;
	}

	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end
				+ " cost:" + logProb;
	}
}