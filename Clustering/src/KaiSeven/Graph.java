package KaiSeven;

public class Graph {
	public Point[] points;
	
	public Graph(int size) {
		points = new Point[size];
	}
	
	public Graph(Point[] pts){
		points = pts;
	}
}
