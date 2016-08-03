package KaiSeven;

public class SPMining {

	/**
	 * @param args
	 */
	private static double MINSUP_RATIO = 0.001;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String DataPath = "/home/kai7/Documents/Sequential Pattern Mining/C50S10T2.5N10000.ascii";
//		String DataPath = "/home/kaichi/Documents/Simple_C50S10T2.5N10000.ascii";
//		String DataPath = "/home/kaichi/Documents/Simple.ascii";
		
		PrefixSpan PSMiner = new PrefixSpan(DataPath, MINSUP_RATIO);
		PSMiner.mining();
		
		System.out.println("---------------------\ndone");
	}

}
