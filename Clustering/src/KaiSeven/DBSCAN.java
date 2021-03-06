package KaiSeven;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DBSCAN {
	private static boolean WRITE_TO_FILE = true;
	
	double Eps;
	double sqrtEps;
	int minPts;
	Graph graph;

	public DBSCAN(String inDataPath, String outDataPath, double eps, int minpts) {
		Scanner inputDataScanner = null;
		try {
			inputDataScanner = new Scanner(new FileInputStream(inDataPath));
		} catch (FileNotFoundException e) {
			System.out.println("input file error!");
			System.exit(0);
		}
		Eps = eps;
		sqrtEps = Math.pow(eps, 2);
		minPts = minpts;

		LinkedList<Point> tmpPointList = new LinkedList<Point>();
		StringTokenizer tokenizer;
		while (inputDataScanner.hasNext()) {
			tokenizer = new StringTokenizer(inputDataScanner.nextLine());
			double xx = Double.parseDouble(tokenizer.nextToken());
			double yy = Double.parseDouble(tokenizer.nextToken());
			tmpPointList.add(new Point(xx, yy));
		}
		graph = new Graph(tmpPointList.size());
		HashMap<Point, Integer> pointIndexMap = new HashMap<>();
		for (int i = 0; i < graph.points.length; i++) {
			graph.points[i] = tmpPointList.remove();
			pointIndexMap.put(graph.points[i], i);
		}

		for (int i = 0; i < graph.points.length; i++) {
			for (int j = 0; j < graph.points.length; j++) {
				if (Point.sqrtDistanceOf(graph.points[i], graph.points[j]) <= sqrtEps) {
					graph.points[i].neighborPoints.add(graph.points[j]);
				}
			}
		}

//		for (int i = 0; i < graph.points.length; i++) {
//			System.out.println(graph.points[i].neighborPoints.size());
//		}

		boolean[] flagVisit = new boolean[graph.points.length];
		int[] labelCluster = new int[graph.points.length];
		for (int i = 0; i < labelCluster.length; i++) {
			labelCluster[i] = 0;
		}

		int countCluster = 0;
		LinkedList<Point> visitQueue = new LinkedList<Point>();
		for (int i = 0; i < flagVisit.length; i++) {
			if (flagVisit[i] == true) {
				// System.out.println("visited");
				continue;
			}
			// System.out.println("start-visit");
			if (graph.points[i].neighborPoints.size() >= minpts + 1) {
				countCluster += 1;
				flagVisit[pointIndexMap.get(graph.points[i])] = true;
				labelCluster[pointIndexMap.get(graph.points[i])] = countCluster;
				visitQueue.clear();
				visitQueue.add(graph.points[i]);
				while (visitQueue.size() > 0) {
					// System.out.println("core");
					Point visitPoint = visitQueue.remove();
					for (Point p : visitPoint.neighborPoints) {
						if (flagVisit[pointIndexMap.get(p)])
							continue;
						flagVisit[pointIndexMap.get(p)] = true;
						labelCluster[pointIndexMap.get(p)] = countCluster;
						if (p.neighborPoints.size() >= minpts + 1) {
							visitQueue.add(p);
						}
					}
				}
			}
		}

		// for(int i=0; i<labelCluster.length;i++){
		// System.out.println(graph.points[i].neighborPoints.size());
		// }

		for (int i = 0; i < labelCluster.length; i++) {
			System.out.println(labelCluster[i] + ":(" + graph.points[i].x + "," + graph.points[i].y + ")");
		}
		
		if (WRITE_TO_FILE) {
			PrintWriter dataOutputWriter = null;
			try {
				dataOutputWriter = new PrintWriter(outDataPath, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				System.out.println("output file error!");
				System.exit(0);
			}
			for (int i = 0; i < graph.points.length; i++) {
				Point ptrPoint = graph.points[i];
				dataOutputWriter.println(ptrPoint.x + "\t" + ptrPoint.y + "\t" + labelCluster[i]);
			}
			dataOutputWriter.close();
		}
	}
}
