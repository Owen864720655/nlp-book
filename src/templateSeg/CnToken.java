package templateSeg;

import java.util.HashSet;

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
	public String termText;
	public HashSet<POSType> types;
	public int start;
	public int end;
	public double logProb;
    
	public CnToken(int vertexFrom, int vertexTo,  double logP,String word,HashSet<POSType> d) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		types = d;
		logProb = logP;
	}

	@Override
	public boolean equals(Object obj) {
		//System.out.println("CnToken  比较相等 " + this +" : that: " + obj);
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CnToken other = (CnToken) obj;
		if (end != other.end)
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
				+" logProb "+logProb +"--------types:"+types;
	}
}