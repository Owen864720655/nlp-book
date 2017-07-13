/*
 * Created on 2004-8-26
 *
 */
package com.lietu.EventExtract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

//import probTagger.DocTypes.DocTypeInf;

//import probTagger.UnknowGrammar.MatchRet;
/**
 * 分词和标注
 */
public class DocTagger {
	public static ContextStatDoc contexStatAddress = ContextStatDoc.getInstance();
	public static DicDoc dictAddress = DicDoc.getInstance(); // 初始化词典
	public static UnknowGrammar grammar = UnknowGrammar.getInstance();
	static long minValue = Long.MIN_VALUE / 2;

	/**
	 * 计算节点i的最佳前驱节点
	 * 
	 * @param adjList 切分词图
	 * @param i 节点编号
	 */
	public static void getPrev(AdjList g, int i,DocTokenInf[] prevNode,long[] prob) {
		Iterator<DocTokenInf> it = g.getPrev(i);
		long maxFee = minValue;
		DocTokenInf maxID = null;

		// 向左查找所有候选词，得到前驱词集合，从中挑选最佳前趋词
		while (it.hasNext()) {
			DocTokenInf itr = it.next();
			long currentCost = prob[itr.start] + itr.cost;
			//currentCost += contexStatAddress.getContextPossibility(itr, i);
			if (currentCost > maxFee) {
				maxID = itr;
				maxFee = currentCost;
			}
		}
		prob[i] = maxFee;
		prevNode[i] = maxID;
		return;
	}

	/**
	 * 获得最有可能匹配的DocToken链表
	 * 
	 * @param g
	 * @param endToken
	 * @return maxProb maxProb中的所有元素都是最大概率匹配上的序列
	 */
	public static ArrayList<DocToken> maxProb(AdjList g) {
		//System.out.println(g);
		DocTokenInf[] prevNode = new DocTokenInf[g.verticesNum];
		long[] prob = new long[g.verticesNum];
		for (int index = 1; index < g.verticesNum; index++) {
			getPrev(g, index,prevNode,prob);
		}

		//for (int i = 1; i < prevNode.length; i++) {
		//	if(prevNode[i] == null)
		//		continue;
		//	System.out.println("i: "+i+"    "+prevNode[i].toString());
		//}
		
		ArrayList<DocTokenInf> ret = new ArrayList<DocTokenInf>(g.verticesNum);
		for (int i = (g.verticesNum-1); i>0; i = prevNode[i].start)// 从右向左取词候选词
		{
			ret.add(prevNode[i]);
			//System.out.println("ret "+prevNode[i]);
		}

		Collections.reverse(ret);
		mergeUnknow(ret);

		//System.out.println("ret.size() "+ret.size());
		//for (int i = 0; i < ret.size(); i++) {
		// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
		// System.out.println("data:"+ret.get(i).toString());
		// System.out.println(g.toString());
		//}

		DocType[] bestTag = hmm(ret);
		//for (int i = 0; i < bestTag.length; i++) {
			// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
		//	 System.out.println("tag:"+bestTag[i]);
			// System.out.println(g.toString());
		//}
		
		ArrayList<DocToken> list = new ArrayList<DocToken>();
		for (int i = 0; i < ret.size(); i++) {
			DocTokenInf tokenInf = ret.get(i);
			// System.out.println("maxProb:----------" + tokenInf.data.size());
			DocToken addressToken = new DocToken(tokenInf.start,tokenInf.end,tokenInf.termText,bestTag[i]);
			list.add(addressToken);
		}
		return list;
	}

	/**
	 * 
	 * @param word
	 * @return AdjListDoc 表示一个列表图
	 */
	public static AdjList getAdjList(String addressStr) {
		if (addressStr == null || addressStr.length() == 0) {
			return null;
		}

		int atomCount;

		// 存放匹配的节点信息
		// int fee;
		int start = 0;
		atomCount = addressStr.length();

		AdjList g = new AdjList(atomCount + 1); // 初始化在Dictionary中词组成的图

		while (true) // 在这里开始进行分词
		{
			ArrayList<DicDoc.MatchRet> matchRet = dictAddress.matchAll(
					addressStr, start);
			if (matchRet.size() > 0) {// 匹配上
				// fee = 100;
				for (DicDoc.MatchRet ret : matchRet) {
					String termText = addressStr.substring(start, ret.end);
					DocTokenInf tokenInf = new DocTokenInf(start,
							ret.end, termText,ret.posInf);					
					
					g.addEdge(tokenInf);
				}
				start++;
			} else { // 没匹配上
				// fee = -10;
				g.addEdge(new DocTokenInf(start, start + 1, addressStr
						.substring(start, start + 1),null));
				start++;
			}
			if (start >= atomCount) {
				break;
			}
		}

		return g;
	}

	/**
	 * 消除歧义方法
	 * 
	 * @param ret
	 *            要消除歧义的集合
	 * @return 估计的标注类型序列
	 */
	public static DocType[] hmm(ArrayList<DocTokenInf> ret) {
		DocTypes startType = new DocTypes();
		startType.put(new DocTypes.DocTypeInf(DocType.Start,1,0));
		ret.add(0, new DocTokenInf(-1,0,"Start",startType));
		
		DocTypes endType = new DocTypes();
		endType.put(new DocTypes.DocTypeInf(DocType.End,100,100));
		ret.add(new DocTokenInf(-1,0,"End",endType));
		
		//for (int i = 0; i < ret.size(); i++) {
			// Iterator<AddTypeInf> it = ret.get(i).data.iterator();
		//	 System.out.println("data:"+ret.get(i).toString());
			// System.out.println(g.toString());
		//}
		
		int stageLength = ret.size();//开始阶段和结束阶段
		int[][] prob = new int[stageLength][];//累积概率
		for(int i = 0;i<stageLength;++i)
		{
			prob[i] = new int[DocType.values().length];
		}
		
		DocType[][] bestPre = new DocType[stageLength][];//最佳前驱，也就是前一个标注是什么
		for(int i = 0;i<ret.size();++i)
		{
			bestPre[i] = new DocType[DocType.values().length];
		}
		
		prob[0][DocType.Start.ordinal()]=1;
		
		for (int stage = 1; stage < stageLength; stage++) {
			DocTokenInf nexInf = ret.get(stage);
			if (nexInf.data == null) {
				continue;
			}
			Iterator<DocTypes.DocTypeInf> nextIt = nexInf.data.iterator();
			while (nextIt.hasNext()) {
				DocTypes.DocTypeInf nextTypeInf = nextIt.next();

				DocTokenInf preInf = ret.get(stage - 1);
				if (preInf.data == null) {
					continue;
				}

				Iterator<DocTypes.DocTypeInf> preIt = preInf.data.iterator();

				while (preIt.hasNext()) {
					DocTypes.DocTypeInf preTypeInf = preIt.next();
					// 上一个结点到下一个结点的转移概率
					int trans = contexStatAddress.getContextPossibility(preTypeInf.pos,
									nextTypeInf.pos);
					int currentprob = prob[stage - 1][preTypeInf.pos.ordinal()];
					currentprob = currentprob + trans + nextTypeInf.weight;
					if (prob[stage][nextTypeInf.pos.ordinal()] <= currentprob) {
						// System.out.println("find new max:"+prob+" old:"+
						// nextTypeInf.prob);
						prob[stage][nextTypeInf.pos.ordinal()] = currentprob;
						bestPre[stage][nextTypeInf.pos.ordinal()] = preTypeInf.pos;
					}
				}
			}
		}
		
		DocType endTag = DocType.End;

		//System.out.println("stageLength "+stageLength);
		DocType[] bestTag = new DocType[stageLength];
		for(int i = (stageLength-1);i>1;i--)
		{
			//System.out.println("i-1 "+(i-1));
			bestTag[i-1]=bestPre[i][endTag.ordinal()];
			endTag = bestTag[i-1];
		}
		DocType[] resultTag = new DocType[stageLength-2];
	    System.arraycopy(bestTag, 1, resultTag, 0, resultTag.length);
	    
		ret.remove(stageLength-1);
		ret.remove(0);
		return resultTag;
	}

	public static void mergeUnknow(ArrayList<DocTokenInf> tokens) {
		// 合并未知词
		for (int i = 0; i < tokens.size(); ++i) {
			DocTokenInf token = tokens.get(i);
			if (token.data != null) {
				continue;
			}
			//System.out.println("mergeUnknow:"+token);
			StringBuilder unknowText = new StringBuilder();
			int start = token.start;
			while (true) {
				unknowText.append(token.termText);
				tokens.remove(i);
				if (i >= tokens.size()) {
					int end = token.end; // token.end

					DocTypes item = new DocTypes();
					item.put(new DocTypes.DocTypeInf(DocType.Unknow, 10, 0));
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,
							end, unknowText.toString(),item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
				token = tokens.get(i);
				if (token.data != null) {
					int end = token.start;

					DocTypes item = new DocTypes();
					item.put(new DocTypes.DocTypeInf(DocType.Unknow, 10, 0));
					DocTokenInf unKnowTokenInf = new DocTokenInf(start,
							end, unknowText.toString(),item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
			}
		}
	}

	/**
	 * 分词函数，将给定的一个名称分成若干个有意义的部分
	 * 
	 * @param word
	 * @return 一个ArrayList，其中每个元素是一个词
	 */
	public static ArrayList<DocToken> basicTag(String addressStr) // 分词
	{
		AdjList g = getAdjList(addressStr);
		ArrayList<DocToken> tokens = maxProb(g);
		return tokens;
	}

	public static ArrayList<DocToken> tag(String addressStr) // 分词
	{
		AdjList g = getAdjList(addressStr);
		//System.out.println(g);
		ArrayList<DocToken> tokens = maxProb(g);
		//增加开始和结束节点
		DocToken startToken = new DocToken(-1,0,"Start",DocType.Start);
		tokens.add(0, startToken);
		DocToken endToken = new DocToken(g.verticesNum-1,g.verticesNum,"End",DocType.End);
		tokens.add(endToken);

		//for(AddressToken tk:tokens){
		// System.out.println(tk.toString());
		//}

		// 未登录词识别
		UnknowGrammar.MatchRet matchRet = new UnknowGrammar.MatchRet(0, null);
		int offset = 0;
		while (true) {
			matchRet = grammar.matchLong(tokens, offset, matchRet);

			// System.out.println(matchRet.lhs);
			// System.out.println(matchRet.end);
			if (matchRet.lhs != null) {
				UnknowGrammar.replace(tokens, offset, matchRet.lhs);
				// System.out.println("replace:"+matchRet.lhs);
				offset = 0;
			} else {
				++offset;
				if (offset >= tokens.size())
					break;
			}
		}
		return tokens;
	}

	public static void main(String[] args) {
		String addressStr = 
			"电话：134-2755-8003";
		ArrayList<DocToken> ret =  DocTagger.tag(addressStr);
		
        for (int i =0; i <ret.size() ; ++i)
        {
        	DocToken token = ret.get(i);
        	System.out.println("token:"+token);
        }
	}
}
