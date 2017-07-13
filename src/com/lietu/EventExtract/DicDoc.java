/*
 * Created on 2004-9-12
 *
 */
package com.lietu.EventExtract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * basic dictionary
 */
public class DicDoc
{
	private Properties propertie;
	private InputStream inputFile;
	private String dicDir;
	private DicDoc()
	{
			
		propertie = new Properties();
		try {
			inputFile = this.getClass().getClassLoader().getResourceAsStream(
					"conf.properties");
			propertie.load(inputFile);
			dicDir = propertie.getProperty("extractor");
		} catch (FileNotFoundException ex) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("装载文件--->失败!");
			ex.printStackTrace();
		} finally {
			try {
				inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

			//addWord(" ", 0, DocType.Link, 10);
			//addWord("　", 0, DocType.Other, 10);
			//addWord("“", 0, DocType.GuillemetStart, 1000);
			//addWord("”", 0, DocType.GuillemetEnd, 1000);
			addWord(":", 0, DocType.GuillemetStart, 1000);
			addWord("：", 0, DocType.GuillemetStart, 1000);
			addWord("： ", 0, DocType.GuillemetStart, 1000);
			addWord("\n\r", 0, DocType.GuillemetStart, 1000);
			addWord("\r\n", 0, DocType.GuillemetStart, 1000);
			addWord("\n", 0, DocType.GuillemetStart, 1000);
			addWord("\r", 0, DocType.GuillemetStart, 1000);
			addWord("", 0, DocType.GuillemetStart, 1000);
			
			//addWord(" ", 0, DocType.GuillemetStart, 1000);
			//addWord("，", 0, DocType.GuillemetStart, 1000);
			//addWord(",", 0, DocType.GuillemetStart, 1000);
			//addWord("《", 0, DocType.GuillemetStart, 1000);
			addWord("	", 0, DocType.GuillemetEnd, 1000);
			addWord(" ", 0, DocType.GuillemetEnd, 1000);
			addWord("!", 0, DocType.GuillemetEnd, 1000);
			addWord("。", 0, DocType.GuillemetEnd, 1000);
			         
			addWord("", 0, DocType.GuillemetEnd, 1000);
			addWord(" ;", 0, DocType.GuillemetEnd, 1000);
			// 加载字典
			load("link.txt", DocType.Link);
			load("other.txt", DocType.Other);		
			
			/* QQ前缀   */
			load("qq_prefix.txt", DocType.QQPrefix);
			
			/* QQ后缀 */
			load("qq_suffix.txt", DocType.QQSuffix);
			
			/* 移动电话号码前缀  */
			load("mobile_prefix.txt", DocType.MobilePhonePrefix);
			
			/* 移动电话号码后缀 */
			load("mobile_suffix.txt", DocType.MobilePhoneSuffix);
			
			/*固定电话区位码*/
			load("telcode.txt", DocType.telAreaCode);
			
			/*固定电话前缀*/
			load("tel_prefix.txt", DocType.telphonePrefix);
			
			/* 召集人前缀 */
			load("caller_prefix.txt", DocType.CallerPrefix);
			
			/* 召集人后缀 */
			load("caller_suffix.txt", DocType.CallerSuffix);
			
			/* 参与人数前缀 */
			load("attender_prefix.txt", DocType.AttenderPrefix);
			
			/* 参与人数后缀 */
			load("attender_suffix.txt", DocType.AttenderSuffix);
			
			load("number.txt", DocType.Num); 
			
			/* email前缀 */
			load("email_prefix.txt", DocType.EmailPrefix); 
					
			/* 匹配email中间表示 */
			load("email_middle.txt", DocType.EmailMiddle);
			
			/* email后缀 */
			load("email_suffix.txt", DocType.EmailSuffix);
			
			/*msn前缀*/
			load("msn_prefix.txt", DocType.MSNPrefix);		
			
			/* 出发城市前缀 */
			load("city_prefix.txt", DocType.CityPrefix);
			
			/* 出发城市后缀 */
			load("city_suffix.txt", DocType.CitySuffix);
			
			/* 详细地址前缀 */
			load("detailaddress_prefix.txt", DocType.DetailAddressPrefix);
			
			/* 详细地址后缀 */
			load("detailaddress_suffix.txt", DocType.DetailAddressSuffix);
			
			/* 匹配英文字母 */
			load("english.txt", DocType.English);
			
			/* 去除详细地址中的垃圾信息 */
			load("detailaddress_other.txt", DocType.DetailAddressOther);
			
			load("time.txt", DocType.Time);
			
			load("detailaddressunit.txt", DocType.DetailAddressUnit);
	}
	
	
	/**
	 *  An inner class of Ternary Search Trie that represents a node in the trie.
	 */
	public final class TSTNode {
		/** The key to the node. */
		public DocTypes data=null;
		
		/** The relative nodes. */
		protected TSTNode loKID;
		protected TSTNode eqKID;
		protected TSTNode hiKID;

		/** The char used in the split. */
		protected char splitchar;

		/**
		 *  Constructor method.
		 *
		 *@param  splitchar  The char used in the split.
		 */
		protected TSTNode(char splitchar) {
			this.splitchar = splitchar;
		}
		
		public String toString()
		{
			return "splitchar:"+ splitchar;
		}
	}
	
	/** The base node in the trie. */
	public TSTNode root;

	/**
	 *  Constructs a Ternary Search Trie and loads data from a <code>File</code> into the Trie. 
	 *  The file is a normal text document, where each line is of the form
	 *  word : integer.
	 *
	 *@param  file             The <code>File</code> with the data to load into the Trie.
	 *@exception  IOException  A problem occured while reading the data.
	 */
	public void load(String dic,DocType type,int weight){
		
		String line = null;
		long code = 0;
		
		try{
			InputStream file = null;
			if (System.getProperty("dic.dir") == null)
			{
				file = getClass().getResourceAsStream(dicDir+dic);
			}
			else
				file = new FileInputStream(new File(dicDir+dic));
			
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(file,"GBK"));
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,":");
				String key = "";
				if (st.hasMoreTokens()){
					key = st.nextToken();
				}
				if (st.hasMoreTokens()){
					try{
						code = Long.parseLong(st.nextToken());
						//System.out.println(code);
					}catch (NumberFormatException e){
						code=0;
					}
				}
				addWord(key,code,type,weight);
			}
			in.close();
		}catch( IOException e)
		{
			System.out.println("not find dictionary file:"+dic);
		}
		catch(java.util.NoSuchElementException e)
		{
			System.out.println("format error:"+line);
			//System.exit(-1);
		}
	}
	
	public void load(String dic, DocType type) 
	{
		String line = null;
		int weight = 0;
		
		try {
			InputStream	file = new FileInputStream(new File(dicDir + dic));
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(file, "gbk"));
			while ((line = in.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, ":");
				String key = "";
				if (st.hasMoreTokens()) {
					key = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					try {
						weight = Integer.parseInt(st.nextToken());
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}				
				addWord(key, 0l, type, weight);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("not find dictionary file:" + dic);
		} catch (java.util.NoSuchElementException e) {
			System.out.println("format error:" + line);
		}
	}

	//添加内容
	private void addWord(String key,long code,DocType type,int w)
	{
		if("".equals(key))
		{
			return;
		}
		if (root == null) {
			root = new TSTNode(key.charAt(0));
		}
		
		DocTypes.DocTypeInf pi = new DocTypes.DocTypeInf(type,w,code);
		
		TSTNode node = null;
		if (key.length() > 0 && root != null) {
			TSTNode currentNode = root;
			int charIndex = 0;
			while (true) {
				if (currentNode == null)
					break;
				int charComp =							
						(key.charAt(charIndex)-
						currentNode.splitchar);
				if (charComp == 0) {
					charIndex++;
					if (charIndex == key.length()) {
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
			DocTypes occur2 = null;
			if (node != null)
			{
				occur2 = node.data;
			}
			if (occur2 != null) {			
				occur2.insert(pi);
				return;
			}
			currentNode =
				getOrCreateNode(key);
			//currentNode.data = occur2;

			DocTypes occur3 = currentNode.data;
			if (occur3 != null ) {
				occur3.insert(pi);
			}
			else
			{
				DocTypes occur = new DocTypes();
				occur.put(pi);
				currentNode.data = occur;
			}
		}
	}
	
	public static int matchEnglish (int start, String sentence)
	{
		int i = start;
		int count = sentence.length();
		String sd="ＱＷＥＲＴＹＵＩＯＰＡＳＤＦＧＨＪＫＬＺＸＣＶＢＮＭ";
		while(i<count)
		{
			char c = sentence.charAt(i);
			if( c>='a'&&c<='z' || c>='A'&&c<='Z' )
			{
				++i;
			}else if(sd.indexOf(c)>-1)
			{
				++i;
			}
			else
			{
				break;
			}
		}
		
		return i;
	}
	
	public static int matchNum (int start, String sentence)
	{
		int i = start;
		int count = sentence.length();
		while(i<count)
		{
			char c = sentence.charAt(i);
			if( (c>='0'&&c<='9')
					||(c >= '０' && c <= '９')|| c=='-' || c=='－' )
			{
				++i;
			}
			else
			{
				break;
			}
		}
		
		if(i>start && i<count)
		{
			char end = sentence.charAt(i);
			if('号'==end || '#'==end||'＃'==end)
			{
				i++;
			}
		}

	//	System.out.println(i+" "+sentence.substring(start));
		return i;
	}

	public ArrayList<MatchRet> matchAll(String key,int offset) {
		ArrayList<MatchRet> matchRets = new ArrayList<MatchRet>();
		if (key == null || root == null || "".equals(key) || offset>= key.length()) {
			return matchRets;
		}
		//System.out.println(key);
		int ret = offset;
	//	DocType retPOS = null;
	//	int retWeight = 0;
		
		/*ret = matchNum(offset,key);
		if(ret>offset)
		{
			retPOS = DocType.No;
			retWeight = 100;
			
			DocTypes.DocTypeInf posInf = new DocTypes.DocTypeInf(retPOS,retWeight,0);
			DocTypes addressData = new DocTypes();
			addressData.put(posInf);
			MatchRet matchRet = new MatchRet(ret,addressData);
			matchRets.add(matchRet);
		}*/
		
		/*int retEnglishNum = matchEnglish(offset,key);
		if(retEnglishNum>ret)
		{
			//matchRet.end = ret;
			//matchRet.pos = AddressType.No;
			//return matchRet;
			ret = retEnglishNum;
			retPOS = DocType.Symbol;
			retWeight = 100;
			DocTypes.DocTypeInf posInf = new DocTypes.DocTypeInf(retPOS,retWeight,0);
			DocTypes addressData = new DocTypes();
			addressData.put(posInf);
			MatchRet matchRet = new MatchRet(ret,addressData);
			matchRets.add(matchRet);
		}*/
		
		TSTNode currentNode = root;
		int charIndex = offset;
		while (true) {
			if (currentNode == null) {
				//System.out.println("ret "+ret);
//				matchRet.end = ret;
//				matchRet.code = code;
//				matchRet.pos = retPOS;
//				matchRet.weight = retWeight;
				
				return matchRets;
			}
			int charComp = key.charAt(charIndex) - currentNode.splitchar;
			
			if (charComp == 0) {
				charIndex++;
				
				if(currentNode.data != null) {
					ret = charIndex;
					//code = currentNode.code;
					//retPOS = currentNode.type;
					//retWeight = currentNode.weight;
					MatchRet matchRet = new MatchRet(ret,currentNode.data);
					matchRets.add(matchRet);
					//System.out.println("ret pos:"+retPOS);
				}
				if (charIndex == key.length()) {
					return matchRets;
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				currentNode = currentNode.loKID;
			} else {
				currentNode = currentNode.hiKID;
			}
		}
	}
		
	/**
	 *  Returns the node indexed by key, creating that node if it doesn't exist,
	 *  and creating any required intermediate nodes if they don't exist.
	 *
	 *@param  key                           A <code>String</code> that indexes the node that is returned.
	 *@return                                  The node object indexed by key. This object is an
	 *                                               instance of an inner class named <code>TernarySearchTrie.TSTNode</code>.
	 *@exception  NullPointerException      If the key is <code>null</code>.
	 *@exception  IllegalArgumentException  If the key is an empty <code>String</code>.
	 */
	protected TSTNode getOrCreateNode(String key)
		throws NullPointerException, IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException("attempt to get or create node with null key");
		}
		if ("".equals(key)) {
			throw new IllegalArgumentException("attempt to get or create node with key of zero length");
		}
		if (root == null) {
			root = new TSTNode(key.charAt(0));
		}
		TSTNode currentNode = root;
		int charIndex = 0;
		while (true) {
			int charComp =(
					key.charAt(charIndex) -
					currentNode.splitchar);
			if (charComp == 0) {
				charIndex++;
				if (charIndex == key.length()) {
					return currentNode;
				}
				if (currentNode.eqKID == null) {
					currentNode.eqKID =
						new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.eqKID;
			} else if (charComp < 0) {
				if (currentNode.loKID == null) {
					currentNode.loKID =
						new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.loKID;
			} else {
				if (currentNode.hiKID == null) {
					currentNode.hiKID =
						new TSTNode(key.charAt(charIndex));
				}
				currentNode = currentNode.hiKID;
			}
		}
	}

	public static class MatchRet {
		public int end;
		public DocTypes posInf;
		public MatchRet(int e,DocTypes d)
		{
			end = e;
			posInf = d;
		}
	
		public String toString()
		{
			return "endPosition:"+end+ " posInf:"+posInf;
		}
	}
	
	private static DicDoc dicCore = new DicDoc();

	/**
	 * 
	 * @return the singleton of basic dictionary
	 */
	public static DicDoc getInstance()
	{
		return dicCore;
	}
	
	public static void reLoad()
	{
		dicCore = null;
		dicCore = new DicDoc();
	}
	
}
