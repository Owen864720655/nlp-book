/*
 * Created on 2005-9-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package probSeg;

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
	public String termText;  //词
	public int start;  //词在文本中出现的位置
	public int end;
	public double logProb;  //词概率 log(P(w))

	public CnToken(int vertexFrom, int vertexTo, double logP, String word) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		logProb = logP;
	}

	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end
				+ " cost:" + logProb;
	}
}