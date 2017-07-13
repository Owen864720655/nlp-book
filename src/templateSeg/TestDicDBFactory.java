package templateSeg;

import java.sql.Connection;

import templateSeg.TernarySearchTrie.PrefixRet;
import junit.framework.TestCase;

public class TestDicDBFactory extends TestCase{

	public static void main(String[] args) {
		//TernarySearchTrie dict = (new DicDBFactory()).create(); // 得到词典
		DicFactory dicFactory = //new DicFileFactory();
				new DicDBFactory();
		TernarySearchTrie dic = dicFactory.create();
		String sentence = "中国成立了";
		int offset = 0;
		PrefixRet prefix= new PrefixRet();
		//PrefixRet prefix = new PrefixRet();
		dic.matchAll(sentence, offset,prefix);
		System.out.println(prefix.values);
	}

	public static void testConnection() {
		Connection conn = DicDBFactory.getConnect();
		System.out.println(conn);
	}

}
