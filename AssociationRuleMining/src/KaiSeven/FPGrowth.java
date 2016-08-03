package KaiSeven;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FPGrowth {
	private int[][] DataSet;
	private ItemSup[] ItemSupTable;
	private int min_sup;

	public FPGrowth() {
		DataSet = null;
		ItemSupTable = null;
	}

	public FPGrowth(String DataPath, double minsupRatio) {

		Scanner DataInput = null;
		try {
			DataInput = new Scanner(new FileInputStream(DataPath));
		} catch (FileNotFoundException e) {
			System.out.println("input file error!");
			System.exit(0);
		}

		HashSet<Integer> TmpItemSet = null;
		IntArrayLinkNode IntArrayRoot = new IntArrayLinkNode();
		IntArrayLinkNode IntArrayRear = IntArrayRoot;
		IntArrayLinkNode tmpIntArrayNode = null;
		int[] tmpIntArray = null;
		int tmpNum, tmpCount;
		int TransactionCount = 0;
		String TmpHandlingLine = null;
		StringTokenizer HandlingLine = null;
		while (DataInput.hasNextLine()) {
			TransactionCount += 1;
			TmpItemSet = new HashSet<Integer>();
			TmpHandlingLine = DataInput.nextLine();
			HandlingLine = new StringTokenizer(TmpHandlingLine, " ,");
			while (HandlingLine.hasMoreTokens()) {
				tmpNum = Integer.parseInt(HandlingLine.nextToken());
				TmpItemSet.add(tmpNum);
			}
			tmpIntArrayNode = new IntArrayLinkNode(TmpItemSet);
			IntArrayRear.next = tmpIntArrayNode;
			IntArrayRear = IntArrayRear.next;
		}
		DataSet = new int[TransactionCount][];
		for (int i = 0; i < TransactionCount; i++) {
			DataSet[TransactionCount] = IntArrayRoot.next.arr;
			IntArrayRoot = IntArrayRoot.next;
		}

		min_sup = (int) ((int) DataSet.length * minsupRatio);
		System.out.println("DataSet.length : " + DataSet.length);
		System.out.println("min_sup : " + min_sup);
		System.out.println("-------------------------------------------");
	}

	private class IntArrayLinkNode {
		public int[] arr;
		public IntArrayLinkNode next;

		public IntArrayLinkNode() {
			arr = null;
			next = null;
		}

		public IntArrayLinkNode(HashSet<Integer> TmpSet) {
			arr = new int[TmpSet.size()];
			int tmpCount = 0;
			for (int item : TmpSet) {
				arr[tmpCount] = item;
				tmpCount += 1;
			}
			next = null;
		}
	}

	private class ItemSup {
		public int itemNO;
		public int support;

		public ItemSup() {
			itemNO = -1;
			support = 0;
		}
	}
}
