import java.util.ArrayList;
import java.util.HashSet;

public class FPGrowthAlgo {
	private static int ITEMCOUNT = 10;

	public static class FPItem {
		public int itemset;
		public int sup;

		public FPItem() {
			itemset = 0;
			sup = 0;
		}

		public FPItem(int a) {
			itemset = a;
		}

		public FPItem(int a, int b) {
			itemset = a;
			sup = b;
		}
	}

	public static HashSet<Integer> FPGrowthMine(
			ArrayList<HashSet<Integer>> DataSet, int minsup) {
		ArrayList<FPItem> FPTable = new ArrayList<FPItem>();
		for (int i = 1; i <= ITEMCOUNT; i++) {
			FPTable.add(new FPItem(i));
		}
		for (int i = 0; i < DataSet.size(); i++) {
			for (int j : DataSet.get(i)) {
				FPTable.get(j - 1).sup += 1;
			}
		}
		for (int i = FPTable.size(); i > 0; i--) {
			if (FPTable.get(i - 1).sup < minsup)
				FPTable.remove(i - 1);
		}
		sortFPTable(FPTable);

		System.out.print("done");
		return null;

	}

	private static void sortFPTable(ArrayList<FPItem> FPTable) {
		int tmpInt;
		for (int j = FPTable.size(); j >= 1; j--) {
			for (int i = 0; i <= j - 2; i++) {
				if (FPTable.get(i).sup < FPTable.get(i + 1).sup) {
					tmpInt = FPTable.get(i).itemset;
					FPTable.get(i).itemset = FPTable.get(i + 1).itemset;
					FPTable.get(i + 1).itemset = tmpInt;
					tmpInt = FPTable.get(i).sup;
					FPTable.get(i).sup = FPTable.get(i + 1).sup;
					FPTable.get(i + 1).sup = tmpInt;
				}
			}
		}
	}
}
