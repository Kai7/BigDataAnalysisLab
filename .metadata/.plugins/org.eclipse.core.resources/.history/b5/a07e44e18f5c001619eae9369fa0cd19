package KaiSeven;

public class SPMining {

	/**
	 * @param args
	 */
	private static double MINSUP_RATIO = 0.5;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inDataPath = "/home/kai7/Documents/BDALab_testdata/C50S10T2.5N10000.ascii", outDataPath = "/home/kai7/Documents/BDALab_testdata/C50S10T2.5N10000_miningResult.txt"; 
		
		for(int i=1; i>=1; i--){
			PrefixSpan PSMiner = new PrefixSpan(inDataPath,outDataPath, (double)i/1000);
			PSMiner.mining();
		}
		
		
//		String inDataPath = "/home/kaichi/Documents/Simple.ascii", outDataPath = "/home/kaichi/Documents/Simple_miningResult.txt";
//		PrefixSpan PSMiner = new PrefixSpan(inDataPath, outDataPath, MINSUP_RATIO);
//		PSMiner.mining();
		
		System.out.println("------------------------------------------------------\ndone");
	}

}
