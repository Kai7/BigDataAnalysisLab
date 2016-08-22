package ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DecisionTree {
	public DecisionNode root;

	public String[] attributes;
	public int idxTarget;
	HashSet<Integer> idxDisAttrs;
	HashSet<Integer> idxNumAttrs;

	public DecisionTree(String[] attrs, int idxtarget, HashSet<Integer> idxdisattrs, HashSet<Integer> idxnumattrs) {
		root = null;
		attributes = attrs;
		idxTarget = idxtarget;
		idxDisAttrs = idxdisattrs;
		idxNumAttrs = idxnumattrs;
	}

	public void buildTree(String[][] dataInfo, HashSet<Integer> idxTrain) {
		HashSet<Integer> disattrs = new HashSet<Integer>(idxDisAttrs);
		HashSet<Integer> numattrs = new HashSet<Integer>(idxNumAttrs);

		root = detectDecisionAttr(dataInfo, idxTrain, disattrs, numattrs);
	}

	// This version is only for ID3
	public DecisionNode detectDecisionAttr(String[][] dataInfo, HashSet<Integer> idxTrain, HashSet<Integer> disattrs, HashSet<Integer> numattrs) {
		if (disattrs.size() == 0) {
			DecisionNode leafDNode = new DecisionNode();
			HashMap<String, Integer> countDefaultResult = new HashMap<String, Integer>();
			for (int i : idxTrain) {
				if (countDefaultResult.get(dataInfo[i][idxTarget]) == null) {
					countDefaultResult.put(dataInfo[i][idxTarget], 1);
				} else {
					countDefaultResult.put(dataInfo[i][idxTarget], countDefaultResult.get(dataInfo[i][idxTarget]) + 1);
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

		HashMap<String, IdxTrainInfo> mapAttrToTrainInfo = null;
		double minAttrInfo = Double.MAX_VALUE;
		int minAttrInfoIndex = -1;
		for (int idx : disattrs) {
			AttrInfoAndMap tmpAttrInfoMap = calculateAttrInfo(dataInfo, idxTrain, idx);
			if (tmpAttrInfoMap.attrInfo < minAttrInfo) {
				minAttrInfoIndex = idx;
				minAttrInfo = tmpAttrInfoMap.attrInfo;
				mapAttrToTrainInfo = tmpAttrInfoMap.mapAttrToIdxTrainInfo;
			}
		}

		HashSet<Integer> prndisattrs = new HashSet<Integer>(disattrs);
		prndisattrs.remove(minAttrInfoIndex);
		HashMap<String, DecisionNode> decisionMap = new HashMap<String, DecisionNode>();
		for (String attr : mapAttrToTrainInfo.keySet()) {
			DecisionNode tmpNode = detectDecisionAttr(dataInfo, mapAttrToTrainInfo.get(attr).idxTrain, prndisattrs, numattrs);
			tmpNode.defaultResult = mapAttrToTrainInfo.get(attr).defaultR;
			tmpNode.info = mapAttrToTrainInfo.get(attr).info;
			decisionMap.put(attr, tmpNode);
		}

		return new DecisionNode(attributes[minAttrInfoIndex], decisionMap);
	}

	private AttrInfoAndMap calculateAttrInfo(String[][] dataInfo, HashSet<Integer> idxTrain, int detectIndex) {
		HashMap<String, HashSet<Integer>> m = new HashMap<String, HashSet<Integer>>();
		for (int i : idxTrain) {
			if (m.get(dataInfo[i][detectIndex]) == null) {
				HashSet<Integer> tmpSet = new HashSet<Integer>();
				tmpSet.add(i);
				m.put(dataInfo[i][detectIndex], tmpSet);
			} else {
				m.get(dataInfo[i][detectIndex]).add(i);
			}
		}
		double a = 0;
		HashMap<String, IdxTrainInfo> tmpMap = new HashMap<String, IdxTrainInfo>();
		for (String attr : m.keySet()) {
			InfoAndDefaultResult tmpAnswer = calculateInfo(dataInfo, m.get(attr));
			a += (double) m.get(attr).size() / idxTrain.size() * tmpAnswer.info;
			IdxTrainInfo tmpIdxTrainInfo = new IdxTrainInfo(tmpAnswer.info, tmpAnswer.defaultR, m.get(attr));
			tmpMap.put(attr, tmpIdxTrainInfo);
		}

		return new AttrInfoAndMap(a, tmpMap);
	}

	private InfoAndDefaultResult calculateInfo(String[][] dataInfo, HashSet<Integer> idxTrain) {
		HashMap<String, Integer> counterResult = new HashMap<String, Integer>();
		for (int i : idxTrain) {
			if (counterResult.get(dataInfo[i][idxTarget]) == null) {
				counterResult.put(dataInfo[i][idxTarget], 1);
			} else {
				counterResult.put(dataInfo[i][idxTarget], counterResult.get(dataInfo[i][idxTarget]) + 1);
			}
		}
		int maxCount = 0;
		String defaultResult = null;
		double result = 0.0;
		for (String targetValue : counterResult.keySet()) {
			double p = (double) counterResult.get(targetValue) / idxTrain.size();
			result += p * Math.log(p);
			if (counterResult.get(targetValue) > maxCount) {
				defaultResult = targetValue;
			}
		}

		return new InfoAndDefaultResult(-result / Math.log(2), defaultResult);
	}

	public String predict(HashMap<String, String> inputAttr) {
		if (root == null) {
			System.out.println("root is null!");
			System.exit(1);
		}
		DecisionNode ptrSearchDNode = root;
		while (ptrSearchDNode.attr != null) {
			DecisionNode nextDNode = ptrSearchDNode.decisionMap.get(inputAttr.get(ptrSearchDNode.attr));
			if (nextDNode == null) {
				// System.out.println(ptrSearchDNode.attr + " --> " +
				// inputAttr.get(ptrSearchDNode.attr));
				// for(String s:ptrSearchDNode.decisionMap.keySet()){
				// System.out.println(s);
				// }
				return "unknow";
			}
			ptrSearchDNode = nextDNode;
		}

		return ptrSearchDNode.defaultResult;
	}

	class InfoAndDefaultResult {
		double info;
		String defaultR;

		public InfoAndDefaultResult(double d, String s) {
			info = d;
			defaultR = s;
		}
	}

	class IdxTrainInfo {
		double info;
		String defaultR;
		HashSet<Integer> idxTrain;

		public IdxTrainInfo(double d, String s, HashSet<Integer> hs) {
			info = d;
			defaultR = s;
			idxTrain = hs;
		}
	}
	
	class AttrInfoAndMap {
		public double attrInfo;
		HashMap<String, IdxTrainInfo> mapAttrToIdxTrainInfo = null;

		public AttrInfoAndMap(double a, HashMap<String, IdxTrainInfo> m) {
			attrInfo = a;
			mapAttrToIdxTrainInfo = m;
		}
	}
}
