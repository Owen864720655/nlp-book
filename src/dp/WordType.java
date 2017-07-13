package dp;

public class WordType {
	public String word; //词
	public int freq; //词频

	public WordType(String w, int f) {
		word = w;
		freq = f;
	}

	public String toString() {
		return word + ":" + freq;
	}
}
