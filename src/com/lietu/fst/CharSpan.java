package com.lietu.fst;

/**
 * 字符区间
 * @author luogang
 *
 */
public class CharSpan {
	public char min;
	public char max;
	
	public CharSpan(char min, char max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
		result = prime * result + min;
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
		CharSpan other = (CharSpan) obj;
		if (max != other.max)
			return false;
		if (min != other.min)
			return false;
		return true;
	}
}
