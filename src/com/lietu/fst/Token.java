package com.lietu.fst;

import java.util.Collection;
import java.util.HashSet;

public class Token {
	public String termText;
	public HashSet<String> types; //词的各种词性
	public int start;
	public int end;

	public Token(String word,int vertexFrom, int vertexTo) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
	}
	
	/**
	 *  
	 * @param word  词本身
	 * @param vertexFrom  开始位置
	 * @param vertexTo   结束位置
	 * @param collection  词性
	 */
	public Token(String word,int vertexFrom, int vertexTo,Collection<String> collection) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		
		HashSet<String> typs = new HashSet<String>(collection.size());
		for(String wt:collection){
			typs.add(wt);
		}
		types = typs;
	}
	
	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end+" types:"+types;
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
		Token other = (Token) obj;
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

}
