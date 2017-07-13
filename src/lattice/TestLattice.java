package lattice;

public class TestLattice {

	public static void main(String[] args) {
		String sentence = "有意见分歧QQ23232323怎么样";
		Segmenter seg = new Segmenter();
		AdjList g = seg.getLattice(sentence);

		// 输出结果
		System.out.println(g);
	}

}
