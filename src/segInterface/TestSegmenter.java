package segInterface;

import junit.framework.TestCase;

public class TestSegmenter extends TestCase{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String text = "大学生活动中心888iii来到这里123,7878,7744367 2015年12月12日";
		Segmenter seg = new Segmenter(text); //切分文本  //掱
		String word;
		do {
			word = seg.nextWord(); //返回一个词
			
			if(word==null)
				return;
			System.out.println(word);
		} while (true);
	}
	
	public static void testSimple()throws Exception{
		String text = "大学生活动中心";
		Segmenter seg = new Segmenter(text); //切分文本 
		String word;
		word = seg.nextWord(); //返回一个词
		System.out.println(word);
		word = seg.nextWord(); //返回一个词
		System.out.println(word);
		word = seg.nextWord(); //返回一个词
		System.out.println(word);
		word = seg.nextWord(); //返回一个词
		System.out.println(word);
	}
	
	public static void testNextWord()throws Exception{
		String text = "大学生活动中心";
		Segmenter seg = new Segmenter(text); //切分文本 
		String word;
		word = seg.nextWord(); //返回一个词
	}

}
