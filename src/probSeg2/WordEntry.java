package probSeg2;

public class WordEntry {
	public String term; //词
	public int freq; //词频

	public WordEntry(String w, int f) {
		term = w;
		freq = f;
	}

	public String toString() {
		return term + ":" + freq;
	}
}
