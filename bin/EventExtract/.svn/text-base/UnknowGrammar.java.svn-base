package com.lietu.EventExtract;

import java.util.ArrayList;

/**
 * 未登录地名识别规则
 * 
 * @author luogang
 * @2010-3-23
 */
public class UnknowGrammar {

	/**
	 * An inner class of Ternary Search Trie that represents a node in the trie.
	 */
	public final class TSTNode {

		/** The key to the node. */
		public ArrayList<DocSpan> data = null;

		/** The relative nodes. */
		protected TSTNode loKID;
		protected TSTNode eqKID;
		protected TSTNode hiKID;

		/** The char used in the split. */
		protected DocType splitchar;

		/**
		 * Constructor method.
		 * 
		 *@param splitchar
		 *            The char used in the split.
		 */
		protected TSTNode(DocType splitchar) {
			this.splitchar = splitchar;
		}

		public String toString() {
			return "splitchar:" + splitchar;
		}
	}

	/** The base node in the trie. */
	public TSTNode root;

	public void addProduct(ArrayList<DocType> key,
			ArrayList<DocSpan> lhs) {
		if (root == null) {
			root = new TSTNode(key.get(0));
		}
		TSTNode node = null;
		if (key.size() > 0 && root != null) {
			TSTNode currentNode = root;
			int charIndex = 0;
			while (true) {
				if (currentNode == null)
					break;
				int charComp = key.get(charIndex).compareTo(
						currentNode.splitchar);
				if (charComp == 0) {
					charIndex++;
					if (charIndex == key.size()) {
						node = currentNode;
						break;
					}
					currentNode = currentNode.eqKID;
				} else if (charComp < 0) {
					currentNode = currentNode.loKID;
				} else {
					currentNode = currentNode.hiKID;
				}
			}
			ArrayList<DocSpan> occur2 = null;
			if (node != null) {
				occur2 = node.data;
			}
			if (occur2 != null) {
				// occur2.insert(pi);
				return;
			}
			currentNode = getOrCreateNode(key);
			currentNode.data = lhs;
		}
	}

	public MatchRet matchLong(ArrayList<DocToken> key, int offset,
			MatchRet matchRet) {

		if (key == null || root == null || "".equals(key)
				|| offset >= key.size()) {
			matchRet.end = offset;
			matchRet.lhs = null;
			return matchRet;
		}
		int ret = offset;
		ArrayList<DocSpan> retPOS = null;

		// System.out.println("enter");
		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				// System.out.println("ret "+ret);
				matchRet.end = ret;
				matchRet.lhs = retPOS;
				return matchRet;
			}
			int charComp = key.get(charIndex).type
					.compareTo(currentNode.splitchar);

			if (charComp == 0) {
				// System.out.println("comp:"+key.get(charIndex).type);
				charIndex++;

				if (currentNode.data != null && charIndex > ret) {
					ret = charIndex;
					retPOS = currentNode.data;
					// System.out.println("ret pos:"+retPOS);
				}
				if (charIndex == key.size()) {
					matchRet.end = ret;
					matchRet.lhs = retPOS;
					return matchRet;
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				currentNode = currentNode.loKID;
			} else {
				currentNode = currentNode.hiKID;
			}
		}
	}

	public static void replace(ArrayList<DocToken> key, int offset,
			ArrayList<DocSpan> spans) {
		int j = 0;
		for (int i = offset; i < key.size(); ++i) {
			DocSpan span = spans.get(j);
			DocToken token = key.get(i);
			StringBuilder newText = new StringBuilder();
			int newStart = token.start;
			int newEnd = token.end;
			DocType newType = span.type;

			for (int k = 0; k < span.length; ++k) {
				token = key.get(i + k);
				newText.append(token.termText);
				newEnd = token.end;
			}
			DocToken newToken = new DocToken(newStart, newEnd, newText
					.toString(), newType);

			for (int k = 0; k < span.length; ++k) {
				key.remove(i);
			}
			key.add(i, newToken);
			j++;
			if (j >= spans.size()) {
				return;
			}
		}
	}

	/**
	 * Returns the node indexed by key, creating that node if it doesn't exist,
	 * and creating any required intermediate nodes if they don't exist.
	 * 
	 *@param key
	 *            A <code>String</code> that indexes the node that is returned.
	 *@return The node object indexed by key. This object is an instance of an
	 *         inner class named <code>TernarySearchTrie.TSTNode</code>.
	 *@exception NullPointerException
	 *                If the key is <code>null</code>.
	 *@exception IllegalArgumentException
	 *                If the key is an empty <code>String</code>.
	 */
	protected TSTNode getOrCreateNode(ArrayList<DocType> key)
			throws NullPointerException, IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException(
					"attempt to get or create node with null key");
		}
		if ("".equals(key)) {
			throw new IllegalArgumentException(
					"attempt to get or create node with key of zero length");
		}
		if (root == null) {
			root = new TSTNode(key.get(0));
		}
		TSTNode currentNode = root;
		int charIndex = 0;
		while (true) {
			int charComp = key.get(charIndex).compareTo(currentNode.splitchar);
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.size()) {
					return currentNode;
				}
				if (currentNode.eqKID == null) {
					currentNode.eqKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				if (currentNode.loKID == null) {
					currentNode.loKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.loKID;
			} else {
				if (currentNode.hiKID == null) {
					currentNode.hiKID = new TSTNode(key.get(charIndex));
				}
				currentNode = currentNode.hiKID;
			}
		}
	}

	public static class MatchRet {
		public int end;
		public ArrayList<DocSpan> lhs;

		public MatchRet(int e, ArrayList<DocSpan> d) {
			end = e;
			lhs = d;
		}

		public String toString() {
			return end + ":" + lhs;
		}
	}

	private UnknowGrammar() {
		
	/**
	 * 合并GuillemetStart
	 */
		ArrayList<DocSpan> lhs = new ArrayList<DocSpan>();
		ArrayList<DocType> rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
	/**
	 * 合并GuillemetEnd
	 */		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.GuillemetEnd));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.GuillemetEnd));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.GuillemetEnd));	
		addProduct(rhs, lhs);
		
	/**
	 *合并Link
	 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.Link);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.Link));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.Link);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.Link));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.Link);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.Link));	
		addProduct(rhs, lhs);
		
	/**
	 * 合并字母
	 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.English);
		rhs.add(DocType.English);
		lhs.add(new DocSpan(2, DocType.English));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.English);
		lhs.add(new DocSpan(2, DocType.English));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.English);
		rhs.add(DocType.QQPrefix);
		lhs.add(new DocSpan(2, DocType.English));	
		addProduct(rhs, lhs);
		
	/**
	 * 合并QQPrefix	
	 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.QQPrefix);
		lhs.add(new DocSpan(3, DocType.QQPrefix));	
		addProduct(rhs, lhs);
		
		
	/**
	 * 合并数字
	 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.Num));	
		addProduct(rhs, lhs);	
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();				
		rhs.add(DocType.Num);
		rhs.add(DocType.telAreaCode);
		lhs.add(new DocSpan(2, DocType.Num));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();				
		rhs.add(DocType.telAreaCode);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.Num));	
		addProduct(rhs, lhs);
		
//		lhs = new ArrayList<DocSpan>();
//		rhs = new ArrayList<DocType>();
//		rhs.add(DocType.Link);
//		rhs.add(DocType.Num);		
//		lhs.add(new DocSpan(2, DocType.Num));	
//		addProduct(rhs, lhs);
		
//		lhs = new ArrayList<DocSpan>();
//		rhs = new ArrayList<DocType>();		
//		rhs.add(DocType.GuillemetEnd);
//		rhs.add(DocType.Num);
//		lhs.add(new DocSpan(2, DocType.Num));	
//		addProduct(rhs, lhs);
		

	/**
	 * 匹配QQ
	 */
//	    QQPrefix +	qqinfo
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Num);		
		lhs.add(new DocSpan(1, DocType.QQPrefix));	
		lhs.add(new DocSpan(1, DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.QQPrefix));
		lhs.add(new DocSpan(1, DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.QQPrefix));
		lhs.add(new DocSpan(1, DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.QQPrefix));
		lhs.add(new DocSpan(1, DocType.qqinfo));
		addProduct(rhs, lhs);
			
//		QQPrefix + qqinfo + GuillemetEnd
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(1, DocType.QQPrefix));	
		lhs.add(new DocSpan(1, DocType.qqinfo));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(1, DocType.QQPrefix));	
		lhs.add(new DocSpan(1, DocType.qqinfo));
		lhs.add(new DocSpan(1, DocType.GuillemetStart));
		addProduct(rhs, lhs);
		
		rhs=new ArrayList<DocType>();
		lhs=new ArrayList<DocSpan>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		rhs=new ArrayList<DocType>();
		lhs=new ArrayList<DocSpan>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);		
		
//		Link +	QQPrefix +	Link +	qqinfo + GuillemetEnd
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Link);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(3,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);	
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Link);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(4,DocType.QQPrefix));	
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);	
		
//	    Num + GuillemetEnd + Num
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(3,DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(3,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
			
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(3,DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(5,DocType.qqinfo));
		addProduct(rhs, lhs);	
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(4,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2,DocType.QQPrefix));
		lhs.add(new DocSpan(1,DocType.qqinfo));
		addProduct(rhs, lhs);
		

//		fire
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.qqinfo);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(2,DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.qqinfo);
		rhs.add(DocType.telAreaCode);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(2,DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.qqinfo);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(3,DocType.qqinfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.qqinfo);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1,DocType.QQPrefix));
		lhs.add(new DocSpan(3,DocType.qqinfo));
		addProduct(rhs, lhs);
		
	/**
	 * 匹配移动电话号码
	 */
		
//		MobilePhonePrefix + mobiletelephone
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(1, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(3, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(1, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
	    rhs.add(DocType.GuillemetStart);
	    rhs.add(DocType.Num);
	    rhs.add(DocType.GuillemetEnd);
	    rhs.add(DocType.Num);
	    rhs.add(DocType.GuillemetEnd);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(2,DocType.MobilePhonePrefix));
	    lhs.add(new DocSpan(5,DocType.mobiletelephone));
	    addProduct(rhs, lhs);	
	    
//	 GuillemetStart + mobiletelephone + GuillemetEnd
	    
	    lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);	
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(1,DocType.GuillemetStart));
		lhs.add(new DocSpan(5,DocType.mobiletelephone));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);	
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(5,DocType.mobiletelephone));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
//      fire
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.mobiletelephone);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(2, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.MobilePhonePrefix));	
		lhs.add(new DocSpan(11, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.mobiletelephone);
		rhs.add(DocType.telAreaCode);
		lhs.add(new DocSpan(1, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(2, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.mobiletelephone);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(3, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.MobilePhonePrefix);
		rhs.add(DocType.mobiletelephone);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1, DocType.MobilePhonePrefix));		
		lhs.add(new DocSpan(3, DocType.mobiletelephone));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);	
		lhs.add(new DocSpan(5, DocType.Num));
		addProduct(rhs, lhs);
		
		
	/**
	 * 匹配固定电话	
	 */
		
//	    telAreaCode + Link + Num
		
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.telAreaCode);
	    rhs.add(DocType.Link);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(3,DocType.telephone));
	    addProduct(rhs, lhs);
	    
//	    Link + telAreaCode + Link + Num	    
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.Link);
	    rhs.add(DocType.telAreaCode);
	    rhs.add(DocType.Link);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(4,DocType.telephone));
	    addProduct(rhs, lhs);
	    
//	    fire
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.telephone);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(2,DocType.telephone));
	    addProduct(rhs, lhs);
	    
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.telephone);
	    rhs.add(DocType.GuillemetEnd);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(3,DocType.telephone));
	    addProduct(rhs, lhs);
	    
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.telephone);
	    rhs.add(DocType.Link);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(3,DocType.telephone));
	    addProduct(rhs, lhs);
	    
	    
	/**
	 * 匹配召集人
	 */
	    
		/* http://u.8264.com/activity */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.Other));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Other);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.Other));
		addProduct(rhs, lhs);
		
		/*517户外活动网*/
		/* 抓取召集人 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));		
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
	    addProduct(rhs, lhs);

	    /* www.ezeem.com */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(3, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(1, DocType.convenor));
		addProduct(rhs, lhs);		
		

		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));
		lhs.add(new DocSpan(2, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
		addProduct(rhs, lhs);	
		
		/* http://www.lvye.org/modules/lvyebb/viewforum.php */
		/* 匹配作者 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(1, DocType.CallerPrefix));	
		lhs.add(new DocSpan(1, DocType.convenor));
		//lhs.add(new DocSpan(1, DocType.Other));
		addProduct(rhs, lhs);
		
		//update 12 23
		/* 匹配作者   如：	作者	圆圆向日葵*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Other);
		rhs.add(DocType.convenor);
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(1,DocType.CallerPrefix));
		lhs.add(new DocSpan(1,DocType.Other));
		lhs.add(new DocSpan(3,DocType.convenor));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		//update 12 23
		//活动作者
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(3,DocType.GuillemetEnd));
		lhs.add(new DocSpan(2,DocType.convenor));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		//update 12 28
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.English);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(1,DocType.CallerPrefix));
		lhs.add(new DocSpan(1,DocType.convenor));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
	
		/*http://www.ifindu.cn/*/
		/*活动发布者*/
	    lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));		
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
		//update 12 23
		/*活动发布者*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2,DocType.CallerPrefix));
		lhs.add(new DocSpan(1,DocType.convenor));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
	
		//update 12 31
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);	
		lhs.add(new DocSpan(2,DocType.CallerPrefix));
		lhs.add(new DocSpan(1,DocType.convenor));
		addProduct(rhs, lhs);
		
			
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));	
		lhs.add(new DocSpan(3, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);		
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));	
		lhs.add(new DocSpan(2, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetStart));	
		addProduct(rhs, lhs);
		
		//update 12 27
		/*作者	csx398	时*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.convenor);
		rhs.add(DocType.Num);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2,DocType.GuillemetEnd));
		lhs.add(new DocSpan(2,DocType.convenor));
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		
		/* 豆瓣网 */
		/* 抓取召集人 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));		
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
	    addProduct(rhs, lhs);
	    
	    /* 抓取召集人 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));	
		addProduct(rhs, lhs);    
	    
	    /* 抓取召集人 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CallerPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.CallerPrefix));	
		lhs.add(new DocSpan(1, DocType.convenor));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
	    addProduct(rhs, lhs);
	    
	    //update 12 24
	    /* 抓取召集人  	作者	pinan728*/
	    lhs=new ArrayList<DocSpan>();
	    rhs=new ArrayList<DocType>();
	    rhs.add(DocType.CallerPrefix);
	    rhs.add(DocType.English);
	    rhs.add(DocType.Num);
	    lhs.add(new DocSpan(2,DocType.CallerPrefix));
	    lhs.add(new DocSpan(1,DocType.GuillemetEnd));
	    lhs.add(new DocSpan(2,DocType.convenor));
	    addProduct(rhs, lhs);
	    
	    //update 12 24
	    /* 抓取召集人  		作者  	sunnergao*/
	    lhs=new ArrayList<DocSpan>();
	    rhs=new ArrayList<DocType>();
	    rhs.add(DocType.CallerPrefix);
	    rhs.add(DocType.GuillemetEnd);
	    rhs.add(DocType.English);
	    lhs.add(new DocSpan(2,DocType.CallerPrefix));
	    lhs.add(new DocSpan(1,DocType.convenor));
	    addProduct(rhs, lhs);
	    
	    //update 12 31
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.CallerPrefix);
	    rhs.add(DocType.GuillemetStart);
	    rhs.add(DocType.Num);
	    rhs.add(DocType.GuillemetEnd);
	    lhs.add(new DocSpan(2,DocType.CallerPrefix));
	    lhs.add(new DocSpan(1,DocType.convenor));
	    lhs.add(new DocSpan(1,DocType.GuillemetEnd));
	    addProduct(rhs, lhs);
	    
	    
	    //update 12 24
	    /*抓取召集人  发起人           永远的高原　*/
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.CallerPrefix);
	    rhs.add(DocType.convenor);
	    rhs.add(DocType.Link);
	    rhs.add(DocType.Unknow);
	    rhs.add(DocType.GuillemetEnd);
	    lhs.add(new DocSpan(1,DocType.CallerPrefix));
	    lhs.add(new DocSpan(3,DocType.convenor));
	    lhs.add(new DocSpan(1,DocType.GuillemetEnd));
	    addProduct(rhs, lhs);
	    
	    lhs = new ArrayList<DocSpan>();
	    rhs = new ArrayList<DocType>();
	    rhs.add(DocType.CallerPrefix);
	    rhs.add(DocType.convenor);
	    rhs.add(DocType.English);
	    lhs.add(new DocSpan(1,DocType.CallerPrefix));
	    lhs.add(new DocSpan(2,DocType.convenor));
	    addProduct(rhs, lhs);
	    
	
	    
	/**
	 * 抓取活动人数
	 */
	    
	    /* http://u.tourye.com/space.php */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.AttenderPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.AttenderPrefix));		
		lhs.add(new DocSpan(1, DocType.peoplenumber));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.AttenderPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.AttenderPrefix));
		lhs.add(new DocSpan(1, DocType.peoplenumber));
		addProduct(rhs, lhs);
	
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.AttenderPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(2, DocType.AttenderPrefix));
		lhs.add(new DocSpan(1, DocType.Num));
		lhs.add(new DocSpan(1, DocType.Link));
		lhs.add(new DocSpan(1, DocType.peoplenumber));
		addProduct(rhs, lhs);
		
	
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Other);
		rhs.add(DocType.Num);
		rhs.add(DocType.AttenderSuffix);
		lhs.add(new DocSpan(1, DocType.Other));
		lhs.add(new DocSpan(1, DocType.peoplenumber));
		lhs.add(new DocSpan(1,DocType.AttenderSuffix));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.AttenderPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.AttenderSuffix);
		lhs.add(new DocSpan(2, DocType.AttenderPrefix));		
		lhs.add(new DocSpan(1, DocType.Num));	
		lhs.add(new DocSpan(1, DocType.Link));	
		lhs.add(new DocSpan(1, DocType.peoplenumber));
		lhs.add(new DocSpan(1,DocType.AttenderSuffix));
		addProduct(rhs, lhs);	
		
//		fire
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.AttenderPrefix);
		rhs.add(DocType.peoplenumber);
		rhs.add(DocType.Num);
		lhs.add(new DocSpan(1, DocType.AttenderPrefix));
		lhs.add(new DocSpan(2, DocType.peoplenumber));
		addProduct(rhs, lhs);

	
   /**
    * 匹配邮箱	
    */
		
		/* 提取邮箱地址      邮箱前缀    + GuillemetStart + 数字  + @ + 数字  + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
				
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文  + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 数字  + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文  + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文   + link + 英文    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文   + link + 英文    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
        /* 提取邮箱地址      邮箱前缀     + GuillemetStart + 数字   + link + 数字    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 数字   + link + 数字    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
        /* 提取邮箱地址      邮箱前缀     + GuillemetStart + 数字   + link + 英文    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 数字   + link + 英文    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文   + link + 数字    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + GuillemetStart + 英文   + link + 数字    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀    + 空格   + 数字  + @ + 数字  + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
				
		/* 提取邮箱地址      邮箱前缀     + 空格   + 英文  + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格    + 数字  + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格    + 英文  + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 提取邮箱地址      邮箱前缀     + 空格    + 英文   + link + 英文    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格 + 英文   + link + 英文    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
        /* 提取邮箱地址      邮箱前缀     + 空格 + 数字   + link + 数字    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格 + 数字   + link + 数字    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
        /* 提取邮箱地址      邮箱前缀     + 空格 + 数字   + link + 英文    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格 + 数字   + link + 英文    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
		/* 提取邮箱地址      邮箱前缀     + 空格 + 英文   + link + 数字    + @ + 数字   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);	
		
		/* 提取邮箱地址      邮箱前缀     + 空格   + 英文   + link + 数字    + @ + 英文   + 邮箱后缀  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);		
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);		
		
		//update 12 23
		/*【邮箱】tangmeiyan@live.cn（加我请注明目的地~不然会拒绝哦~）*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Link);
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(3,DocType.EmailPrefix));
		lhs.add(new DocSpan(4,DocType.emailInfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(3,DocType.EmailPrefix));		
		lhs.add(new DocSpan(4,DocType.emailInfo));
		addProduct(rhs, lhs);
		//update 12 23
		/*邮箱：tangmeiyan@live.cn*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Link);
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(4,DocType.EmailPrefix));
		lhs.add(new DocSpan(4,DocType.emailInfo));
		addProduct(rhs, lhs);
		
		
		//updater 12 23
		/*邮箱：xiaoxin0720@hotmail.com*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2,DocType.EmailPrefix));
		lhs.add(new DocSpan(5,DocType.emailInfo));
		addProduct(rhs, lhs);
		
		//update 12 24
		/*          yrshan888@163.com*/
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(1,DocType.GuillemetEnd));
		lhs.add(new DocSpan(5,DocType.emailInfo));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2,DocType.EmailPrefix));
		lhs.add(new DocSpan(7,DocType.emailInfo));
		addProduct(rhs, lhs);
		
		
			
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(4, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.English);
		rhs.add(DocType.Link);
		rhs.add(DocType.Num);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		/* 匹配QQ邮箱 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.EmailPrefix);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Num);
		rhs.add(DocType.Link);
		rhs.add(DocType.English);
		rhs.add(DocType.EmailMiddle);
		rhs.add(DocType.QQPrefix);
		rhs.add(DocType.EmailSuffix);
		lhs.add(new DocSpan(2, DocType.EmailPrefix));
		lhs.add(new DocSpan(6, DocType.emailInfo));
		addProduct(rhs, lhs);
		
		
		
	
	/**
	 * 提取MSN
	 */
			/* 提取MSN      MSN前缀    + GuillemetStart + 数字  + @ + 数字  + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
					
			/* 提取MSN     MSN前缀     + GuillemetStart + 英文  + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + GuillemetStart + 数字  + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN  MSN前缀     + GuillemetStart + 英文  + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 提取MSN     MSN前缀     + GuillemetStart + 英文   + link + 英文    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
			/* 提取MSN    MSN前缀     + GuillemetStart + 英文   + link + 英文    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
	        /* 提取MSN     MSN前缀     + GuillemetStart + 数字   + link + 数字    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + GuillemetStart + 数字   + link + 数字    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
	        /* 提取MSN     MSN前缀     + GuillemetStart + 数字   + link + 英文    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + GuillemetStart + 数字   + link + 英文    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
			/* 提取MSN    MSN前缀     + GuillemetStart + 英文   + link + 数字    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + GuillemetStart + 英文   + link + 数字    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀    + 空格   + 数字  + @ + 数字  + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
					
			/* 提取MSN      MSN前缀     + 空格   + 英文  + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + 空格    + 数字  + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + 空格    + 英文  + @ + 数字   +MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 提取MSN      MSN前缀     + 空格    + 英文   + link + 英文    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + 空格 + 英文   + link + 英文    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
	        /* 提取MSN      MSN前缀     + 空格 + 数字   + link + 数字    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MSN前缀     + 空格 + 数字   + link + 数字    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
	        /* 提取MSN    MSN前缀     + 空格 + 数字   + link + 英文    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN     MSN前缀     + 空格 + 数字   + link + 英文    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
			/* 提取MSN      MSN前缀     + 空格 + 英文   + link + 数字    + @ + 数字   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.Num);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);	
			
			/* 提取MSN      MNS前缀     + 空格   + 英文   + link + 数字    + @ + 英文   + MSN后缀  */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);		
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);		
			
			//update 12 23
			/*【MSN】tangmeiyan@live.cn（加我请注明目的地~不然会拒绝哦~）*/
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.Link);
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(3,DocType.MSNPrefix));		
			lhs.add(new DocSpan(4,DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(3,DocType.MSNPrefix));		
			lhs.add(new DocSpan(4,DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			//update 12 23
			/*【MSN】：tangmeiyan@live.cn*/
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.Link);
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.Link);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(4,DocType.MSNPrefix));
			lhs.add(new DocSpan(4,DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			
			//updater 12 23
			/*MSN：xiaoxin0720@hotmail.com*/
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2,DocType.MSNPrefix));
			lhs.add(new DocSpan(5,DocType.MSNInfo));
			addProduct(rhs, lhs);
			
						
				
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetStart);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(4, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));		
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.English);
			rhs.add(DocType.Link);
			rhs.add(DocType.Num);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
			
			/* 匹配QQ型MSN */
			lhs = new ArrayList<DocSpan>();
			rhs = new ArrayList<DocType>();
			rhs.add(DocType.MSNPrefix);
			rhs.add(DocType.GuillemetEnd);
			rhs.add(DocType.Num);
			rhs.add(DocType.Link);
			rhs.add(DocType.English);
			rhs.add(DocType.EmailMiddle);
			rhs.add(DocType.QQPrefix);
			rhs.add(DocType.EmailSuffix);
			lhs.add(new DocSpan(2, DocType.MSNPrefix));
			lhs.add(new DocSpan(6, DocType.MSNInfo));
			addProduct(rhs, lhs);
		
		/* 匹配城市(出发地) */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CityPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.CityPrefix));
		lhs.add(new DocSpan(1, DocType.CityInfo));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		
		/* 匹配城市(出发地) */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.CityPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.CityPrefix));
		lhs.add(new DocSpan(1, DocType.CityInfo));
		addProduct(rhs, lhs);
		
		/* 匹配详细地点，尾部带空格 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(5, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
		addProduct(rhs, lhs);	
		
		/* 匹配详细地点，尾部带空格 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Link);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(4, DocType.DetailAddressInfo));
		addProduct(rhs, lhs);	
		
		/* 匹配详细地点，尾部带空格 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetStart);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(1, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(1, DocType.GuillemetStart));
		addProduct(rhs, lhs);
		
		/* 匹配详细地点，尾部不带空格 */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(1, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(1, DocType.GuillemetEnd));
		addProduct(rhs, lhs);
		
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Link);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(7, DocType.DetailAddressInfo));
		addProduct(rhs, lhs);		
		
		/* 提取详细地址  数据格式举例：    广州集合地点：越秀区人民北路691号金信大厦B座2205室   */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.AttenderSuffix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.Num);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.English);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.Num);
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.GuillemetEnd);
		lhs.add(new DocSpan(3, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(13, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(1, DocType.DetailAddressSuffix));
		addProduct(rhs, lhs);	
		
		/* 提取详细地址  数据格式举例：   集合地点：越秀区人民北路691号金信大厦B座2205室   */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();		
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.GuillemetStart);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.AttenderSuffix);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.English);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Num);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(2, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(9, DocType.DetailAddressInfo));
		addProduct(rhs, lhs);
		
		/* 提取详细地址  数据格式举例：  【活动地点】浙江嘉善 西塘(宁静水乡) */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Link);
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.Link);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.GuillemetEnd);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Link);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.Link);
		lhs.add(new DocSpan(3, DocType.DetailAddressPrefix));
		lhs.add(new DocSpan(6, DocType.DetailAddressInfo));
		addProduct(rhs, lhs);
	
		/* 提取详细地址  数据格式举例： 25号哈尔滨集合  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();	
		rhs.add(DocType.DetailAddressUnit);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressSuffix);
		lhs.add(new DocSpan(1, DocType.DetailAddressUnit));
		lhs.add(new DocSpan(1, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(1, DocType.DetailAddressSuffix));
		addProduct(rhs, lhs);
		
		/* 提取详细地址  数据格式举例： 2月5日北京飞哈尔滨  */
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<DocType>();
		rhs.add(DocType.Time);
		rhs.add(DocType.Unknow);
		rhs.add(DocType.DetailAddressPrefix);
		rhs.add(DocType.Unknow);
		lhs.add(new DocSpan(1, DocType.Time));
		lhs.add(new DocSpan(1, DocType.DetailAddressInfo));
		lhs.add(new DocSpan(2, DocType.DetailAddressSuffix));
		lhs.add(new DocSpan(1, DocType.Unknow));
		addProduct(rhs, lhs);

	}

	private static UnknowGrammar dicGrammar = new UnknowGrammar();

	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static UnknowGrammar getInstance() {
		return dicGrammar;
	}
}
