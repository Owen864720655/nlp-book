/*
 * Created on 2005-9-7
 *
 */
package com.lietu.automSeg;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * 有向边
 */
public class CnToken {
	public String termText;
	public HashSet<String> types; //词的各种词性 
	public int start;
	public int end;
	public boolean completed = false; //这条边是否能够完整的表示一个问题
	
	public CnToken(int vertexFrom, int vertexTo, String word) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
	}
	
	public CnToken(int vertexFrom, int vertexTo, String word,HashSet<String> t) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		HashSet<String> typs = new HashSet<String>(t.size());
		for(String wt:t){
			typs.add(wt);
		}
		types = typs;
	}
	
	public CnToken(int vertexFrom, int vertexTo, String word,String n) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		HashSet<String> types = new HashSet<String>(1);
		types.add(n);
		this.types = types;
	}
	
	public CnToken(int vertexFrom, int vertexTo, String word,String n, boolean c) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		HashSet<String> types = new HashSet<String>(1);
		types.add(n);
		this.types = types;
		completed = c;
	}

	public CnToken(String word , int vertexFrom, int vertexTo,
			Collection<String> ts) {
		termText = word;
		start = vertexFrom;
		end = vertexTo;
		types = new HashSet<String>(1);
		types.addAll(ts);
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
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}
	
	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end+" types:"+types;
	}

}