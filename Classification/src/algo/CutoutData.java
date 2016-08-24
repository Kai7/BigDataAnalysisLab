package algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import ds.DecisionTree;
import ds.DecisionTreeOld;
import ds.DecisionTreeV2;

public class CutoutData {
	private static final double RATIO_TRAIN_DATA = 0.7;
	private static final double RATIO_DETECT_IDENTIFY_ATTR = 0.01;
	private static final double RATIO_NUMERIC_AS_DISCRETE = 0.005;

	private static final boolean SHOW_ATTRIBUTE_CATEGORI = true;
	private static final boolean SHOW_ATTRIBUTE_IN_PREDICT = false;
	private static final boolean SHOW_TARGET_ATTRIBUTE_VALUE = true;

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

		// for (int i = 0; i < customersInfo.length; i++) {
		// for (int i = 0; i < customersInfo.length * 0.01; i++) {
		// if (isNumeric(customersInfo[i][a])) {
		// continue;
		// }
		// System.out.println("[i:" + i + "]" + "[id:" + customersInfo[i][0] +
		// "]" + customersInfo[i][5]);
		// }

		detectAttrFeature();

		HashSet<Integer> trainInfo = new HashSet<Integer>();
		HashSet<Integer> testInfo = new HashSet<Integer>();

		cutupTrainAndTest(trainInfo, testInfo);

		// DecisionTree dtree = new DecisionTree(attributes,
		// targetAttrIndex,discreteAttrs, numericAttrs);
		// dtree.buildTree(customersInfo, trainInfo);

		// DecisionTreeOld dtree = new DecisionTreeOld();
		// dtree.buildTree(customersInfo, trainInfo, attributes, discreteAttrs,
		// numericAttrs, targetAttrIndex);

		showAttrValueOf(13);
		
		DecisionTreeV2 dtree = new DecisionTreeV2(attributes, targetAttrIndex, discreteAttrs, numericAttrs);
		dtree.buildTree(customersInfo, trainInfo);
		
		System.out.println("Building Decision Tree Complete!");

		int countCurrent = 0;
		int countUnknow = 0;
		for (int i : testInfo) {
			HashMap<String, String> mapAttrValue = new HashMap<String, String>();
			if (SHOW_ATTRIBUTE_IN_PREDICT) {
				System.out.println("== predict start ==========================");
			}
			for (int j = 0; j < attributes.length; j++) {
				mapAttrValue.put(attributes[j], customersInfo[i][j]);
				if (SHOW_ATTRIBUTE_IN_PREDICT) {
					System.out.println(attributes[j] + ":" + customersInfo[i][j]);
				}
			}
			String predictResult = dtree.predict(mapAttrValue);
			if (SHOW_ATTRIBUTE_IN_PREDICT) {
				System.out.println("-------------------------------------------");
				System.out.println(attributes[targetAttrIndex] + ":" + customersInfo[i][targetAttrIndex]);
				System.out.println("predict result:" + predictResult);
			}
			if (predictResult.equals(customersInfo[i][targetAttrIndex])) {
				countCurrent++;
			}
			if (predictResult.equals("unknow")) {
				countUnknow++;
			}
		}

		System.out.println();
		System.out.println("== Report =================================");
		System.out.println("test data size : " + testInfo.size());
		System.out.println("predict success : " + countCurrent);
		System.out.println("current rate : " + (double) countCurrent / testInfo.size());
		System.out.println("-------------------------------------------");
		System.out.println("unknow : " + countUnknow);
		System.out.println("unknow rate : " + (double) countUnknow / testInfo.size());
		System.out.println("===========================================");
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
			double ratioCategory = (double) categorySet.size() / customersInfo.length;
			System.out.println("[" + i + "]" + "[" + attributes[i] + "] ratio = " + ratioCategory);

			if (ratioCategory > RATIO_DETECT_IDENTIFY_ATTR) {
				identifyAttrs.add(i);
			} else if (attributes[i].equals(targetAttr)) {
				targetAttrIndex = i;
			} else {
				conditionAttrs.add(i);
				if (isNumeric(customersInfo[0][i]) && ratioCategory > RATIO_NUMERIC_AS_DISCRETE) {
					numericAttrs.add(i);
				} else {
					discreteAttrs.add(i);
				}
			}
		}

		if (SHOW_ATTRIBUTE_CATEGORI) {
			System.out.println("++ identifyAttrs ++++++++++++++++++++++++++++");
			for (int i : identifyAttrs) {
				System.out.println("[" + i + "][" + attributes[i] + "]");
			}
			// System.out.println("++ conditionAttrs +++++++++++++++++++++++++++");
			// for (int i : conditionAttrs) {
			// System.out.println("[" + i + "][" + attributes[i] + "]");
			// }
			System.out.println("++ numericAttrs +++++++++++++++++++++++++++++");
			for (int i : numericAttrs) {
				System.out.println("[" + i + "][" + attributes[i] + "]");
			}
			System.out.println("++ discreteAttrs ++++++++++++++++++++++++++++");
			for (int i : discreteAttrs) {
				System.out.println("[" + i + "][" + attributes[i] + "]");
			}
			System.out.println("++ targetAttrs ++++++++++++++++++++++++++++++");
			System.out.println("[" + targetAttrIndex + "][" + attributes[targetAttrIndex] + "]");
		}
	}

	private static void cutupTrainAndTest(HashSet<Integer> trainInfo, HashSet<Integer> testInfo) {
		HashMap<String, ArrayList<Integer>> mapSubInfoByResult = new HashMap<String, ArrayList<Integer>>();
		for (int i = 0; i < customersInfo.length; i++) {
			if (mapSubInfoByResult.get(customersInfo[i][targetAttrIndex]) == null) {
				ArrayList<Integer> createSubInfo = new ArrayList<Integer>();
				createSubInfo.add(i);
				mapSubInfoByResult.put(customersInfo[i][targetAttrIndex], createSubInfo);
			} else {
				mapSubInfoByResult.get(customersInfo[i][targetAttrIndex]).add(i);
			}
		}

		Random genRandom = new Random();
		for (String s : mapSubInfoByResult.keySet()) {
			if (SHOW_TARGET_ATTRIBUTE_VALUE) {
				System.out.println(s + ":" + mapSubInfoByResult.get(s).size());
			}
			ArrayList<Integer> subInfo = mapSubInfoByResult.get(s);
			int subSize = (int) (subInfo.size() * RATIO_TRAIN_DATA);
			int count = 0;
			while (count < subSize) {
				int ramNum = genRandom.nextInt(subInfo.size());
				trainInfo.add(subInfo.remove(ramNum));
				count++;
			}
			testInfo.addAll(subInfo);
		}
	}

	private static void showAttrValueOf(int idxAttr) {
		System.out.println("== Show Attribute Value =======================");
		for (int i = 0; i < customersInfo.length; i++) {
			System.out.println("["+i+"] "+customersInfo[i][idxAttr]);
		}
		System.out.println("== Show Attribute Value (END) =================");
	}

	private static boolean isNumeric(String s) {
		return s.matches("\\d+(\\.\\d+)?");
	}
}
