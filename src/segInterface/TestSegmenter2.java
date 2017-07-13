package segInterface;

public class TestSegmenter2 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String text = "大学生活动中心888iii来到这里123,7878,7744367 2015年12月12日";
		Segmenter2 seg = new Segmenter2(text); //切分文本  //掱
		String word;
		do {
			word = seg.nextWord(); //返回一个词
			
			if(word==null)
				return;
			System.out.println(word);
		} while (true);
	}
	
}
