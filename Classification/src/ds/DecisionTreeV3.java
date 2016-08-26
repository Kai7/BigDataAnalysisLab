package ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import ds.DecisionTreeV2.AttrInfoAndMap;

public class DecisionTreeV3 {
	private static final double RATIO_CURRENT_NOT_BRANCH = 0.9;
	private static final double RATIO_SIZE_NOT_BRANCH = 0.005;

	public DecisionNode root;

	public String[] attributes;
	public int idxTarget;
	HashSet<Integer> idxDisAttrs;
	HashSet<Integer> idxNumAttrs;
	HashSet<String> setDisAttrs;
	HashSet<String> setNumAttrs;

	public DecisionTreeV3() {
		root = null;
	}

	public DecisionTreeV3(String[] attrs, int idxtarget, HashSet<Integer> idxdisattrs, HashSet<Integer> idxnumattrs) {
		root = null;
		attributes = attrs;
		idxTarget = idxtarget;
		idxDisAttrs = new HashSet<Integer>(idxdisattrs);
		idxNumAttrs = new HashSet<Integer>(idxnumattrs);
		setDisAttrs = new HashSet<String>();
		setNumAttrs = new HashSet<String>();
		for (int idx : idxDisAttrs) {
			setDisAttrs.add(attributes[idx]);
		}
		for (int idx : idxNumAttrs) {
			setNumAttrs.add(attributes[idx]);
		}
	}

	public void buildTree(String[][] dataTable, HashSet<Integer> idxSubTable) {

		root = detectDecisionAttr(dataTable, idxSubTable, new HashSet<Integer>(idxDisAttrs), new HashSet<Integer>(idxNumAttrs));
	}

	// This version is only for ID3
	public DecisionNode detectDecisionAttr(String[][] dataTable, HashSet<Integer> idxSubTable, HashSet<Integer> disattrs, HashSet<Integer> numattrs) {
		if (disattrs.size() == 0) {
			DecisionNode leafDNode = new DecisionNode();
			leafDNode.setDefaultInfo(dataTable, idxSubTable, idxTarget);

			return leafDNode;
		}
		DecisionNode DNode = new DecisionNode();
		DNode.setDefaultInfo(dataTable, idxSubTable, idxTarget);
		if (DNode.defaultResultRatio >= RATIO_CURRENT_NOT_BRANCH || idxSubTable.size() <= RATIO_SIZE_NOT_BRANCH) {
			// System.out.println("subtable size: " + idxSubTable.size());
			// System.out.println("disattrs size: " + disattrs.size());
			// System.out.println("dR = " +DNode.defaultResult + " ratio = "+
			// DNode.defaultResultRatio+", not to branch...");
			return DNode;
		}

		HashMap<String, HashSet<Integer>> mapAttrSubInfo = null;
		double minAttrInfoRatio = Double.MAX_VALUE;
		int minAttrInfoIndex = -1;
		// for (int idxDisAttr : disattrs) {
		// AttrInfoAndMap tmpAttrInfoMap = calculateAttrInfo(dataTable,
		// idxSubTable, idxDisAttr);
		// if ((DNode.info - tmpAttrInfoMap.attrInfo) / tmpAttrInfoMap.splitInfo
		// < minAttrInfoRatio) {
		// minAttrInfoIndex = idxDisAttr;
		// minAttrInfoRatio = (DNode.info - tmpAttrInfoMap.attrInfo) /
		// tmpAttrInfoMap.splitInfo;
		// mapAttrSubInfo = tmpAttrInfoMap.mapAttrInfo;
		// }
		// }
		for (int idxDisAttr : numattrs) {
			AttrInfoAndMap tmpAttrInfoMap = calculateAttrInfo(dataTable, idxSubTable, idxDisAttr);
			System.out.println((DNode.info - tmpAttrInfoMap.attrInfo) / tmpAttrInfoMap.splitInfo);
			System.out.println(tmpAttrInfoMap.splitInfo);
			if ((DNode.info - tmpAttrInfoMap.attrInfo) / tmpAttrInfoMap.splitInfo < minAttrInfoRatio) {
				minAttrInfoIndex = idxDisAttr;
				minAttrInfoRatio = (DNode.info - tmpAttrInfoMap.attrInfo) / tmpAttrInfoMap.splitInfo;
				mapAttrSubInfo = tmpAttrInfoMap.mapAttrInfo;
			}
		}

		HashSet<Integer> prndisattrs = new HashSet<Integer>(disattrs);
		prndisattrs.remove(minAttrInfoIndex);
		HashMap<String, DecisionNode> decisionMap = new HashMap<String, DecisionNode>();
		if (mapAttrSubInfo == null) {
			System.out.println("error");
			System.exit(1);
		}
		for (String attr : mapAttrSubInfo.keySet()) {
			DecisionNode tmpNode = detectDecisionAttr(dataTable, mapAttrSubInfo.get(attr), prndisattrs, numattrs);
			decisionMap.put(attr, tmpNode);
		}
		DNode.attr = attributes[minAttrInfoIndex];
		DNode.decisionMap = decisionMap;
		return DNode;
		// return new DecisionNode(attributes[minAttrInfoIndex], decisionMap);
	}

	private AttrInfoAndMap calculateAttrInfo(String[][] dataTable, HashSet<Integer> idxSubTable, int detectIndex) {
		HashMap<String, HashSet<Integer>> m = new HashMap<String, HashSet<Integer>>();
		for (int i : idxSubTable) {
			if (m.get(dataTable[i][detectIndex]) == null) {
				HashSet<Integer> tmpSet = new HashSet<Integer>();
				tmpSet.add(i);
				m.put(dataTable[i][detectIndex], tmpSet);
			} else {
				m.get(dataTable[i][detectIndex]).add(i);
			}
		}

		if (idxDisAttrs.contains(detectIndex)) {
			double a = 0;
			double r = 0;
			for (String attr : m.keySet()) {
				double p = (double) m.get(attr).size() / idxSubTable.size();
				a += p * calculateInfo(dataTable, m.get(attr));
				r += -p * Math.log(p);
				// System.out.println("r:"+r);
			}
			// System.out.println("r/log2:"+r/ Math.log(2));
			// if (r / Math.log(2) > 1) {
			// return new AttrInfoAndMap(a / r / Math.log(2), m);
			// }
			return new AttrInfoAndMap(a, r, m);
		} else {
			ArrayList<Integer> detectAttrValues = new ArrayList<Integer>();
			for (String v : m.keySet()) {
				detectAttrValues.add(Integer.parseInt(v));
			}
			Collections.sort(detectAttrValues);

			HashSet<Integer> idxLeftSubTable = new HashSet<Integer>();
			HashSet<Integer> idxRightSubTable = new HashSet<Integer>();
			for (int idx : idxSubTable) {
				idxRightSubTable.add(idx);
			}

			double minAttrInfo = Double.MAX_VALUE;
			int idxSplit = -1;
			for (int i = 0; i < detectAttrValues.size() - 1; i++) {
				String considerValue = Integer.toString((detectAttrValues.get(i)));
				for (int idx : m.get(considerValue)) {
					idxLeftSubTable.add(idx);
					idxRightSubTable.remove(idx);
				}
				double tmpAttrInfo = 0.0;
				tmpAttrInfo += (double) idxLeftSubTable.size() / idxSubTable.size() * calculateInfo(dataTable, idxLeftSubTable);
				tmpAttrInfo += (double) idxRightSubTable.size() / idxSubTable.size() * calculateInfo(dataTable, idxRightSubTable);
				if (tmpAttrInfo < minAttrInfo) {
					minAttrInfo = tmpAttrInfo;
					idxSplit = i;
				}
			}
			idxLeftSubTable.clear();
			idxRightSubTable.clear();
			for (int i = 0; i <= idxSplit; i++) {
				String considerValue = Integer.toString((detectAttrValues.get(i)));
				for (int idx : m.get(considerValue)) {
					idxLeftSubTable.add(idx);
				}
			}
			for (int i = idxSplit + 1; i < detectAttrValues.size(); i++) {
				String considerValue = Integer.toString((detectAttrValues.get(i)));
				for (int idx : m.get(considerValue)) {
					idxRightSubTable.add(idx);
				}
			}
			String splitValue = Integer.toString(detectAttrValues.get(idxSplit));
			m.clear();
			m.put(splitValue, idxLeftSubTable);
			m.put(Integer.toString(Integer.MAX_VALUE), idxRightSubTable);
			double r = 0.0;
			double p = (double) idxLeftSubTable.size() / idxSubTable.size();
//			System.out.println("l-size"+idxLeftSubTable.size());
			r += -p * Math.log(p);
			p = (double) idxRightSubTable.size() / idxSubTable.size();
//			System.out.println("r-size"+idxRightSubTable.size());
			r += -p * Math.log(p);
			return new AttrInfoAndMap(minAttrInfo, r, m);
		}

	}

	private double calculateInfo(String[][] cusInfo, HashSet<Integer> subInfo) {
		HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
		for (int i : subInfo) {
			if (tmpMap.get(cusInfo[i][idxTarget]) == null) {
				tmpMap.put(cusInfo[i][idxTarget], 1);
			} else {
				tmpMap.put(cusInfo[i][idxTarget], tmpMap.get(cusInfo[i][idxTarget]) + 1);
			}
		}
		double result = 0.0;
		for (String targetValue : tmpMap.keySet()) {
			double p = (double) tmpMap.get(targetValue) / subInfo.size();
			result += p * Math.log(p);
		}

		// System.out.println(-result / Math.log(2));
		return -result / Math.log(2);
	}

	public String predict(HashMap<String, String> inputAttr) {
		if (root == null) {
			System.out.println("root is null!");
			System.exit(1);
		}
		DecisionNode ptrSearchDNode = root;
		while (ptrSearchDNode.attr != null) {
			if (setDisAttrs.contains(ptrSearchDNode.attr)) {
				DecisionNode nextDNode = ptrSearchDNode.decisionMap.get(inputAttr.get(ptrSearchDNode.attr));
				if (nextDNode == null) {
					// System.out.println(ptrSearchDNode.attr + " --> " +
					// inputAttr.get(ptrSearchDNode.attr));
					// for(String s:ptrSearchDNode.decisionMap.keySet()){
					// System.out.println(s);
					// }
					// return "unknow";
					// System.out.println("unknow, retunn df:"+ptrSearchDNode.defaultResult);
					// System.out.println(ptrSearchDNode.attr);
					return ptrSearchDNode.defaultResult;
				}
				ptrSearchDNode = nextDNode;
			} else {
				int splitValue = Integer.MIN_VALUE;
				DecisionNode leftNode = null;
				DecisionNode rightNode = null;
				for (String v : ptrSearchDNode.decisionMap.keySet()) {
					int intv = Integer.parseInt(v);
					if (intv == Integer.MAX_VALUE) {
						rightNode = ptrSearchDNode.decisionMap.get(v);
					} else {
						leftNode = ptrSearchDNode.decisionMap.get(v);
						splitValue = Integer.parseInt(v);
					}
				}
				int value = Integer.parseInt(inputAttr.get(ptrSearchDNode.attr));
				if (value <= splitValue) {
					ptrSearchDNode = leftNode;
				} else {
					ptrSearchDNode = rightNode;
				}
			}
		}

		return ptrSearchDNode.defaultResult;
	}

	class AttrInfoAndMap {
		public double attrInfo;
		public double splitInfo;
		HashMap<String, HashSet<Integer>> mapAttrInfo = null;

		public AttrInfoAndMap(double a, double s, HashMap<String, HashSet<Integer>> m) {
			attrInfo = a;
			splitInfo = s;
			mapAttrInfo = m;
		}
	}
}
