package fmseg2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DicFileFactory implements DicFactory {
	// static final String dicDir = "./dic/";
	// public static final String binDic = "baseWords.txt";
	public static final String txtDic = "WordList.txt";
	public static final String binDic = "WordList.bin";

	@Override
	public TernarySearchTrie create() {
		TernarySearchTrie dic = new TernarySearchTrie();
		File binFile = new File(binDic);

		if (!binFile.exists()) {
			File txtFile = new File(txtDic);
			//logger.debug("加载文本文件:"+txtFile);
			loadDictionay(txtFile,dic);

			dic.compileDic(binFile);
		} else {// 从生成的数组树中进行加载
			//logger.debug("加载二进制文件:"+binFile);
			dic.loadBinaryDataFile(binFile);
		}
		
		return dic;
	}

	public void loadDictionay(File txtFile, TernarySearchTrie dic) {
		try {
			InputStream input = new FileInputStream(txtFile);
			BufferedReader read = new BufferedReader(new InputStreamReader(
					input, "GBK"));
			String line;

			while (((line = read.readLine()) != null)) {
				// System.out.println(line);
				if ("".equals(line))
					continue;

				dic.addWord(line);
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
