/*
 * dk.brics.automaton
 * 
 * Copyright (c) 2001-2011 Anders Moeller
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lietu.fstOperator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.lietu.fst.CharSpan;
import com.lietu.fst.State;

/**
 * Pair of states.
 * @author Anders M&oslash;ller &lt;<a href="mailto:amoeller@cs.au.dk">amoeller@cs.au.dk</a>&gt;
 */
public class StatePair {
	State s1;
	State s2;
	//public HashSet<String> wordType;
	//public HashSet<String> automatonType; //自动机类型
	public HashMap<String,String> automaton2WordType;
	
	public Set<Transition> transitions;
	public int number;
	
	/**
	 * Constructs a new state pair.
	 * @param s1 first state
	 * @param s2 second state
	 */
	public StatePair(State s1, State s2) {
		this.s1 = s1;
		this.s2 = s2;
		
		if(s1!=null && s1.automaton2WordType!=null){
			//TODO
			if(automaton2WordType==null){
				//wordType = new HashSet<String>();
				//automatonType = new HashSet<String>();
				automaton2WordType = new HashMap<String,String>();
			}
			automaton2WordType.putAll(s1.automaton2WordType);
			//wordType.addAll(s1.wordType);
			
			//automatonType.addAll(s1.automatonType);
		}

		if(s2!=null && s2.automaton2WordType!=null){
			//TODO
			if(automaton2WordType==null){
				automaton2WordType = new HashMap<String,String>();
			}
			automaton2WordType.putAll(s2.automaton2WordType);
			//if(wordType==null){
			//	wordType = new HashSet<String>();
			//	automatonType = new HashSet<String>();
			//}
			//wordType.addAll(s2.wordType);
			
			//automatonType.addAll(s2.automatonType);
		}
	}
	
	/**
	 * Returns first component of this pair.
	 * @return first state
	 */
	public State getFirstState() {
		return s1;
	}
	
	/**
	 * Returns second component of this pair.
	 * @return second state
	 */
	public State getSecondState() {
		return s2;
	}
	
	/** 
	 * Checks for equality.
	 * @param obj object to compare with
	 * @return true if <tt>obj</tt> represents the same pair of states as this pair
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StatePair) {
			StatePair p = (StatePair)obj;
			//System.out.println("this "+this);
			//System.out.println("that "+p);
			
			boolean isEqual = (p.s1 == this.s1 && p.s2 == this.s2);
			//System.out.println("isEqual "+isEqual);
			return isEqual;
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("StatePair ").append(number);
		if (automaton2WordType!=null)
			b.append(" [accept]");
		else
			b.append(" [reject]");
		b.append(":\n");
		if(transitions==null){
			return b.toString();
		}
		for (Transition t : transitions)
			b.append("  ").append(t.toString()).append("\n");
		return b.toString();
		//return "StatePair [s1=" + s1 + ", s2=" + s2 + "]";
	}

	/** 
	 * Returns hash code.
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		if(s1==null)
			return 1;
		if(s2==null)
			return 2;
		return s1.hashCode() + s2.hashCode();
	}

	public StatePair step(CharSpan c) {
		for (Transition t : transitions)
			if (t.min <= c.min && c.max <= t.max)
				return t.to;
		return null;
	}

	public void addTransition(CharSpan edge,
			StatePair nextStackPair) {
		Transition t = new Transition(edge,nextStackPair);
		if(transitions==null){
			transitions = new HashSet<Transition>();
		}
		transitions.add(t);
	}
}
