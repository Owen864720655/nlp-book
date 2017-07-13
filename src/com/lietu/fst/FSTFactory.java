package com.lietu.fst;

import com.lietu.fst.FST;
import com.lietu.fstOperator.FSTUnion;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.BasicOperations;

/**
 * 和中文词性标注类型保持一致
 * @author luogang
 *
 */
public class FSTFactory {
	public static final String BlankType = "blank";

	//英文字符和数字,以及空格
	public static FST createSimple() throws Exception {
		Automaton num = AutomatonFactory.getNum();
		FST fstNum = new FST(num, "m"); //数值类型

		//英文单词
		Automaton lowerCase = BasicAutomata.makeCharRange('a', 'z');
		Automaton upperCase = BasicAutomata.makeCharRange('A', 'Z');
		Automaton c = BasicOperations.union(lowerCase, upperCase);
		c.determinize();
		Automaton character = c.repeat(1); //至少一次
		character.determinize(); //确定化，NFA转DFA
		character.minimize();  //最小化

		//System.out.println(character.toString());

		FST fstN = new FST(character, "n");
		FSTUnion union = new FSTUnion(fstNum, fstN);

		FST newFST = union.union();

		Automaton blank = BasicAutomata.makeChar(' ').repeat(1); //至少1次
		
		Automaton newLine = BasicAutomata.makeString("\r\n").repeat(1);
		blank = BasicOperations.union(blank, newLine);
		
		newLine = BasicAutomata.makeChar('\n').repeat(1);
		blank = BasicOperations.union(blank, newLine);
		
		blank.determinize();

		FST fstBlank = new FST(blank, BlankType);

		union = new FSTUnion(newFST, fstBlank);
		
		return union.union();
	}
	
	public static FST createDate() throws Exception {
		Automaton dateAutomaton = AutomatonFactory.getCnDate();
		FST fstDate = new FST(dateAutomaton, "t"); //时间类型
		return fstDate;
	}
	

	public static FST createNum() throws Exception {
		return null;
		
	}

	//同时匹配日期和数字
	public static FST createAll()throws Exception  {
		FST dateFST = createDate(); //日期
		FST simpleFST = createSimple(); //数值
		FSTUnion union = new FSTUnion(dateFST, simpleFST);
		return union.union();
	}
	
	//FSM 有限状态机　　FST 有限状态转换

}
