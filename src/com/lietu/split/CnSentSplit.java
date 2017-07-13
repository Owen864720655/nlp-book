package com.lietu.split;

/**
 * 中文句子切分
 * @author luogang
 *
 */
public class CnSentSplit {
	public static TernarySearchTrie eosDic = getDic(); // 可能是句子结束符号的标点

	private static TernarySearchTrie getDic() {
		TernarySearchTrie dic = new TernarySearchTrie();
		dic.add(". ");
		dic.add(".\"");
		dic.add("\".");
		dic.add(".\n");
		dic.add(".\r\n");
		dic.add("!");
		dic.add("!\"");
		dic.add("\"!");
		dic.add("?");
		dic.add("? ");
		dic.add("?\"");
		dic.add("\"?");
		dic.add(";");
		dic.add("..");
		dic.add("...");
		dic.add("....");
		dic.add(".....");
		dic.add("......");
		dic.add("..\"");
		dic.add("...\"");
		dic.add("....\"");
		dic.add(".....\"");
		dic.add("......\"");
		
		dic.add("。");
		dic.add("！");
		dic.add("？");
		dic.add("！");
		dic.add("…");
		return dic;
	}

	// 找下一个切分点
	public static int nextPoint(String text, int lastEOS) {
		int i = lastEOS;
		while (i < text.length()) {
			//System.out.println("i  :"+text.substring(i));
			
			// 然后再找标点符号
			String toFind = eosDic.matchLong(text, i);
			if (toFind != null) {
				// System.out.println("findFirst :"+toFind
				// +" lastEOS "+lastEOS);
				//boolean isEndPoint = isSplitPoint(text, lastEOS, i);
				//if (isEndPoint) {
					return i + toFind.length();
				//}
				//i = i + toFind.length();
			} else {
				i++;
				// System.out.println("not find");
			}
		}
		return text.length();
	}

}
