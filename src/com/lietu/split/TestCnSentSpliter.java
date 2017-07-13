package com.lietu.split;

import com.lietu.split.CnSentSplit;

public class TestCnSentSpliter {

	public static void main(String[] args) {
		String text = "陈丹青：多数家长都是失败的一代，扭曲的一代。我很少遇到有眼光的家长，但我也理解，他们只有一个孩子，只是这孩子太惨了，那还叫孩子吗?所以，我认为家长是第一凶手，学校是第二凶手，他们合伙把孩子弄成跟他们一样，这些孩子长大后又成了下一茬凶手。每个中国孩子的自杀之路，从他生出在一个中国家庭的那天起就已经开始了。 ";

		int lastEOS = 0;
		do {
			// 找下一个切分点
			int nextEOS = CnSentSplit.nextPoint(text, lastEOS);
			// System.out.println(nextEOS + " text.length() "+text.length());
			// 根据上一个切分点和下一个切分点分出句子
			String sent = text.substring(lastEOS, nextEOS);
			System.out.println(sent);

			lastEOS = nextEOS;
		} while (lastEOS < text.length());
	}
}
