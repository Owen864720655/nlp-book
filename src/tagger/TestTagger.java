package tagger;

import java.io.IOException;
import java.util.ArrayList;

public class TestTagger {

	public static void main(String[] args) throws IOException {
		LietuTagger tagger = new LietuTagger();
		String text = "把这篇文章修改好";//"湖北京山";
		
		ArrayList<WordToken> words = tagger.split(text);
		for(WordToken token : words){
			//System.out.println(token.toString());
			System.out.println(token.termText+"|"+token.type);
		}
	}

}
