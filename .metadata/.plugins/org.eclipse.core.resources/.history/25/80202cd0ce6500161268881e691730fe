package functions.distanceFunction;

import dataPattern.DataVector;

public class DistanceEuclidean extends DistanceFunction {
	public static String DEFINITION = "Euclidean";

	@Override
	public double calculateDistance(DataVector v1, DataVector v2) {
		double sum = 0;
		for (int i = 0; i < v1.data.length; i++) {
			sum += Math.pow(v1.data[i] - v2.data[i], 2);
		}
		return Math.sqrt(sum);
	}

	@Override
	public double calculateDistanceSquare(DataVector v1, DataVector v2){
		double sum = 0;
		for (int i = 0; i < v1.data.length; i++) {
			sum += Math.pow(v1.data[i] - v2.data[i], 2);
		}
		return sum;
	}
	
	@Override
	public String getDefinition() {
		return DEFINITION;
	}

}
