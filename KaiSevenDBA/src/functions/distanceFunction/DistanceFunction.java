package functions.distanceFunction;

import datapattern.DataVector;

public abstract class DistanceFunction {
	public abstract double calculateDistance(DataVector v1, DataVector v2);
	
	public abstract double calculateDistanceSquare(DataVector v1, DataVector v2);
	
	public abstract String getDefinition();
	
	public static DistanceFunction getDistanceFunctionByDef(String Def){
		if(Def.equals(DistanceEuclidean.DEFINITION)){
			return new DistanceEuclidean();
		}
		return null;
	}
}
