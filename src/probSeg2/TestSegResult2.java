package probSeg2;

import java.util.ArrayDeque;
import java.util.Deque;

public class TestSegResult2 {

	public static void main(String[] args) {
		String[] bestSucWords = new String[6]; //最佳后继词 
		bestSucWords[4] = "歧";
		bestSucWords[3] = "分歧";
		bestSucWords[2] = "见";
		bestSucWords[1] = "意见";
		bestSucWords[0] = "有";
		
		Deque<String> path = new ArrayDeque<String>(); // 最佳词序列
		int i = 0;
		while (bestSucWords[i] != null) {
			System.out.println("节点"+i+"的最佳后继词:"+bestSucWords[i]);
			path.add(bestSucWords[i]); // 压栈
			i = i + bestSucWords[i].length();
		}
		
		for(String w:path){
			System.out.print(w + "/ ");
		}
	}

}
