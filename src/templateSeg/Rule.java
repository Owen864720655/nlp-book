package templateSeg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 从文本中解析出来的规则
 * 
 * @author luogang
 * @2013-12-27
 */
public class Rule {
	public ArrayList<String> typeSeq = new ArrayList<String>(); // 类型序列
	public HashMap<String, HashSet<String>> words = new HashMap<String, HashSet<String>>();

	public void addWord(String w, String type) {
		HashSet<String> ret = words.get(w);
		if (ret == null) {
			ret = new HashSet<String>();
			ret.add(type);
			words.put(w, ret);
		} else {
			ret.add(type);
		}
	}

	@Override
	public String toString() {
		return "Rule [类型序列=" + typeSeq + ", words=" + words + "]";
	}
}
