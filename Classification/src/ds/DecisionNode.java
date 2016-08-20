package ds;

import java.util.HashMap;

public class DecisionNode {
	public String attr;
	public String defaultResult;
	
	public HashMap<String, DecisionNode> decisionMap;
	
	public DecisionNode(){
		attr = null;
		defaultResult = null;
		decisionMap = null;
	}
	
	public DecisionNode(String a, HashMap<String, DecisionNode> dmap){
		attr = a;
		defaultResult = null;
		decisionMap = dmap;
	}
}
