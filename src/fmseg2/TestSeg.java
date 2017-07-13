package fmseg2;

import java.io.IOException;
import java.util.ArrayList;

public class TestSeg {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		FMMSegment seg = new FMMSegment();

		String sentence = //"美元";//
		//"大学生活动中心NBA";
				//"NBA篮球";
				"大学生T恤怎么卖";
		ArrayList<String> ret = seg.split(sentence);
		for (String word : ret) {
			System.out.print(word + " ");
			System.out.print(' ');
		}
		long end = System.currentTimeMillis();
		System.out.print("\ntime: "+(end - start));
	}

}
