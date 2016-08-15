package dataStructure.kdTree;

import java.util.List;
import java.util.Random;

import dataPattern.DataVector;

import functions.distanceFunction.DistanceEuclidean;
import functions.distanceFunction.DistanceFunction;

public class KDTree {
	private int countNode = 0;
	private KDNode root = null;
	private int countDimension = 0;

	private Random randomGen = new Random(System.currentTimeMillis());

	DistanceFunction disCalculator = new DistanceEuclidean();

	public KDTree() {

	}

	public int size() {
		return countNode;
	}

	public void buildTree(List<DataVector> dataVectors) {
		if (dataVectors.size() == 0) {
			return;
		}
		countDimension = dataVectors.get(0).size();
		
		root = generateNode(0, dataVectors, 0, dataVectors.size() - 1);
	}
	
	private KDNode generateNode(int currentD, List<DataVector> dataVectors, int left, int right) {
		return null;
	}
}
