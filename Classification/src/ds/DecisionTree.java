package ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DecisionTree {
	DecisionNode root;

	public DecisionTree() {
		root = null;
	}

	public void buildTree(String[][] cusInfo, HashSet<Integer> trainInfo, String[] attrs, HashSet<Integer> numattrs, HashSet<Integer> disattrs, int targetIndex) {
		
		root = detectDecisionAttr(cusInfo, trainInfo, attrs, numattrs, disattrs, targetIndex);
	}

	// This version is only for ID3
	public DecisionNode detectDecisionAttr(String[][] cusInfo, HashSet<Integer> subInfo, String[] attrs, HashSet<Integer> numattrs, HashSet<Integer> disattrs,
			int targetIndex) {
		if (disattrs.size() == 0) {
			DecisionNode leafDNode = new DecisionNode();
			HashMap<String, Integer> countDefaultResult = new HashMap<String, Integer>();
			for (int i : subInfo) {
				if (countDefaultResult.get(cusInfo[i][targetIndex]) == null) {
					countDefaultResult.put(cusInfo[i][targetIndex], 1);
				} else {
					countDefaultResult.put(cusInfo[i][targetIndex], countDefaultResult.get(cusInfo[i][targetIndex]) + 1);
				}
			}
			int maxCount = 0;
			String defaultResult = null;
			for (String value : countDefaultResult.keySet()) {
				if (countDefaultResult.get(value) > maxCount) {
					maxCount = countDefaultResult.get(value);
					defaultResult = value;
				}
			}
			leafDNode.defaultResult = defaultResult;

			return leafDNode;
		}
		HashMap<String, HashSet<Integer>> mapAttrSubInfo = null;
		double minAttrInfo = Double.MAX_VALUE;
		int minAttrInfoIndex = -1;
		for (int idxDisAttr : disattrs) {
			AttrInfoAndMap tmpAttrInfoMap = calculateAttrInfo(cusInfo, subInfo, idxDisAttr, targetIndex);
			if (tmpAttrInfoMap.attrInfo < minAttrInfo) {
				minAttrInfoIndex = idxDisAttr;
				minAttrInfo = tmpAttrInfoMap.attrInfo;
				mapAttrSubInfo = tmpAttrInfoMap.mapAttrInfo;
			}
		}
		HashSet<Integer> prndisattrs = new HashSet<Integer>(disattrs);
		prndisattrs.remove(minAttrInfoIndex);
		HashMap<String, DecisionNode> decisionMap = new HashMap<String, DecisionNode>();
		for (String attr : mapAttrSubInfo.keySet()) {
			DecisionNode tmpNode = detectDecisionAttr(cusInfo, mapAttrSubInfo.get(attr), attrs, numattrs, prndisattrs, targetIndex);
			decisionMap.put(attr, tmpNode);
		}

		return new DecisionNode(attrs[minAttrInfoIndex], decisionMap);
	}

	private AttrInfoAndMap calculateAttrInfo(String[][] cusInfo, HashSet<Integer> subInfo, int detectIndex, int targetIndex) {
		HashMap<String, HashSet<Integer>> m = new HashMap<String, HashSet<Integer>>();
		for (int i : subInfo) {
			if (m.get(cusInfo[i][detectIndex]) == null) {
				HashSet<Integer> tmpSet = new HashSet<Integer>();
				tmpSet.add(i);
				m.put(cusInfo[i][detectIndex], tmpSet);
			} else {
				m.get(cusInfo[i][detectIndex]).add(i);
			}
		}
		double a = 0;
		for (String attr : m.keySet()) {
			a += (double) m.get(attr).size() / subInfo.size() * calculateInfo(cusInfo, m.get(attr), targetIndex);
		}

		return new AttrInfoAndMap(a, m);
	}

	private double calculateInfo(String[][] cusInfo, HashSet<Integer> subInfo, int targetIndex) {
		HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
		for (int i : subInfo) {
			if (tmpMap.get(cusInfo[i][targetIndex]) == null) {
				tmpMap.put(cusInfo[i][targetIndex], 1);
			} else {
				tmpMap.put(cusInfo[i][targetIndex], tmpMap.get(cusInfo[i][targetIndex]) + 1);
			}
		}
		double result = 0.0;
		for (String targetValue : tmpMap.keySet()) {
			double p = (double) tmpMap.get(targetValue) / subInfo.size();
			result += p * Math.log(p);
		}

		return -result / Math.log(2);
	}

	public String predict(HashMap<String, String> inputAttr) {
		DecisionNode ptrSearchDNode = root;
		while (ptrSearchDNode.attr != null) {
			ptrSearchDNode = ptrSearchDNode.decisionMap.get(inputAttr.get(ptrSearchDNode.attr));
		}

		return ptrSearchDNode.defaultResult;
	}

	class AttrInfoAndMap {
		public double attrInfo;
		HashMap<String, HashSet<Integer>> mapAttrInfo = null;

		public AttrInfoAndMap(double a, HashMap<String, HashSet<Integer>> m) {
			attrInfo = a;
			mapAttrInfo = m;
		}
	}
}
