package bigramSeg;

import java.util.HashSet;

public class WordEntry {
	public String word;
	public HashSet<POSType> types;
	public int freq;

	public WordEntry(String w, HashSet<String> t) {
		word = w;
		if (types == null)
			types = new HashSet<POSType>();
		for(String wordPOS:t){
			types.add(new POSType(wordPOS,0));
		}
	}

	public WordEntry(String w, String type,int frq) {
		types = new HashSet<POSType>();
		types.add(new POSType(type,frq));

		word = w;
		freq+=frq;
	}

	public void addType(String type,int frq) {
		if (types == null)
			types = new HashSet<POSType>();
		types.add(new POSType(type,frq));
		freq+=frq;
	}

	public void addType(HashSet<String> value) {
		if (types == null)
			types = new HashSet<POSType>();
		//types.addAll(value);
		for(String wordPOS:value){
			types.add(new POSType(wordPOS,0));

		}
	}

	public String toString() {
		return word + ":" + types;
	}

}
