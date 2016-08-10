package KaiSeven;

import java.io.IOException;

public class ClusteringRun {

	public static void main(String[] args) {
		DBSCAN2d DBSCANCluster;
		String inDataPath = "";

		String currentPath = "";
		try {
			currentPath = new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			System.out.println("Error : Can't get current working directory !");
			System.exit(0);
		}

		inDataPath = currentPath + "/DataSet/clustering_test.txt";

		double eps = 3.0;
		int minpts = 8;

		DBSCANCluster = new DBSCAN2d(inDataPath, inDataPath + ".DBSCAN.eps-" + eps + ".minpts-" + minpts + ".result", eps, minpts);
		
		System.out.println("------------------------------------------------------\ndone");
	}

}
