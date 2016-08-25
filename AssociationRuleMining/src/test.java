import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

public class test {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String current = "";
		try {
			current = new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Current dir:" + current);
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current dir using System:" + currentDir);

		// boolean[] tmpB = new boolean[100];
		// for(int i=0; i<tmpB.length; i++){
		// if(tmpB[i])
		// System.out.println(i+":true");
		// else
		// System.out.println(i+":false");
		// }

		// int[] tmpI = new int[100];
		// for(int i=0; i<tmpI.length; i++){
		// System.out.println(i);
		// }
		// String inputTestString =
		// "[1.1,1.2][2.1,2.2][3.1,3.2]\n[4.1,4.2][5.1,5.2]";
		String inputTestString = "[1.1,1.2][2.1,2.2][3.1,3.2]";
		// String splitString[] = inputTestString.split("\\[|\\]");
		String splitString[] = inputTestString.split("\\[");
		for (int i = 0; i < splitString.length; i++) {
			System.out.print("i=" + i + ":");
			if (splitString[i] == "") {
				System.out.println("null");
				continue;
			}
			System.out.println(splitString[i]);
		}

		Stack<String> tmpStack = new Stack<String>();
		tmpStack.push("hello");
		tmpStack.push(null);
		while (!tmpStack.isEmpty()) {
			String tmpS = tmpStack.pop();
			System.out.println("this is " + tmpS);
		}

		System.out.println("=========================");

		HashSet<Integer> testSet = new HashSet<Integer>();
		testSet.add(1);
		testSet.add(2);
		testSet.add(3);

		HashSet<Integer> testSet2 = new HashSet<Integer>(testSet);

		testSet2.remove(1);
		for (int i : testSet) {
			System.out.print(i + "  ");
		}
		System.out.println();
		System.out.println("=========================");
		System.out.println((int) 0.5);
		System.out.println((int) 0.9);
		System.out.println((int) 1.1);

		System.out.println("=========================");

		ArrayList<String> ttarrl = new ArrayList<>();
		for (int i = 1000; i > 0; i--) {
			ttarrl.add(Integer.toString(i));
		}
		Collections.sort(ttarrl);
		for (int i = 0; i < ttarrl.size(); i++) {
			System.out.println("[" + i + "]:" + ttarrl.get(i));
		}
	}

}
