package ds;

import java.util.HashMap;
import java.util.HashSet;

public class DecisionNode {
	public String attr;
	public String defaultResult;
	public double defaultResultRatio;
	public double info;

	public HashMap<String, DecisionNode> decisionMap;

	public DecisionNode() {
		attr = null;
		defaultResult = null;
		defaultResultRatio = 0.0;
		info = Double.MAX_VALUE;
		decisionMap = null;
	}

	public DecisionNode(String a, HashMap<String, DecisionNode> dmap) {
		attr = a;
		defaultResult = null;
		defaultResultRatio = 0.0;
		info = Double.MAX_VALUE;
		decisionMap = dmap;
	}

	public void setDefaultInfo(String[][] dataTable, HashSet<Integer> idxSubTable, int idxTarget) {
		HashMap<String, Integer> counterResult = new HashMap<String, Integer>();
		for (int i : idxSubTable) {
			if (counterResult.get(dataTable[i][idxTarget]) == null) {
				counterResult.put(dataTable[i][idxTarget], 1);
			} else {
				counterResult.put(dataTable[i][idxTarget], counterResult.get(dataTable[i][idxTarget]) + 1);
			}
		}
		int maxCount = 0;
		String tmpResult = null;
		double tmpInfo = 0.0;
		for (String targetValue : counterResult.keySet()) {
			double p = (double) counterResult.get(targetValue) / idxSubTable.size();
			tmpInfo += p * Math.log(p);
			if (counterResult.get(targetValue) > maxCount) {
				tmpResult = targetValue;
				maxCount = counterResult.get(targetValue);
			}
		}

		this.info = tmpInfo;
		this.defaultResult = tmpResult;
		this.defaultResultRatio = (double) maxCount / idxSubTable.size();
	}
}
