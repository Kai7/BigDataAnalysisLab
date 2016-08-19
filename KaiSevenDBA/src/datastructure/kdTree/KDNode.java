package datastructure.kdTree;

import datapattern.DataVector;

public class KDNode {
	DataVector vector;
	int splitD;
	KDNode left;
	KDNode right;
	
	public KDNode(DataVector v, int d) {
		this.vector = v;
		this.splitD = d;
	}
	
}
