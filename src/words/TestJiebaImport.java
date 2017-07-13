package words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TestJiebaImport {

	public static void main(String[] args) throws IOException {
		String fileName = "D:/codes/nlp/nlp_book/dict.txt"; // 文件名
		InputStream file = new FileInputStream(new File(fileName)); // 打开输入流

		// 缓存读入数据
		BufferedReader in = new BufferedReader(new InputStreamReader(file,
				"gbk"));
		String line = in.readLine();
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line);
			
			//System.out.println(line);
			String word = st.nextToken();
			System.out.println(word);
			String frq = st.nextToken();
			System.out.println(frq);
			String pos = st.nextToken();
			System.out.println(pos);
			
			line = in.readLine();
		}
		in.close();
	}

}
