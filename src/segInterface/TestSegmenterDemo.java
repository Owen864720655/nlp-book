package segInterface;

public class TestSegmenterDemo {

	public static void main(String[] args) throws Exception {
		String text = "大学生活动中心";
		SegmenterDemo seg = new SegmenterDemo(text); //切分文本  //掱
		String word;
		do {
			word = seg.nextWord(); //返回一个词
			
			if(word==null)
				return;
			System.out.println(word);
		} while (true);
	}

}
