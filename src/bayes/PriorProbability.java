package bayes;

/**
* 先验概率计算
* <h3>先验概率计算</h3>
* P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
* 其中，N(C=c<sub>j</sub>)表示类别c<sub>j</sub>中的训练文本数量；
* N表示训练文本集总数量。
*/

public class PriorProbability 
{
	private static TrainingData trainingData=TrainingData.getInstance();//训练集管理器

	/**
	* 先验概率
	* @param c 给定的分类
	* @return 给定条件下的先验概率
	*/
	public static float calculatePc(String c)
	{
		float Nc = trainingData.getClassDocNum(c);//给定分类的训练文本数
		float N = trainingData.getTotalNum();//训练集中文本总数
		return (Nc / N);
	}
}