package com.lietu.automSeg;

import java.util.BitSet;

public class SplitPoints {
	public BitSet endPoints; // 可结束点
	public BitSet startPoints;// 可开始点

	public SplitPoints(int senLen) {
		endPoints = new BitSet(senLen);// 存储所有可能的切分点
		startPoints = new BitSet(senLen);// 存储所有可能的切分点
	}

	@Override
	public String toString() {
		return "SplitPoints [endPoints=" + endPoints + "\r\n startPoints="
				+ startPoints + "]";
	}
}
