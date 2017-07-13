package com.lietu.automSeg;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.lietu.fst.FST;
import com.lietu.fst.FSTFactory;
import com.lietu.fst.State;

/**
 * 根据自动机得到词图
 * 用于原子切分
 * TODO
 * 
 * @author luogang
 *
 */
public class FSTSGraph {
	public FST fst;

	public FSTSGraph() throws Exception {
		//Automaton num = AutomatonFactory.getNum();
		//fst = new FST(num, TernarySearchTrie.num);
		fst = //FSTFactory.createSimple();
				FSTFactory.createAll();
	}

	public FSTSGraph(FST f) {
		fst = f;
	}

	public static void addPoints(Collection<CnToken> tokens,BitSet startPoints,BitSet endPoints) {
		for(CnToken t:tokens){
			//System.out.println("set Point "+(t.end-1));
			endPoints.set(t.end);
			if(t.types==null){
				//System.out.println("set blank start Point "+(t.end));
				//startPoints.set(t.end);
			}
			else{
				//System.out.println("set nonblank start Point "+(t.start));
				startPoints.set(t.start);
			}
		}
	}

	private Collection<CnToken> matchAll(String s, int offset) {
		HashMap<String, Integer> count = new HashMap<String, Integer>(); // 每个类型最长的匹配
		HashMap<String, CnToken> tokens = new HashMap<String, CnToken>(); // 每个类型对应的Token

		State p = fst.initial;
		// System.out.println("initial state "+p);
		int i = offset;
		for (; i < s.length(); i++) {
			State q = p.step(s.charAt(i));
			// System.out.println("new state "+q);
			if (q == null) {
				break;
			}
			p = q;

			if (p.automaton2WordType != null) { // 碰到一个可结束的状态
				int end = i + 1;
				CnToken token = new CnToken(s.substring(offset, end), offset, end,
						p.automaton2WordType.values());
				int matchLen = (i - offset); // 匹配长度
				count(p.automaton2WordType, count, tokens, matchLen, token);

				//System.out.println("automatonType "+p.automaton2WordType.values() +" tokens "+tokens);
			}
		}
		return tokens.values();
	}
	
	public void count(HashMap<String, String> automaton2WordType,
			HashMap<String, Integer> count, HashMap<String, CnToken> tokens,
			int len, CnToken token) {
		for (Entry<String, String> e : automaton2WordType.entrySet()) {
			String automatonName = e.getKey();
			Integer matchLen = count.get(automatonName);

			if (matchLen == null) {
				count.put(automatonName, len);
				tokens.put(automatonName, token);
			} else if (len > matchLen) {
				count.put(automatonName, len);
				tokens.put(automatonName, token);
				//System.out.println("tokens put automatonName  "+automatonName );//+ " "+token
			}
		}
	}

	public CnToken matchLong(String s, int offset){
		State p = fst.initial;
		// System.out.println("initial state "+p);
		int i = offset;
		State toReturn = null;
		int end = offset;
		for (; i < s.length(); i++) {
			State q = p.step(s.charAt(i));
			// System.out.println("new state "+q);
			if (q == null) {
				break;
			}
			p = q;

			if (p.automaton2WordType != null) {
				toReturn = p;
				end = i + 1;
			}
		}
		if (toReturn != null) {
			return new CnToken(s.substring(offset, end), offset, end,
					toReturn.automaton2WordType.values());
		}
		return null;
	}
	
	public SplitPoints splitPoints(String sentence){
		int senLen = sentence.length();

		SplitPoints splitPoints = new SplitPoints(senLen);
		
		int i = 0;// 用来控制匹配的起始位置的变量
		
		//startPoints.set(0);
		while (i < senLen) {
			//System.out.println("match i:"+i);
			Collection<CnToken> tokens = matchAll(sentence, i);
			if (tokens.size()>0) {
				//System.out.println("FST切分的结果" + tokens);
				
				//addPoints(tokens);
				for(CnToken t:tokens){
					//System.out.println("set Point "+(t.end-1));
					splitPoints.endPoints.set(t.end);
					splitPoints.startPoints.set(t.start); //不管是不是空格,都设置开始匹配点
				}
				
				i = minEnd(tokens);
			}
			else{  //单个字符作为一个Token
				splitPoints.endPoints.set(i+1);
				splitPoints.startPoints.set(i);
				//System.out.println("set unknow start Point "+i);
				i++;
			}
		}

		return splitPoints;
	}

	//找最小的结束位置
	static int minEnd(Collection<CnToken> tokens){
		int min = Integer.MAX_VALUE;
		
		for(CnToken t:tokens){
			if(t.end<min){
				min = t.end;
			}
		}
		return min;
	}
}
