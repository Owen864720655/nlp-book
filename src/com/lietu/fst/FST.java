package com.lietu.fst;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

//import com.koolearn.unknowWords.Token;


import com.lietu.fstOperator.FSTStatePair;
import com.lietu.fstOperator.FSTUnion;
import com.lietu.fstOperator.StatePair;

import dk.brics.automaton.Automaton;

/**
 * 把 FSM转换成FST,然后再根据上下文把基本类型转换成最终的类型
 * 
 * @author luogang
 * 
 */
public class FST {
	public State initial;

	static int id = 0; // 用于得到唯一的名字
	
	//根据Automaton构造FST
	public FST(Automaton a, String type) throws Exception {
		if (!a.deterministic) {
			throw new Exception("Automaton必须是DFA");
		}

		String name = String.valueOf(id++);  //fst的名字
		HashMap<dk.brics.automaton.State, State> m = new HashMap<dk.brics.automaton.State, State>();
		Set<dk.brics.automaton.State> states = a.getStates();
		for (dk.brics.automaton.State s : states)
			m.put(s, new State());
		for (dk.brics.automaton.State s : states) {
			State p = m.get(s);

			if (s.accept) {
				// p.wordType = new HashSet<String>();
				// p.automatonType = new HashSet<String>();
				p.automaton2WordType = new HashMap<String, String>();

				if (type != null) {
					// p.wordType.add(type);
					// p.automatonType.add(name);
					p.automaton2WordType.put(name, type);
				}
			}
			if (s == a.getInitialState()) {
				initial = p;
			}
			for (dk.brics.automaton.Transition t : s.transitions) {
				p.transitions.add(new Transition(t.min, t.max, m.get(t.to)));
			}
		}
	}

	public static Set<CharSpan> getCharSpans(
			Set<com.lietu.fstOperator.Transition> transitions) {
		HashSet<CharSpan> charSpans = new HashSet<CharSpan>();
		for (com.lietu.fstOperator.Transition t : transitions) {
			CharSpan cs = new CharSpan(t.min, t.max);
			charSpans.add(cs);
		}
		return charSpans;
	}

	public FST(FSTStatePair fstPair) {
		HashMap<StatePair, State> m = new HashMap<StatePair, State>();
		Set<StatePair> states = fstPair.getStates();
		for (StatePair s : states)
			m.put(s, new State());
		for (StatePair s : states) {
			State p = m.get(s);

			// p.wordType = s.wordType;
			// p.automatonType = s.automatonType;
			p.automaton2WordType = s.automaton2WordType;
			if (s == fstPair.getInitialState()) {
				initial = p;
			}

			if (s.transitions == null)
				continue;
			for (com.lietu.fstOperator.Transition t : s.transitions) {
				p.transitions.add(new Transition(t.min, t.max, m.get(t.to)));
			}
		}
	}

	public FST union(FST f) {
		FSTUnion union = new FSTUnion(this, f);

		return union.union();
	}
	
	//最长匹配
	public Token matchLong(String s, int offset) {
		State p = initial; //当前状态设为初始状态
		// System.out.println("initial state "+p);
		int i = offset; 
		State toReturn = null; //候选状态
		int end = offset; //最长匹配候选位置
		for (; i < s.length(); i++) {
			State q = p.step(s.charAt(i)); //前进到下一个状态
			// System.out.println("new state "+q);
			if (q == null) {  //前面没路了
				break;
			}
			p = q;

			if (p.automaton2WordType != null) { //可以结束
				toReturn = p; //记录候选状态
				end = i + 1;
			}
		}
		if (toReturn != null) {
			String word = s.substring(offset, end);
			
			Collection<String> types = toReturn.automaton2WordType.values();
			
			return new Token(word, offset, end, types);
		}
		return null;
	}

	// 基于类型的最长匹配
	public Collection<Token> matchAll(String s, int offset) {
		HashMap<String, Integer> count = new HashMap<String, Integer>(); // 每个类型最长的匹配
		HashMap<String, Token> tokens = new HashMap<String, Token>(); // 每个类型对应的Token

		State p = initial;
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
				Token token = new Token(s.substring(offset, end), offset, end,
						p.automaton2WordType.values());
				int matchLen = (i - offset); // 匹配长度
				count(p.automaton2WordType, count, tokens, matchLen, token);

				// System.out.println("automatonType "+p.automaton2WordType);

				// tokens.add(token);
				// toReturn.add(p);
				// end = i + 1;
			}
		}
		return tokens.values();
	}

	public BitSet getEndPoints(String s) {
		int senLen = s.length();
		BitSet endPoints = new BitSet(senLen);// 存储所有可能的结束点
		int i = 0;// 用来控制匹配的起始位置的变量
		while (i < senLen) {
			//System.out.println("to find SplitPoints "+i);
			HashMap<String, Integer> lenMap = getEndPoint(s, i);

			if (lenMap.size() > 0) {
				for(Entry<String, Integer> e:lenMap.entrySet()){
					int newPoint = i+e.getValue()-1;
					endPoints.set(newPoint);
					//System.out.println("newPoint "+newPoint);
				}
				i += min(lenMap);
				
			} else { // 单个字符作为一个Token
				endPoints.set(i);
				i++;
			}
		}
		return endPoints;
	}

	// 找最小的结束位置
	static int min(HashMap<String, Integer> lenMap) {
		int min = Integer.MAX_VALUE;

		for (Entry<String, Integer> e : lenMap.entrySet()) {
			if (e.getValue() < min) {
				min = e.getValue();
			}
		}
		return min;
	}

	public HashMap<String, Integer> getEndPoint(String s, int offset) {
		HashMap<String, Integer> lenMap = new HashMap<String, Integer>(); // 每个类型最长的匹配

		State p = initial;
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
				int matchLen = i - offset+1; // 匹配长度
				count(p.automaton2WordType, lenMap, matchLen);

				//System.out.println("automatonType "+p.automaton2WordType +" matchLen "+matchLen);
			}
		}

		return lenMap;
	}

	private void count(HashMap<String, String> automaton2WordType,
			HashMap<String, Integer> count, int len) {
		for (Entry<String, String> e : automaton2WordType.entrySet()) {
			String automatonName = e.getKey();
			Integer matchLen = count.get(automatonName);

			if (matchLen == null) {
				count.put(automatonName, len);
			} else if (len > matchLen) {
				count.put(automatonName, len);
			}
		}
	}

	public void count(HashMap<String, String> automaton2WordType,
			HashMap<String, Integer> count, HashMap<String, Token> tokens,
			int len, Token token) {
		for (Entry<String, String> e : automaton2WordType.entrySet()) {
			String automatonName = e.getKey();
			Integer matchLen = count.get(automatonName);

			if (matchLen == null) {
				count.put(automatonName, len);
				tokens.put(automatonName, token);
			} else if (len > matchLen) {
				count.put(automatonName, len);
				tokens.put(automatonName, token);
			}
		}
	}

	/**
	 * Returns the set of states that are reachable from the initial state.
	 * 
	 * @return set of {@link State} objects
	 */
	public Set<State> getStates() {
		Set<State> visited = new HashSet<State>();

		LinkedList<State> worklist = new LinkedList<State>();
		worklist.add(initial);
		visited.add(initial);
		while (worklist.size() > 0) {
			State s = worklist.removeFirst();
			Collection<Transition> tr = s.transitions;
			for (Transition t : tr)
				if (!visited.contains(t.to)) {
					visited.add(t.to);
					worklist.add(t.to);
				}
		}
		return visited;
	}

	/**
	 * 分配连续的号码到给定状态
	 */
	static void setStateNumbers(Set<State> states) {
		int number = 0;
		for (State s : states)
			s.number = number++;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		Set<State> states = getStates();
		setStateNumbers(states);
		b.append("initial state: ").append(initial.number).append("\n");
		for (State s : states)
			b.append(s.toString());

		return b.toString();
	}

}
