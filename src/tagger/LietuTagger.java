package tagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import tagger.WordTypes.WordTypeInf;

public class LietuTagger {
	public static TernarySearchTrie  dic = null;
	public static HMMProb hmmProb = null;
	public static double minValue = Double.NEGATIVE_INFINITY;

	public ArrayList<WordToken> split(String input) throws IOException {
		hmmProb = HMMProb.getInstance();
		//dic = Dictionary.getInstance();
		DicFactory dicFactory = // new DicFileFactory();
				new DicDBFactory();
				dic = dicFactory.create();

		// 都到输入词的邻接矩阵的切分图
		AdjList g = getAdjList(input);
		
		// 最大概率分词并且标注词性
		return maxProb(g);
	}

	public AdjList getAdjList(String input) {
		if (input == null || input.length() == 0) {
			return null;
		}
	
		// 存放匹配的节点信息
		int start = 0;
		int atomCount = input.length();
		AdjList g = new AdjList(atomCount + 1); // 初始化在Dictionary中词组成的图

		while (true) { // 在这里开始进行分词
			TernarySearchTrie.PrefixRet prefix = new TernarySearchTrie.PrefixRet();
			boolean matchRet = dic.matchAll(input, start,prefix);
			if (matchRet) { // 匹配上
				for (WordEntry word : prefix.values) {
					int end = start + word.termText.length();// 词的结束位置
					//String termText = input.substring(start, ret.end);
					WordTokenInf tokenInf = new WordTokenInf(start, end, word.termText, word.types);
					g.addEdge(tokenInf);
				}
				start++;
			} else { // 没匹配上
				g.addEdge(new WordTokenInf(start, start + 1, input.substring(start, start + 1), null));
				start++;
			}
			if (start >= atomCount) {
				break;
			}
		}

		return g;
	}

	/**
	 * @param g
	 *            邻接矩阵的切分图
	 * @return 分词之后标注了词性的WokenToken序列
	 */
	public ArrayList<WordToken> maxProb(AdjList g) {
		WordTokenInf[] prevNode = new WordTokenInf[g.verticesNum];
		double[] prob = new double[g.verticesNum];
		for (int index = 1; index < g.verticesNum; index++) {
			getPrev(g, index, prevNode, prob);
		}
		
		// 逆向查找候选词
		ArrayList<WordTokenInf> ret = new ArrayList<WordTokenInf>(g.verticesNum);
		for (int i = (g.verticesNum - 1); i > 0; i = prevNode[i].start) {
			ret.add(prevNode[i]);
		}

		Collections.reverse(ret);
		mergeUnknow(ret);

		PartOfSpeech[] bestTag = hmm(ret);

		ArrayList<WordToken> list = new ArrayList<WordToken>();
		for (int i = 0; i < ret.size(); i++) {
			WordTokenInf tokenInf = ret.get(i);
			
			WordToken addressToken = new WordToken(tokenInf.start, tokenInf.end, tokenInf.cost, tokenInf.termText, bestTag[i]);
			list.add(addressToken);
		}
		return list;
	}

	/**
	 * 采用动态规划方法计算节点i的最佳前驱节点
	 * 
	 * @param adjList
	 *            切分词图
	 * @param i
	 *            节点编号
	 */
	public static void getPrev(AdjList g, int i, WordTokenInf[] prevNode, double[] prob) {
		Iterator<WordTokenInf> it = g.getPrev(i);
		double maxCost = minValue;
		
		WordTokenInf maxToken = null;

		// 正向查找所有候选词，得到前驱词集合，从中挑选最佳前趋词
		while (it.hasNext()) {
			WordTokenInf currenToken = it.next();
			/* log(前驱最佳概率) + log(当前token的概率) */
			double sum = prob[currenToken.start] + Math.log((double) currenToken.cost / (double) dic.n);
			if (sum > maxCost) {
				maxToken = currenToken;
				maxCost = sum;
			}
		}
		prob[i] = maxCost;
		prevNode[i] = maxToken;
	}

	/**
	 * 将连续的未知的单字合并标注成unknown类型
	 * 
	 * @param tokens
	 *            已经分好的词的token链表
	 */
	public void mergeUnknow(ArrayList<WordTokenInf> tokens) {
		for (int i = 0; i < tokens.size(); ++i) {
			WordTokenInf token = tokens.get(i);
			if (token.data != null) {
				continue;
			}
			StringBuilder unknowText = new StringBuilder();
			int start = token.start;
			while (true) {
				unknowText.append(token.termText);
				tokens.remove(i);
				if (i >= tokens.size()) {
					int end = token.end; // token.end
					WordTypes item = new WordTypes();
					item.put(new WordTypeInf(PartOfSpeech.unknown, Constant.UNKOWN_WORD_SMOOTH_VALUE)); // 这块
					WordTokenInf unKnowTokenInf = new WordTokenInf(start, end, unknowText.toString(), item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
				token = tokens.get(i);
				if (token.data != null) {
					int end = token.start;
					WordTypes item = new WordTypes();
					item.put(new WordTypeInf(PartOfSpeech.unknown, Constant.UNKOWN_WORD_SMOOTH_VALUE));
					WordTokenInf unKnowTokenInf = new WordTokenInf(start, end, unknowText.toString(), item);
					tokens.add(i, unKnowTokenInf);
					break;
				}
			}
		}
	}

	/**
	 * 标注估计词性(隐马尔科夫模型,viterbi算法)
	 * 
	 * @param ret
	 *            要标注词性的结合
	 * @return 估计的标注类型序列
	 */
	public static PartOfSpeech[] hmm(ArrayList<WordTokenInf> ret) {
		// 增加start和end节点
		WordTypes startType = new WordTypes();
		startType.put(new WordTypeInf(PartOfSpeech.start, 1));
		ret.add(0, new WordTokenInf(-1, 0, "Start", startType));

		WordTypes endType = new WordTypes();
		endType.put(new WordTypeInf(PartOfSpeech.end, 100));
		ret.add(new WordTokenInf(-1, 0, "End", endType));

		// 初始化各个节点的隐状态,每个节点都有WordTypes.values().length种隐状态
		// 形成了一个row=stageLength,line=WordTypes.values().length的二维数组
		int stageLength = ret.size();
		double[][] prob = new double[stageLength][];
		for (int i = 0; i < stageLength; ++i) {
			prob[i] = new double[PartOfSpeech.values().length];
			for (int j = 0; j < PartOfSpeech.values().length; ++j)
				prob[i][j] = Double.NEGATIVE_INFINITY;
		}

		// 在隐士马尔科夫模型中,每个隐状态都有一个最佳前驱,此二维数组用来储存每个状态的最佳前驱
		PartOfSpeech[][] bestPre = new PartOfSpeech[stageLength][];
		for (int i = 0; i < ret.size(); ++i) {
			bestPre[i] = new PartOfSpeech[PartOfSpeech.values().length];
		}

		prob[0][PartOfSpeech.start.ordinal()] = 1;

		// viterbi算法计算最佳前驱
		for (int stage = 1; stage < stageLength; stage++) {
			WordTokenInf nexInf = ret.get(stage);
			if (nexInf.data == null) {
				continue;
			}
			Iterator<WordTypeInf> nextIt = nexInf.data.iterator();
			while (nextIt.hasNext()) {
				WordTypeInf nextTypeInf = nextIt.next();

				WordTokenInf preInf = ret.get(stage - 1);
				if (preInf.data == null) {
					continue;
				}

				Iterator<WordTypeInf> preIt = preInf.data.iterator();
				double emiprob = Math.log((double) nextTypeInf.weight / hmmProb.getTypeProb(nextTypeInf.pos) ); // 发射概率

				while (preIt.hasNext()) {
					WordTypeInf preTypeInf = preIt.next();
					double transprob = hmmProb.getTransProb(preTypeInf.pos, nextTypeInf.pos); // 转移概率的对数
					double preProb = prob[stage - 1][preTypeInf.pos.ordinal()]; // 前驱最佳概率
					/* log(前驱最佳概率) + log(发射概率) + log(转移概率) */
					double currentprob = preProb + transprob + emiprob;
					if (prob[stage][nextTypeInf.pos.ordinal()] <= currentprob) { // 计算最佳前驱
						prob[stage][nextTypeInf.pos.ordinal()] = currentprob;
						bestPre[stage][nextTypeInf.pos.ordinal()] = preTypeInf.pos;
					}
				}
			}
		}

		// 逆向求解路径
		PartOfSpeech tmpTag = PartOfSpeech.end;
		PartOfSpeech[] bestTag = new PartOfSpeech[stageLength];
		for (int i = (stageLength - 1); i > 1; i--) {
			bestTag[i - 1] = bestPre[i][tmpTag.ordinal()];
			tmpTag = bestTag[i - 1];
		}

		// 构造返回结果
		PartOfSpeech[] resultTag = new PartOfSpeech[stageLength - 2];
		System.arraycopy(bestTag, 1, resultTag, 0, resultTag.length);

		// 将ret之前添加的start和end节点去除
		ret.remove(stageLength - 1);
		ret.remove(0);

		return resultTag;
	}
}
