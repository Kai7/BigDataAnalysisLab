package datapattern;

public class DataVector {
	public double[] data;

	public DataVector(double[] data) {
		this.data = data;
	}

	public String toString() {
		if (data.length == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(data[0]);
		for (int i = 1; i < data.length; i++) {
			buffer.append("," + data[i]);
		}
		return buffer.toString();
	}
	
	public DataVector clone(){
		return new DataVector(data.clone());
	}

	public int size(){
		return data.length;
	}
	
	public double getValueOf(int index){
		return data[index];
	}
}
