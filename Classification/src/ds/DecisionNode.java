package ds;

import java.util.HashMap;

public class DecisionNode {
	public String attr;
	public String defaultResult;
	public double info;
	
	public HashMap<String, DecisionNode> decisionMap;
	
	public DecisionNode(){
		attr = null;
		defaultResult = null;
		info = Double.MAX_VALUE;
		decisionMap = null;
	}
	
	public DecisionNode(String a, HashMap<String, DecisionNode> dmap){
		attr = a;
		defaultResult = null;
		info = Double.MAX_VALUE;
		decisionMap = dmap;
	}
}
