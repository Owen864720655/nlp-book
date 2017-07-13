package probSeg;

public class WordEntry {
	public String term;
	public int freq;

	public WordEntry(String w, int f) {
		term = w;
		freq = f;
	}

	public String toString() {
		return term + ":" + freq;
	}
}
