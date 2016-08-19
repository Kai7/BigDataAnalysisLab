package algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class CutoutData {
	private static final double RATIO_DETECT_IDENTIFY_ATTR = 0.1;

	private static final boolean SHOW_ATTRIBUTE_CATEGORI = false;

	static String[][] customersInfo;
	static String[] attributes;
	static HashSet<Integer> identifyAttrs;
	static HashSet<Integer> conditionAttrs;
	static HashSet<Integer> numericAttrs;
	static HashSet<Integer> discreteAttrs;
	static String targetAttr = "member_card";
	static int targetAttrIndex = -1;

	public static void main(String[] args) throws IOException {
		String currentPath = System.getProperty("user.dir");
		String inDataPath = currentPath + "/customer.txt";

		BufferedReader breader = breader = new BufferedReader(new FileReader(inDataPath));

		String handleLine = breader.readLine();
		StringTokenizer tokenizer = new StringTokenizer(handleLine, ",\"");
		LinkedList<String> tmpAttrList = new LinkedList<String>();
		while (tokenizer.hasMoreTokens()) {
			tmpAttrList.add(tokenizer.nextToken());
		}
		attributes = new String[tmpAttrList.size()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = tmpAttrList.remove();
			if (attributes[i].equals(targetAttr))
				targetAttrIndex = i;
			// System.out.println("attributes[" + i + "] = " + attributes[i]);
		}
		// System.out.println(targetAttrIndex);

		String[] cusInfo;
		LinkedList<String[]> tmpCusInfoList = new LinkedList<String[]>();
		while ((handleLine = breader.readLine()) != null) {
			cusInfo = new String[attributes.length];
			tokenizer = new StringTokenizer(handleLine, ",\"");
			for (int i = 0; i < attributes.length; i++) {
				cusInfo[i] = tokenizer.nextToken();
			}
			tmpCusInfoList.add(cusInfo);
		}

		customersInfo = new String[tmpCusInfoList.size()][];
		for (int i = 0; i < customersInfo.length; i++) {
			customersInfo[i] = tmpCusInfoList.remove();
		}

//		for (int i = 0; i < customersInfo.length; i++) {
			// for (int i = 0; i < customersInfo.length * 0.01; i++) {
//			if (isNumeric(customersInfo[i][a])) {
//				continue;
//			}
//			System.out.println("[i:" + i + "]" + "[id:" + customersInfo[i][0] + "]" + customersInfo[i][5]);
//		}
		detectAttrFeature();

		breader.close();

	}

	private static void detectAttrFeature() {
		identifyAttrs = new HashSet<Integer>();
		conditionAttrs = new HashSet<Integer>();
		numericAttrs = new HashSet<Integer>();
		discreteAttrs = new HashSet<Integer>();

		HashSet<String> categorySet = new HashSet<String>();
		for (int i = 0; i < attributes.length; i++) {
			categorySet.clear();
			for (int j = 0; j < customersInfo.length; j++) {
				categorySet.add(customersInfo[j][i]);
			}
			System.out.println("[" + i + "]" + "[" + attributes[i] + "] ratio = " + (double) categorySet.size() / customersInfo.length);

			if ((double) categorySet.size() / customersInfo.length > RATIO_DETECT_IDENTIFY_ATTR) {
				identifyAttrs.add(i);
			} else {
				conditionAttrs.add(i);
				if (isNumeric(customersInfo[0][i])) {
					numericAttrs.add(i);
				} else {
					discreteAttrs.add(i);
				}
			}
		}

		if (SHOW_ATTRIBUTE_CATEGORI) {
			System.out.println("++ identifyAttrs ++++++++++++++++++++++++++++");
			for (int i : identifyAttrs) {
				System.out.println(attributes[i]);
			}
			System.out.println("++ conditionAttrs +++++++++++++++++++++++++++");
			for (int i : conditionAttrs) {
				System.out.println(attributes[i]);
			}
			System.out.println("++ numericAttrs +++++++++++++++++++++++++++++");
			for (int i : numericAttrs) {
				System.out.println(attributes[i]);
			}
			System.out.println("++ discreteAttrs ++++++++++++++++++++++++++++");
			for (int i : discreteAttrs) {
				System.out.println(attributes[i]);
			}
		}
	}

	private static boolean isNumeric(String s) {
		return s.matches("\\d+(\\.\\d+)?");
	}
}
