package tagger;

public class WordEntry {
	public String termText;
	public WordTypes types;
	public int freq;

	public WordEntry(String w, PartOfSpeech type,int frq) {
		types = new WordTypes();
		types.put(new WordTypes.WordTypeInf(type,frq));

		termText = w;
		freq+=frq;
	}

	public void addType(PartOfSpeech type,int frq) {
		if (types == null)
			types = new WordTypes();
		types.put(new WordTypes.WordTypeInf(type,frq));
		freq+=frq;
	}

	public String toString() {
		return termText + ":" + types;
	}

}
