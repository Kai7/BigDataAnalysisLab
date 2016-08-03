public class ARMining {

	/**
	 * @param args
	 */
	private static double MINSUP_RATIO = 0.003;

	// private static int MINSUP = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// String DataPath = "/home/kai7/Documents/simple.txt";
		String DataPath = "/home/kai7/Documents/Association Rule Mining/Dataset/D1kT10N500.txt";

//		for (int i = 10; i >= 1; i--)
//			FPGrowthMethod(DataPath, (double) i / 1000);

		for (int i = 10; i >= 1; i--)
			AprioriMethod(DataPath, (double) i / 1000);

	}

	private static void AprioriMethod(String DataPath, double minsup_ratio) {
		KaiSeven.Apriori APMininer = new KaiSeven.Apriori(DataPath, minsup_ratio);
		APMininer.mining();
		// String MiningResult = APMininer.toString();
		String SimpleResult = APMininer.getSimpleResult();
		System.out.println(SimpleResult);
	}

	private static void FPGrowthMethod(String DataPath, double minsup_ratio) {
		FPGrowthAlpha FPGMiner = new FPGrowthAlpha(DataPath, minsup_ratio);
		FPGMiner.mining();
		String SimpleResult = FPGMiner.getSimpleResult();
		System.out.println(SimpleResult);
	}
}
