/*
 * Created on 2005-9-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lietu.EventExtract;

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
public class DocToken {
	public String termText;
	public DocType type;
	public int start;
	public int end;
	public long cost;
	public long code;

	public DocToken(DocType typ) {
		type = typ;
	}

	public DocToken(int vertexFrom, int vertexTo, String word, DocType typ) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		type = typ;
	}

	public DocToken(int vertexFrom, int vertexTo, int c, String word, DocType typ ,long cde) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		cost = c;
		type = typ;
		code = cde;
	}
	
	public boolean match(DocType type) {
		if(type.equals(this.type)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getTermText() {
		return this.termText;
	}

	public String toString() {
		// +" cost "+cost
		return "text:" + termText + " start:" + start + " end:" + end
				+ " cost:" + cost + " pos:" + type +" code:"+code;
	}
}