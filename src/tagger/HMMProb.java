package tagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

public class HMMProb {
	private static HMMProb dic = null;

	public static HMMProb getInstance() {
		if (dic == null) {
			dic = new HMMProb();
		}
		return dic;
	}

	/* 定义语料库的转移概率 */
	private int[][] transFreq = new int[PartOfSpeech.values().length][PartOfSpeech.values().length];
	// 每个词性的频次
	private int[] typeFreq = new int[PartOfSpeech.values().length];
	private int totalFreq; // 所有词的总频次

	public double getTypeProb(PartOfSpeech curState) {
		return  (double) typeFreq[curState.ordinal()];
	}

	/**
	 * 
	 * @param curState
	 *            前一个词性
	 * @param toTranState
	 *            后一个词性
	 * @return
	 */
	public double getTransProb(PartOfSpeech curState, PartOfSpeech toTranState) {
		double transValue = 0.9 * (double) transFreq[curState.ordinal()][toTranState.ordinal()] /
				(double) typeFreq[curState.ordinal()];
		double smoothValue = 0.1 * typeFreq[curState.ordinal()] / totalFreq;
		return Math.log(transValue+smoothValue);
	}

	/* 加载语料库 */
	public HMMProb(){
		try {
			URI uri = Tagger.class.getClass().getResource(
					"/tagger/POSTransFreq.txt").toURI();
			InputStream file = new FileInputStream(new File(uri));

			BufferedReader read = new BufferedReader(new InputStreamReader(
					file, "GBK"));

			String line = null;

			while ((line = read.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ":");

				int pre = PartOfSpeech.valueOf(st.nextToken()).ordinal();
				int next = PartOfSpeech.valueOf(st.nextToken()).ordinal();
				int frq = Integer.parseInt(st.nextToken());
				transFreq[pre][next] = frq;
				typeFreq[next] += frq;
				totalFreq += frq;
				if (pre == 0) {
					typeFreq[0] += frq;
				}
				// System.out.println(pre+" : "+ next + ":" +
				// transFreq[pre][next]);
			}

			// for(int i=0;i<typeFreq.length;++i){
			// System.out.println(PartOfSpeech.names[i]+" "+typeFreq[i]);
			// }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
