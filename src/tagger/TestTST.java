package tagger;

public class TestTST {

	public static void main(String[] args) {
		DicFactory dicFactory = new DicDBFactory();
		TernarySearchTrie dic = dicFactory.create();
		String sentence = "大学生活动中心";
		int offset = 0;

		TernarySearchTrie.PrefixRet prefix = new TernarySearchTrie.PrefixRet();
		dic.matchAll(sentence, offset, prefix);
		System.out.println(sentence + " match:" + prefix.values);
	}

}
