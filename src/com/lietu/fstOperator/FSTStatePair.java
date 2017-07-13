package com.lietu.fstOperator;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.lietu.fst.State;

public class FSTStatePair {
	public StatePair initial; // 开始状态

	public FSTStatePair(StatePair start) {
		initial = start;
	}

	/** 
	 * Returns the set of states that are reachable from the initial state.
	 * @return set of {@link State} objects
	 */
	public Set<StatePair> getStates() {
		Set<StatePair> visited = new HashSet<StatePair>();
		LinkedList<StatePair> worklist = new LinkedList<StatePair>();  //宽度遍历
		worklist.add(initial);
		visited.add(initial);
		while (worklist.size() > 0) {
			StatePair s = worklist.removeFirst();
			Collection<Transition> tr = s.transitions;
			if(tr==null)
				continue;
			for (Transition t : tr)
				if (!visited.contains(t.to)) {
					visited.add(t.to);
					worklist.add(t.to);
				}
		}
		return visited;
	}

	public StatePair getInitialState() {
		return initial;
	}

	/** 
	 * 分配连续的号码到给定状态
	 */
	static void setStateNumbers(Set<StatePair> states) {
		int number = 0;
		for (StatePair s : states)
			s.number = number++;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		Set<StatePair> states = getStates();
		setStateNumbers(states);
		b.append("initial state: ").append(initial.number).append("\n");
		for (StatePair s : states)
			b.append(s.toString()).append("\n");

		return b.toString();
	}
}
