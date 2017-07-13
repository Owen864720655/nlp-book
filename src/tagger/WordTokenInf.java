/*
 * Created on 2005-9-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tagger;

//import com.lietu.analysis.Constant;
//import com.lietu.analysis.WordTypes;

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
public class WordTokenInf {
	public String termText;
	public WordTypes data;
	public int start;
	public int end;
	public long cost = Constant.SINGLE_WORD_SMOOTH_VALUE;	// 默认的单子平滑系数是5
	
	public WordTokenInf(int vertexFrom, int vertexTo, String word, WordTypes d) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		data = d;
		if (d != null) {
			cost = d.totalCost();
		}
	}
	
	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end + "--------data:" + data;
	}
}