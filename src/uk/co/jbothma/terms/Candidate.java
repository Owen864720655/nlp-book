package uk.co.jbothma.terms;

import java.util.ArrayList;
import java.util.Collection;

public class Candidate {
	private int freq, len, uniqNesters, freqNested;
	private String string;

	public Candidate(String string) {
		this.string = string;
		freq = 0;
		freqNested = 0;
		uniqNesters = 0;
		len = string.split(" ").length;
	}

	public void observe() {
		freq++;
	}

	public String getString() {
		return string;
	}
	
	public Collection<String> getSubstrings() {
		String substring;
		String[] words = string.split(" ");
		ArrayList<String> substrings = new ArrayList<String>();
		
		for (ArrayList<Integer> idxs : substrIdxs(words.length)) {
			substring = new String();
			for (int i : idxs) {
				substring += words[i-1] + " "; // idxs start from 1, not 0
			}
			substrings.add(substring.trim());
		}
		return substrings;		
	}
	
	public double getCValue() {
		double log_2_lenD = (Math.log((double)len)/Math.log((double)2));
		double freqD = (double) freq;
		double invUniqNestersD = 1D / (double) uniqNesters;
		double freqNestedD = (double) freqNested;
		
		if (uniqNesters == 0) {
			return log_2_lenD * freqD;
		} else {
			return log_2_lenD * (freqD - invUniqNestersD * freqNestedD);
		}
	}
	
	public int getFrequency() {
		return freq;
	}

	public int getLength() {
		return len;
	}
	
	public int getNesterCount() {
		return uniqNesters;
	}
	
	public void observeNested() {
		uniqNesters++;
	}
	
	public int getFreqNested() {
		return freqNested;
	}
	
	public void incrementFreqNested(int freq) {
		freqNested += freq;
	}
	
	public String toString() {
		return string;
	}

	private static ArrayList<ArrayList<Integer>> substrIdxs(int count) {
		ArrayList<ArrayList<Integer>> idxs = new ArrayList<ArrayList<Integer>>();
		
		// substring means we don't want indices of full length hence < count
		for (int ii = 1; ii < count; ii++) {
			idxs.addAll(substrIdxs(count, ii));
		}
		return idxs;
	}	

	private static ArrayList<ArrayList<Integer>> substrIdxs(int count, int len) {
		int items = count-len+1;
		ArrayList<ArrayList<Integer>> idxs = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> item;
		
		for (int ii = 1; ii <= items; ii++) {
			item = new ArrayList<Integer>();
			for (int i2 = 0; i2 < len; i2++) {
				item.add(ii + i2);
			}
			idxs.add(item);
		}
		return idxs;
	}
}
