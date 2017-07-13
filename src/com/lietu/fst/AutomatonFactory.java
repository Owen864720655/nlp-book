package com.lietu.fst;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.BasicOperations;

public class AutomatonFactory {

	/**
	 * 识别数字的有限状态机
	 * 
	 * @return
	 */
	public static Automaton getNum() {
		Automaton a = BasicAutomata.makeCharRange('0', '9');
		Automaton b = a.repeat(1);  //重复至少一次
		Automaton comma = BasicAutomata.makeChar(',');
		Automaton end = BasicOperations.concatenate(comma, a.repeat(1));
		Automaton intNum = BasicOperations.concatenate(b, end.repeat()); //数字后接逗号
		Automaton comma2 = BasicAutomata.makeChar('.');
		Automaton floatNum = BasicOperations.concatenate(comma2, a.repeat(1));
		Automaton intWithFloat = BasicOperations.concatenate(intNum,
				floatNum.optional());
		
		Automaton percent = BasicAutomata.makeChar('%');
		Automaton floatWithPercent = BasicOperations.concatenate(intWithFloat,
				percent.optional());
		
		Automaton num = BasicOperations.union(floatWithPercent, floatNum);//intWithFloat

		num.determinize();
		return num;
	}

	/**
	 * 识别大写开头的名词短语 例如 the New York Animal Medical Center made a study
	 * 
	 * @return
	 */
	public static Automaton getCapitalPhrase() {
		Automaton a = BasicAutomata.makeCharRange('A', 'Z');
		Automaton b = BasicAutomata.makeCharRange('a', 'z');
		Automaton start = BasicOperations.concatenate(a.repeat(1), b.repeat(1));
		Automaton space = BasicAutomata.makeChar(' ');
		Automaton end = BasicOperations.concatenate(space, start);
		Automaton capitalPhrase = BasicOperations.concatenate(start,
				end.repeat());
		capitalPhrase.determinize();
		return capitalPhrase;
	}
	
	//人名 例如 Aage Eriksen 或者  A. P. J. Abdul Kalam
	public static Automaton getName() {
		Automaton a = BasicAutomata.makeCharRange('A', 'Z');
		Automaton b = BasicAutomata.makeCharRange('a', 'z');
		Automaton nameWord = BasicOperations.concatenate(a.repeat(1), b.repeat(1));
		Automaton space = BasicAutomata.makeChar(' ');
		Automaton end = BasicOperations.concatenate(space, nameWord);
		
		Automaton prefix = BasicOperations.union(getNameAbbreviation(), nameWord);
		prefix.determinize();
		Automaton name = BasicOperations.concatenate(prefix, //nameWord,//
				end.repeat());
		name.determinize();
		return name;
	}
	
	public static Automaton getNameAbbreviation(){
		Automaton a = BasicAutomata.makeCharRange('A', 'Z');
		Automaton start = BasicOperations.concatenate(a,
				BasicAutomata.makeChar('.'));
		//start = start.repeat(1);
		Automaton space = BasicAutomata.makeChar(' ');
		Automaton end = BasicOperations.concatenate(space, start);
		Automaton nameAbbr = BasicOperations.concatenate(start,end.repeat());
		nameAbbr.determinize();
		return nameAbbr;
	}

	// 缩写 例如P.S.
	public static Automaton getAbbreviation() {
		Automaton a = BasicAutomata.makeCharRange('A', 'Z');
		Automaton start = BasicOperations.concatenate(a,
				BasicAutomata.makeChar('.'));
		start = start.repeat(1);
		start.determinize();
		return start;
	}
	
	//the most old-school monster mash fun
	public static Automaton getComposite() {
		Automaton a = BasicAutomata.makeCharRange('a', 'z').repeat(1);
		Automaton start = BasicOperations.concatenate(a,
				BasicAutomata.makeChar('-'));
		start = BasicOperations.concatenate(start,a);
		start.determinize();
		return start;
	}
	
	//日期,例如 Nov. 29
	public static Automaton getDate(){
		Automaton mon = BasicAutomata.makeString("Jan. ");
		mon = mon.union(BasicAutomata.makeString("Feb. "));
		mon = mon.union(BasicAutomata.makeString("Mar. "));
		mon = mon.union(BasicAutomata.makeString("Apr. "));
		mon = mon.union(BasicAutomata.makeString("Jun. "));
		mon = mon.union(BasicAutomata.makeString("Jul. "));
		mon = mon.union(BasicAutomata.makeString("Aug. "));
		mon = mon.union(BasicAutomata.makeString("Sept. "));
		mon = mon.union(BasicAutomata.makeString("Sep. "));
		mon = mon.union(BasicAutomata.makeString("Oct. "));
		mon = mon.union(BasicAutomata.makeString("Nov. "));
		mon = mon.union(BasicAutomata.makeString("Dec. "));
		
		Automaton num = BasicAutomata.makeCharRange('0', '9').repeat(1,2);
		
		Automaton suffix = BasicAutomata.makeString("rd");
		suffix = suffix.union(BasicAutomata.makeString("th"));
		suffix = suffix.union(BasicAutomata.makeString("st"));
		suffix = suffix.union(BasicAutomata.makeString("nd"));
		
		Automaton date = mon.concatenate(num).concatenate(suffix.optional());
		date.determinize();
		return date;
	}
	
	//中文日期
	public static Automaton getCnDate() {
		Automaton a = BasicAutomata.makeCharRange('0', '9');
		Automaton b = a.repeat(2,4); //数字重复2次到4次 例如，2013
		Automaton yearUnit = BasicAutomata.makeChar('年');
		Automaton yearNum = BasicOperations.concatenate(b, yearUnit); //2013年
		
		Automaton monUnit = BasicAutomata.makeChar('月');
		Automaton twoNum = a.repeat(1,2); //数字重复1次或者2次 例如，12
		Automaton monNum = BasicOperations.concatenate(twoNum, monUnit); // 例如，12月
		Automaton yearWithMon = BasicOperations.concatenate(yearNum,monNum); //monNum.optional()
		
		Automaton dayUnit = BasicAutomata.makeChar('日');
		Automaton dayNum = BasicOperations.concatenate(twoNum, dayUnit);
		Automaton yearMonDay = BasicOperations.concatenate(yearWithMon,
				dayNum.optional());
		
		Automaton finalDate = BasicOperations.union(yearNum,yearMonDay);
		finalDate.determinize(); //确定化，变成DFA
		return finalDate;
	}
	
	//时间,例如8 a.m. local time
	public static Automaton getTime(){
		Automaton hours = BasicAutomata.makeCharRange('0', '9').repeat(1,2);
		
		// 2:00 a.m. local time
		Automaton min = BasicAutomata.makeString(":").concatenate(BasicAutomata.makeCharRange('0', '9').repeat(1,2));
		
		Automaton numTime = hours.concatenate(min.optional());
		
		Automaton ampm = BasicAutomata.makeString(" a.m.").union(BasicAutomata.makeString(" p.m."));
		
		//BasicOperations.concatenate(numTime,
		numTime = numTime.concatenate(ampm.optional());
		
		Automaton time = numTime.concatenate(BasicAutomata.makeString(" local time").optional());
		time.determinize();
		return time;
	}
}
