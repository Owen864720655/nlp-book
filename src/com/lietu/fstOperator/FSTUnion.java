package com.lietu.fstOperator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.lietu.fst.CharSpan;
import com.lietu.fst.FST;
import com.lietu.fst.State;

/**
 * DFA求并集 The key to understand is that you have to run the two DFAs
 * simultanously, or in general you have to maintain the states of both DFAs in
 * the union DFA. That's why you have to create the new states for the union DFA
 * as a direct multiplication of the original states. This way you have a state
 * for every combination of the states in original DFAs. The transition rules
 * for the new DFA can be directly calculated then. For example if you are in
 * state Ab, and you get a 0 on input, the first DFA would go to state B and the
 * second one to state b, so the union DFA's next state for this input will be
 * Bb. This method works in every case when you need to make a union of two or
 * more DFAs. The resulting DFA may not be optimal, but later you can minimize
 * it with any algorithm you like.
 * 
 * @author luogang
 * @2013-10-23
 */
public class FSTUnion {

	// 对两个输入集合求并集
	private static Set<CharSpan> union(Set<com.lietu.fst.Transition> setA,
			Set<com.lietu.fst.Transition> setB) {
		Set<CharSpan> tmp = new HashSet<CharSpan>();
		//if (setA != null) {
			for (com.lietu.fst.Transition x : setA) {
				tmp.add(new CharSpan(x.min,x.max));
			}
		//}
		//if (setB != null) {
			for (com.lietu.fst.Transition x : setB) {
				tmp.add(new CharSpan(x.min,x.max));
			}
		//}
		return tmp;
	}
	
	public static Set<CharSpan> getCharSpans(Set<com.lietu.fst.Transition> transitions){
		HashSet<CharSpan> charSpans = new HashSet<CharSpan>();
		for(com.lietu.fst.Transition t :transitions){
			CharSpan cs=new CharSpan(t.min,t.max);
			charSpans.add(cs);
		}
		return charSpans;
	}

	FST dfa1;
	FST dfa2;
	HashMap<StatePair,StatePair> allStates;
	
	public FSTUnion(FST dft1, FST dft2){
		dfa1 = dft1;
		dfa2 = dft2;
		allStates = new HashMap<StatePair,StatePair>();
	}
	
	public StatePair getStatePair(State state1, State state2){
		StatePair newStackPair = new StatePair(state1, state2);
		
		StatePair equality = allStates.get(newStackPair);  //看是否已经有等价状态对
		if(equality==null){
			allStates.put(newStackPair, newStackPair);
			return newStackPair;
		}
		return equality;
	}
	
	public FST union() {
		if (dfa2 == null)
			return dfa1;
		//System.out.println("union..........");
		Set<StatePair> visited = new HashSet<StatePair>();  //不重复遍历状态对
		Stack<StatePair> stack = new Stack<StatePair>();
		StatePair initial = new StatePair(dfa1.initial, dfa2.initial);
		FSTStatePair newDFA = new FSTStatePair(initial);
		stack.add(initial);
		visited.add(initial);

		while (!stack.isEmpty()) {
			StatePair stackValue = stack.pop();  //源状态对
			//System.out.println(toString(stackValue));
			Set<CharSpan> ret = null;
			if(stackValue.s1==null)
				ret = getCharSpans(stackValue.s2.getTransitions());
			else if(stackValue.s2==null)
				ret = getCharSpans(stackValue.s1.getTransitions());
			else
				ret = union(stackValue.s1.getTransitions(), stackValue.s2.getTransitions());
			
			for (CharSpan edge : ret) {
				//System.out.println(edge);
				
				//同步向前遍历
				State state1 = null;
				if(stackValue.s1!=null){
					state1 = stackValue.s1.step(edge);
				}
				
				State state2 = null;
				if(stackValue.s2!=null){
					state2 = stackValue.s2.step(edge);
				}
				if (state1 == null && state2 == null) {
					continue;
				}
				
				StatePair nextStackPair = getStatePair(state1, state2);
						//new StatePair(state1, state2);
				stackValue.addTransition(edge, nextStackPair);
				//System.out.println("addTransition "+toString(nextStackPair));
				if (!visited.contains(nextStackPair)) {
					stack.add(nextStackPair);
					visited.add(nextStackPair);
				}
			}
		}
		
		//System.out.println("FSTStatePair");
		
		//System.out.println(newDFA.toString());

		//System.out.println("FSTStatePair end");
		
		FST finaDFA = new FST(newDFA); // 把状态对表示的DFA转换成整数表示的DFA
		return finaDFA;
	}
	
	public static String toString(StatePair s){
		StringBuilder b = new StringBuilder();
		b.append("StatePair [");
		if(s.s1!=null){
			b.append(s.s1.id);
		}
		b.append(",");
		if(s.s2!=null){
			b.append(s.s2.id);
		}
		b.append("]");
		return b.toString();
	}
}
