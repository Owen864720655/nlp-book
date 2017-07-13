package probSeg2;

public class TestAdjList {

	public static void main(String[] args) {
		String sentence="有意见分歧";
		int len = sentence.length();//字符串长度
		AdjList g = new AdjList(len+1);//存储所有被切分的可能的词

		//第一个节点结尾的边
		g.addEdge(new CnToken(0, 1, 1.0, "有"));

		//第二个节点结尾的边
		g.addEdge(new CnToken(0, 2, 1.0, "有意"));
		g.addEdge(new CnToken(1, 2, 1.0, "意"));

		//第三个节点结尾的边
		g.addEdge(new CnToken(1, 3, 1.0, "意见"));
		g.addEdge(new CnToken(2, 3, 1.0, "见"));

		//第四个节点结尾的边
		g.addEdge(new CnToken(3, 4, 1.0, "分"));

		//第五个节点结尾的边
		g.addEdge(new CnToken(3, 5, 1.0, "分岐"));

		System.out.println(g.toString());
		
		for(CnToken t:g){
			System.out.println(t);
		}
	}

}
