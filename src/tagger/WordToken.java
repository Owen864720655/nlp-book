package tagger;

public class WordToken {
	public String termText;  //词
	public PartOfSpeech type; //词性
	public int start;  //词在文本中出现的开始位置
	public int end; //词在文本中出现的结束位置
	public long cost;

	public WordToken(PartOfSpeech typ) {
		type = typ;
	}

	public WordToken(int vertexFrom, int vertexTo, String word, PartOfSpeech typ) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		type = typ;
	}

	public WordToken(int vertexFrom, int vertexTo, long c, String word, PartOfSpeech typ) {
		start = vertexFrom;
		end = vertexTo;
		termText = word;
		cost = c;
		type = typ;
	}

	public String toString() {
		return "text:" + termText + " start:" + start + " end:" + end + " cost:" + cost + " pos:" + type;
	}
}