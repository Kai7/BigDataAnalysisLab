public class ARMining {

	/**
	 * @param args
	 */
	private static double MINSUP_RATIO = 0.005;

	// private static int MINSUP = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inDataPath = "/home/kai7/Documents/simple.txt";
//		String DataPath = "/home/kai7/Documents/Association Rule Mining/Dataset/D1kT10N500.txt";
//		String inDataPath = "/home/kai7/Documents/Association Rule Mining/Dataset/D10kT10N1k.txt";
		String outDataPath = inDataPath + ".result";
		
		for (int i = 10; i >= 10; i--){
//			FPGrowthAlphaMethod(inDataPath, (double) i / 1000);
			FPGrowthMethod(inDataPath, outDataPath, (double) i / 1000);
		}
		// for (int i = 10; i >= 1; i--)
		// AprioriMethod(DataPath, (double) i / 1000);

	}

	private static void AprioriMethod(String DataPath, double minsup_ratio) {
		KaiSeven.Apriori APMininer = new KaiSeven.Apriori(DataPath, minsup_ratio);
		APMininer.mining();
		// String MiningResult = APMininer.toString();
		String SimpleResult = APMininer.getSimpleResult();
		System.out.println(SimpleResult);
	}

	private static void FPGrowthAlphaMethod(String DataPath, double minsup_ratio) {
		FPGrowthAlpha FPGMiner = new FPGrowthAlpha(DataPath, minsup_ratio);
		FPGMiner.mining();
		String SimpleResult = FPGMiner.getSimpleResult();
		System.out.println(SimpleResult);
	}

	private static void FPGrowthMethod(String inDataPath, String outDataPath, double minsup_ratio) {
		KaiSeven.FPGrowth FPGMiner = new KaiSeven.FPGrowth(inDataPath, outDataPath, minsup_ratio);
		String SimpleResult = FPGMiner.getSimpleResult();
		System.out.println(SimpleResult);
	}
}
