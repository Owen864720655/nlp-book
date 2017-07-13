package trigramSeg;

/**
 * @author Administer
 * @see
 * @2013-8-6
 */
public class TestAdjList {

	public static void main(String[] args) {
		String sentence = //"有意见分歧";
			"中国成立了";
		Segmenter seg = new Segmenter(sentence);
		AdjList g = seg.getWordGraph(sentence);

		System.out.println(g.toString());
		for (Node currentNode : g) {
			System.out.println(currentNode);
		}

		Node n = // new Node(1,2,3);
		//new Node(2, 3, 5);
			new Node(5, 6, 7,0.0);
		Node[] nodes = g.prevNodeSet(n);

		for (Node preNode : nodes) {
			System.out.println(preNode);
		}

	}

}
