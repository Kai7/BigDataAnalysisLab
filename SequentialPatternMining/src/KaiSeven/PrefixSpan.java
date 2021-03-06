package KaiSeven;

import java.awt.print.Printable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PrefixSpan {
	private static boolean SHOW_SEQUENTIAL_DATABASE = false;
	private static boolean SHOW_PROJTABLE_INFORMATION = false;
	private static boolean SHOW_PATTERM = false;
	private static boolean SHOW_INDIVIDUAL_PATTERM_COUNT = false;
	private static boolean WRITE_TO_FILE = true;
	
	private SequentialDataBase SDB;
	private String inputFilePath;
	private String outputFilePath;
	private PrintWriter dataOutputWriter;
	private double min_sup_ratio;
	private int min_sup;
	
	private int countTotalPattern;

	public PrefixSpan(String inDataPath, String outDataPath, double minsupRatio) {
		Scanner dataInput = null;
		try {
			dataInput = new Scanner(new FileInputStream(inDataPath));
		} catch (FileNotFoundException e) {
			System.out.println("input file error!");
			System.exit(0);
		}

		StringTokenizer handleLine;
		int previousSID;
		int currentSID;
		int previousTID;
		int currentTID;
		ItemNode currentTransFirst = null;
		ItemNode currentTransRear = null;
		Customer tmpCustomer = null;
		LinkedList<Customer> tmpCustomerList = new LinkedList<Customer>();
		TransList tmpRecordList = null;
		TransRecord tmpRecord = null;

		handleLine = new StringTokenizer(dataInput.nextLine());
		previousSID = Integer.parseInt(handleLine.nextToken());
		previousTID = Integer.parseInt(handleLine.nextToken());
		currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
		currentTransRear = currentTransFirst;
		tmpRecordList = new TransList();
		while (dataInput.hasNextLine()) {
			handleLine = new StringTokenizer(dataInput.nextLine());
			currentSID = Integer.parseInt(handleLine.nextToken());
			if (currentSID != previousSID) {
				tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
				tmpRecordList.addRecord(tmpRecord);
				tmpCustomer = new Customer(previousSID, tmpRecordList);
				tmpCustomerList.add(tmpCustomer);
				tmpRecordList = new TransList();
				previousTID = Integer.parseInt(handleLine.nextToken());
				currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
				currentTransRear = currentTransFirst;
				previousSID = currentSID;
			} else {
				currentTID = Integer.parseInt(handleLine.nextToken());
				if (currentTID != previousTID) {
					tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
					tmpRecordList.addRecord(tmpRecord);
					currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
					currentTransRear = currentTransFirst;
					previousTID = currentTID;
				} else {
					currentTransRear.nextItem = new ItemNode(Integer.parseInt(handleLine.nextToken()));
					currentTransRear = currentTransRear.nextItem;
				}
			}
		}
		tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
		tmpRecordList.addRecord(tmpRecord);
		tmpCustomer = new Customer(previousSID, tmpRecordList);
		tmpCustomerList.add(tmpCustomer);

		// System.out.println("CustomerList size : " + tmpCustomerList.size());
		// countCustomerNum(dataPath);

		SDB = new SequentialDataBase(tmpCustomerList.size());
		for (int i = 0; i < SDB.customerArr.length; i++) {
			SDB.customerArr[i] = tmpCustomerList.remove();
		}
		
		inputFilePath = inDataPath;
		outputFilePath = outDataPath;
		dataOutputWriter = null;
		
		min_sup_ratio = minsupRatio;
//		min_sup = 48;	// for Simple data
		min_sup = (int) Math.ceil(min_sup_ratio * SDB.customerArr.length);
		
		countTotalPattern = 0;
		
		if(SHOW_SEQUENTIAL_DATABASE){
			for (int i = 0; i < SDB.customerArr.length ; i++) {
				SDB.customerArr[i].show();
				System.out.print("\n");
			}
			System.out.println("######################################################");
			System.out.println();
		}
		
		System.out.println("######################################################");
		System.out.println("Sequential DataBase has : " + SDB.customerArr.length + " customers");
		System.out.println("Under min_sup_ratio : " + min_sup_ratio + " , choose min_sup : " + min_sup);
		System.out.println("######################################################");
		System.out.println();
	}

	public PrefixSpan(String inDataPath, String outDataPath, int minsup) {
		Scanner dataInput = null;
		try {
			dataInput = new Scanner(new FileInputStream(inDataPath));
		} catch (FileNotFoundException e) {
			System.out.println("input file error!");
			System.exit(0);
		}

		StringTokenizer handleLine;
		int previousSID;
		int currentSID;
		int previousTID;
		int currentTID;
		ItemNode currentTransFirst = null;
		ItemNode currentTransRear = null;
		Customer tmpCustomer = null;
		LinkedList<Customer> tmpCustomerList = new LinkedList<Customer>();
		TransList tmpRecordList = null;
		TransRecord tmpRecord = null;

		handleLine = new StringTokenizer(dataInput.nextLine());
		previousSID = Integer.parseInt(handleLine.nextToken());
		previousTID = Integer.parseInt(handleLine.nextToken());
		currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
		currentTransRear = currentTransFirst;
		tmpRecordList = new TransList();
		while (dataInput.hasNextLine()) {
			handleLine = new StringTokenizer(dataInput.nextLine());
			currentSID = Integer.parseInt(handleLine.nextToken());
			if (currentSID != previousSID) {
				tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
				tmpRecordList.addRecord(tmpRecord);
				tmpCustomer = new Customer(previousSID, tmpRecordList);
				tmpCustomerList.add(tmpCustomer);
				tmpRecordList = new TransList();
				previousTID = Integer.parseInt(handleLine.nextToken());
				currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
				currentTransRear = currentTransFirst;
				previousSID = currentSID;
			} else {
				currentTID = Integer.parseInt(handleLine.nextToken());
				if (currentTID != previousTID) {
					tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
					tmpRecordList.addRecord(tmpRecord);
					currentTransFirst = new ItemNode(Integer.parseInt(handleLine.nextToken()));
					currentTransRear = currentTransFirst;
					previousTID = currentTID;
				} else {
					currentTransRear.nextItem = new ItemNode(Integer.parseInt(handleLine.nextToken()));
					currentTransRear = currentTransRear.nextItem;
				}
			}
		}
		tmpRecord = new TransRecord(currentTransFirst, currentTransRear);
		tmpRecordList.addRecord(tmpRecord);
		tmpCustomer = new Customer(previousSID, tmpRecordList);
		tmpCustomerList.add(tmpCustomer);

		// System.out.println("CustomerList size : " + tmpCustomerList.size());
		// countCustomerNum(dataPath);

		SDB = new SequentialDataBase(tmpCustomerList.size());
		for (int i = 0; i < SDB.customerArr.length; i++) {
			SDB.customerArr[i] = tmpCustomerList.remove();
		}
		
		inputFilePath = inDataPath;
		outputFilePath = outDataPath;
		dataOutputWriter = null;
		
//		min_sup_ratio = minsupRatio;
		min_sup = minsup;
		
		countTotalPattern = 0;
		
		if(SHOW_SEQUENTIAL_DATABASE){
			for (int i = 0; i < SDB.customerArr.length ; i++) {
				SDB.customerArr[i].show();
				System.out.print("\n");
			}
			System.out.println("######################################################");
			System.out.println();
		}
		
		System.out.println("######################################################");
		System.out.println("Sequential DataBase has : " + SDB.customerArr.length + " customers");
		System.out.println("choose min_sup : " + min_sup);
		System.out.println("######################################################");
		System.out.println();
	}
	
	public void mining(){
		if(WRITE_TO_FILE){
			try {
				dataOutputWriter = new PrintWriter(outputFilePath, "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				System.out.println("output file error!");
				System.exit(0);
			}
			
			dataOutputWriter.println("######################################################");
			dataOutputWriter.println("Sequential DataBase has : " + SDB.customerArr.length + " customers");
			dataOutputWriter.println("Under min_sup_ratio : " + min_sup_ratio + " , choose min_sup : " + min_sup);
			dataOutputWriter.println("######################################################");
		}
		System.out.println("start mining...");
		// count frequent item (can rewrite)
		ArrayList<ItemSup> frequentItemList = countFrequentItemFirst();
		ProjectionTerm[] projectionTalbe = new ProjectionTerm[SDB.customerArr.length];
		for(int i=0; i<projectionTalbe.length; i++){
			projectionTalbe[i] = new ProjectionTerm();
		}
		ItemNode tmpItemNode;
		TransList tmpTransList;
		TransRecord tmpTransRecord;
		boolean foundflag;
		int itemNum;
		int countPattern = 0;
		for(int i=0; i<frequentItemList.size(); i++){
//		for(int i=0; i<1; i++){
			itemNum = frequentItemList.get(i).item;
			tmpItemNode = new ItemNode(itemNum);
			tmpTransRecord = new TransRecord(tmpItemNode, tmpItemNode);
			tmpTransList = new TransList();
			tmpTransList.addRecord(tmpTransRecord);
			
			if(SHOW_PATTERM){
				tmpTransList.show(); System.out.println(" : " + frequentItemList.get(i).sup);
			}
			
			if(WRITE_TO_FILE){
				dataOutputWriter.println(tmpTransList.toString() + " : " + frequentItemList.get(i).sup);
			}
			
			countTotalPattern +=1;
			
			// build projection
			for(int j=0; j<SDB.customerArr.length; j++){
				projectionTalbe[j].dashFlag = false;
				projectionTalbe[j].headerRecord = SDB.customerArr[j].shopRecord.firstRecord;
				projectionTalbe[j].headerItemNode = projectionTalbe[j].headerRecord.firstItem;
				
				foundflag = false;
				// search item
				while(projectionTalbe[j].headerRecord != null){
					projectionTalbe[j].headerItemNode = projectionTalbe[j].headerRecord.firstItem;
					while(projectionTalbe[j].headerItemNode != null){
						if(projectionTalbe[j].headerItemNode.itemNum == itemNum){
							foundflag = true;
							break;
						}
						projectionTalbe[j].headerItemNode = projectionTalbe[j].headerItemNode.nextItem;
					}
					if(foundflag == true)
						break;
					projectionTalbe[j].headerRecord = projectionTalbe[j].headerRecord.nextRecord;
				}
				if(foundflag == false)
					continue;
				if(projectionTalbe[j].headerItemNode.nextItem != null){
					projectionTalbe[j].headerItemNode = projectionTalbe[j].headerItemNode.nextItem;
					projectionTalbe[j].dashFlag = true;
				}
				else{
					projectionTalbe[j].headerRecord = projectionTalbe[j].headerRecord.nextRecord;
					if(projectionTalbe[j].headerRecord == null){
						projectionTalbe[j].headerItemNode = null;
						continue;
					}
					projectionTalbe[j].headerItemNode = projectionTalbe[j].headerRecord.firstItem;
				}
			}
			if(SHOW_PROJTABLE_INFORMATION){
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				printProjection(projectionTalbe);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
			
			SPmining(projectionTalbe, tmpTransList);
			
			if(SHOW_INDIVIDUAL_PATTERM_COUNT){
				System.out.println("------------------------------------------------------");
				System.out.println("<" + itemNum + " ... > : " + (countTotalPattern - countPattern));
				System.out.println("======================================================");
				countPattern = countTotalPattern;
			}
		}
		
		if(WRITE_TO_FILE){
			dataOutputWriter.println("======================================================");
			dataOutputWriter.println("Total Pattern : " + countTotalPattern);
			dataOutputWriter.println("======================================================");
			
			dataOutputWriter.close();
		}
		System.out.println("mining complete!");
		System.out.println("======================================================");
		System.out.println("Total Pattern : " + countTotalPattern);
		System.out.println("======================================================");
	}
	
	public void SPmining(ProjectionTerm[] projectionTable, TransList prefix){
		// count frequent item
		ArrayList<ItemSup> frequentItemList = new ArrayList<ItemSup>();
		ArrayList<ItemSup> frequentDashItemList = new ArrayList<ItemSup>();
		HashMap<Integer, Integer> counterItemSup = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> counterDashItemSup = new HashMap<Integer, Integer>();
		HashSet<Integer> tmpItemSet = new HashSet<Integer>();
		HashSet<Integer> tmpDashItemSet = new HashSet<Integer>();
		TransRecord ptrRecord = null;
		ItemNode ptrItemNode = null;
		for(int i=0; i<projectionTable.length;i++){
			if(projectionTable[i].headerRecord == null)
				continue;
			tmpItemSet.clear();
			tmpDashItemSet.clear();
			if(projectionTable[i].dashFlag == false){
				ptrRecord = projectionTable[i].headerRecord;
				while(ptrRecord != null){
					ptrItemNode = ptrRecord.firstItem;
					while(ptrItemNode != null){
					// count non-dash
						tmpItemSet.add(ptrItemNode.itemNum);
						ptrItemNode = ptrItemNode.nextItem;
					}
					
					// count dash
					ptrItemNode = prefix.lastRecord.containIn(ptrRecord);
					if(ptrItemNode != null){
						ptrItemNode = ptrItemNode.nextItem;
						while(ptrItemNode != null){
							tmpDashItemSet.add(ptrItemNode.itemNum);
							ptrItemNode = ptrItemNode.nextItem;
						}
					}
					ptrRecord = ptrRecord.nextRecord;
				}
			}else{
				ptrRecord = projectionTable[i].headerRecord;
				ptrItemNode = projectionTable[i].headerItemNode;
				// count dash first
				while(ptrItemNode != null){
					tmpDashItemSet.add(ptrItemNode.itemNum);
					ptrItemNode = ptrItemNode.nextItem;
				}
				
				ptrRecord = ptrRecord.nextRecord;
				while(ptrRecord != null){
					ptrItemNode = ptrRecord.firstItem;
					while(ptrItemNode != null){
					// count non-dash
						tmpItemSet.add(ptrItemNode.itemNum);
						ptrItemNode = ptrItemNode.nextItem;
					}
					
					// count dash
					ptrItemNode = prefix.lastRecord.containIn(ptrRecord);
					if(ptrItemNode != null){
						ptrItemNode = ptrItemNode.nextItem;
						while(ptrItemNode != null){
							tmpDashItemSet.add(ptrItemNode.itemNum);
							ptrItemNode = ptrItemNode.nextItem;
						}
					}
					ptrRecord = ptrRecord.nextRecord;
				}
			}
			
			for(int item: tmpItemSet){
				if(counterItemSup.get(item) == null)
					counterItemSup.put(item, 1);
				else
					counterItemSup.put(item, counterItemSup.get(item) + 1);
			}
			
			for(int item: tmpDashItemSet){
				if(counterDashItemSup.get(item) == null)
					counterDashItemSup.put(item, 1);
				else
					counterDashItemSup.put(item, counterDashItemSup.get(item) + 1);
			}
		}
		
		for(int key: counterItemSup.keySet()){
			if(counterItemSup.get(key)>= min_sup)
				frequentItemList.add(new ItemSup(key, counterItemSup.get(key)));
		}
		Collections.sort(frequentItemList, new ItemSupComparator());
		
		for(int key: counterDashItemSup.keySet()){
			if(counterDashItemSup.get(key)>= min_sup)
				frequentDashItemList.add(new ItemSup(key, counterDashItemSup.get(key)));
		}
		Collections.sort(frequentItemList, new ItemSupComparator());
		
		if(frequentItemList.size() == 0 && frequentDashItemList.size() == 0)
			return;
		
		if(SHOW_PROJTABLE_INFORMATION){
			System.out.print("frequentItemList:{");
			for(int i=0; i<frequentItemList.size() ;i++){
				System.out.print(frequentItemList.get(i)+ ",");
			}
			System.out.println("}");
			
			// fault list for dash
			System.out.print("frequentDashItemList:{");
			for(int i=0; i<frequentDashItemList.size() ;i++){
				System.out.print(frequentDashItemList.get(i)+ ",");
			}
			System.out.println("}");
		}
		
		
		// recursive mining (non-dash item first)
		ProjectionTerm[] nwProjTable = new ProjectionTerm[projectionTable.length];
		for(int i=0; i<nwProjTable.length; i++){
			nwProjTable[i] = new ProjectionTerm();
		}
		
		for(int i=0; i<frequentItemList.size(); i++){
			int itemNum = frequentItemList.get(i).item;
			ItemNode tmpItemNode = new ItemNode(itemNum);
			TransRecord tmpTransRecord = new TransRecord(tmpItemNode, tmpItemNode);
			TransRecord ptrOrgRearRecord = prefix.lastRecord;
			prefix.addRecord(tmpTransRecord);
			
			if(SHOW_PATTERM){
				prefix.show(); System.out.println(" : " + frequentItemList.get(i).sup);
			}
			
			if(WRITE_TO_FILE){
				dataOutputWriter.println(prefix.toString() + " : " + frequentItemList.get(i).sup);
			}
			
			countTotalPattern +=1;
			
			// build new projection
			
			for(int j=0; j<projectionTable.length; j++){
				nwProjTable[j].dashFlag = false;
				if(projectionTable[j].headerRecord == null){
					nwProjTable[j].headerRecord = null;
					nwProjTable[j].headerItemNode = null;
					continue;
				}
				if(projectionTable[j].dashFlag == true){
					nwProjTable[j].headerRecord = projectionTable[j].headerRecord.nextRecord;
					if(nwProjTable[j].headerRecord == null){
						nwProjTable[j].headerItemNode = null;
						continue;
					}
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
				}else{
					nwProjTable[j].headerRecord = projectionTable[j].headerRecord;
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
				}
				
				while(nwProjTable[j].headerRecord != null){
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
					while(nwProjTable[j].headerItemNode != null){
						if(nwProjTable[j].headerItemNode.itemNum == itemNum)
							break;
						nwProjTable[j].headerItemNode = nwProjTable[j].headerItemNode.nextItem;
					}
					if(nwProjTable[j].headerItemNode != null)
						break;
					nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
				}
				if(nwProjTable[j].headerRecord == null)
					continue;
				if(nwProjTable[j].headerItemNode.nextItem != null){
					nwProjTable[j].headerItemNode = nwProjTable[j].headerItemNode.nextItem;
					nwProjTable[j].dashFlag = true;
				}
				else{
					nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
					if(nwProjTable[j].headerRecord == null){
						nwProjTable[j].headerItemNode = null;
						continue;
					}
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
				}			
			}
			if(SHOW_PROJTABLE_INFORMATION){
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				printProjection(nwProjTable);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");	
			}
				
			SPmining(nwProjTable, prefix);
			
//			prefix.deleteRearRecord();
			prefix.lastRecord = ptrOrgRearRecord;
			prefix.lastRecord.nextRecord = null;
		}
		
		// recursive mining (dash item)
		for(int i=0; i<frequentDashItemList.size(); i++){
			int itemNum = frequentDashItemList.get(i).item;
			ItemNode tmpItemNode = new ItemNode(itemNum);
			TransRecord ptrOrgRearRecord = prefix.lastRecord;
			ItemNode ptrOrgRearItem = ptrOrgRearRecord.lastItem;
//			prefix.addItemInRear(itemNum);
			prefix.lastRecord.lastItem.nextItem = tmpItemNode;
			prefix.lastRecord.lastItem = tmpItemNode;
			
			if(SHOW_PATTERM){
				prefix.show(); System.out.println(" : " + frequentDashItemList.get(i).sup);
			}
			
			if(WRITE_TO_FILE){
				dataOutputWriter.println(prefix.toString() + " : " + frequentDashItemList.get(i).sup);
			}
			
			countTotalPattern +=1;
			
			
			// build new projection
			for(int j=0; j<projectionTable.length; j++){
				nwProjTable[j].dashFlag = false;
				if(projectionTable[j].headerRecord == null){
					nwProjTable[j].headerRecord = null;
					nwProjTable[j].headerItemNode = null;
					continue;
				}
				if(projectionTable[j].dashFlag == true){
					nwProjTable[j].headerRecord = projectionTable[j].headerRecord;
					nwProjTable[j].headerItemNode = projectionTable[j].headerItemNode;
					while(nwProjTable[j].headerItemNode != null){
						if(nwProjTable[j].headerItemNode.itemNum == itemNum)
							break;
						nwProjTable[j].headerItemNode = nwProjTable[j].headerItemNode.nextItem;
					}
					// found itemNum in the first dash set
					if(nwProjTable[j].headerItemNode != null){
						if(nwProjTable[j].headerItemNode.nextItem == null){
							nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
							if(nwProjTable[j].headerRecord == null)
								nwProjTable[j].headerItemNode = null;
							else
								nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
							continue;
						}
						nwProjTable[j].dashFlag = true;
						nwProjTable[j].headerItemNode = nwProjTable[j].headerItemNode.nextItem;
						continue;
					}
					// not found itemNum in the first dash set
					nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
					if(nwProjTable[j].headerRecord == null){
						nwProjTable[j].headerItemNode = null;
						continue;
					}
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
				}else{
					nwProjTable[j].headerRecord = projectionTable[j].headerRecord;
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
				}
				
				while(nwProjTable[j].headerRecord != null){
					ptrItemNode = prefix.lastRecord.containIn(nwProjTable[j].headerRecord);
					if(ptrItemNode != null)
						break;
					nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
				}
				// found
				if(ptrItemNode != null){
					if(ptrItemNode.nextItem != null){
						nwProjTable[j].dashFlag = true;
						nwProjTable[j].headerItemNode = ptrItemNode.nextItem;
						continue;
					}
					nwProjTable[j].headerRecord = nwProjTable[j].headerRecord.nextRecord;
					if(nwProjTable[j].headerRecord == null){
						nwProjTable[j].headerItemNode = null;
						continue;
					}
					nwProjTable[j].headerItemNode = nwProjTable[j].headerRecord.firstItem;
					continue;
				}
				// not found
				nwProjTable[j].headerRecord = null;
				nwProjTable[j].headerItemNode = null;
				
			}
			if(SHOW_PROJTABLE_INFORMATION){
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				printProjection(nwProjTable);
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
			
			SPmining(nwProjTable, prefix);
			
//			prefix.deleteRearRecord();
			prefix.lastRecord.lastItem = ptrOrgRearItem;
			prefix.lastRecord.lastItem.nextItem = null;
		}
	}
	
	public void countCustomerNum(String dataPath) {
		Scanner dataInput = null;
		try {
			dataInput = new Scanner(new FileInputStream(dataPath));
		} catch (FileNotFoundException e) {
			System.out.println("input file error!");
			System.exit(0);
		}

		StringTokenizer handleLine;
		HashSet<Integer> tmpSet = new HashSet<Integer>();
		int id;
		while (dataInput.hasNextLine()) {
			handleLine = new StringTokenizer(dataInput.nextLine());
			id = Integer.parseInt(handleLine.nextToken());
			tmpSet.add(id);
		}

		System.out.println("customer count : " + tmpSet.size());
	}
	
	public ArrayList<ItemSup> countFrequentItemFirst(){
		HashMap<Integer, Integer> itemSupCounter = new HashMap<Integer, Integer>();
		HashSet<Integer> tmpItemSet = null;
		TransRecord ptrRecord = null;
		ItemNode ptrItemNode = null;
		for(int i=0; i<SDB.customerArr.length;i++){
			ptrRecord = SDB.customerArr[i].shopRecord.firstRecord;
			tmpItemSet = new HashSet<Integer>();
			while(ptrRecord != null){
				ptrItemNode = ptrRecord.firstItem;
				while(ptrItemNode != null){
					tmpItemSet.add(ptrItemNode.itemNum);
					ptrItemNode = ptrItemNode.nextItem;
				}
				ptrRecord = ptrRecord.nextRecord;
			}
			for(int item: tmpItemSet){
				if(itemSupCounter.get(item) == null){
					itemSupCounter.put(item, 1);
				}
				else{
					itemSupCounter.put(item, itemSupCounter.get(item) + 1);
				}
			}
		}
		
		ArrayList<ItemSup> FrequentItemSet = new ArrayList<ItemSup>();
		for(int key: itemSupCounter.keySet()){
			if(itemSupCounter.get(key)>= min_sup){
				FrequentItemSet.add(new ItemSup(key, itemSupCounter.get(key)));
			}
		}
		Collections.sort(FrequentItemSet, new ItemSupComparator());
		return FrequentItemSet;
	}
	
	public void printProjection(ProjectionTerm[] projTable){
		for(int i=0; i<projTable.length; i++){
			if(projTable[i].headerRecord == null){
				System.out.println("<>");
				continue;
			}
			TransRecord ptrRecord;
			ItemNode ptrItemNode;
			if(projTable[i].dashFlag){
				ptrRecord = projTable[i].headerRecord;
				ptrItemNode = projTable[i].headerItemNode;
				System.out.print("<(_");
				while(ptrItemNode != null){
					System.out.print("," + ptrItemNode.itemNum);
					ptrItemNode = ptrItemNode.nextItem;
				}
				System.out.print(")");
				ptrRecord = ptrRecord.nextRecord;
				while(ptrRecord != null){
					System.out.print(",");
					ptrRecord.show();
					ptrRecord = ptrRecord.nextRecord;
				}
				System.out.print(">\n");
			}
			else{
				ptrRecord = projTable[i].headerRecord;
				ptrItemNode = projTable[i].headerItemNode;
				System.out.print("<(" + ptrItemNode.itemNum);
				ptrItemNode = ptrItemNode.nextItem;
				while(ptrItemNode != null){
					System.out.print("," + ptrItemNode.itemNum);
					ptrItemNode = ptrItemNode.nextItem;
				}
				System.out.print(")");
				ptrRecord = ptrRecord.nextRecord;
				while(ptrRecord != null){
					System.out.print(",");
					ptrRecord.show();
					ptrRecord = ptrRecord.nextRecord;
				}
				System.out.print(">\n");
			}
		}
	}
	
	private class SequentialDataBase {
		public Customer[] customerArr;

		public SequentialDataBase() {
			customerArr = null;
		}

		public SequentialDataBase(int size) {
			customerArr = new Customer[size];
		}
	}
	
	private class ProjectionTerm{
		boolean dashFlag;
		TransRecord headerRecord;
		ItemNode headerItemNode;
		
		public ProjectionTerm() {
			dashFlag = false;
			headerRecord = null;
			headerItemNode = null;
		}
	}

	private class ItemSup{
		int item;
		int sup;
		
		public ItemSup(int i, int s) {
			item = i;
			sup = s;
		}
	}
	
	private class Customer {
		public int id;
//		public int recordCount;
		public TransList shopRecord;

		public Customer(int SID, TransList shopList) {
			id = SID;
			shopRecord = shopList;
		}

		public void show() {
			System.out.print("ID: " + id + " # ");
			
			System.out.print("<");
			if(shopRecord.firstRecord == shopRecord.lastRecord){
				shopRecord.firstRecord.show();
			}else{
				TransRecord tmpPointer = shopRecord.firstRecord;
				tmpPointer.show();
				while (tmpPointer.nextRecord != null) {
					tmpPointer = tmpPointer.nextRecord;
					System.out.print(",");
					tmpPointer.show();
				}
			}
			
			System.out.print(">");
		}
	}

	private class TransList {
		public TransRecord firstRecord;
		public TransRecord lastRecord;

		public TransList() {
			firstRecord = null;
			lastRecord = null;
		}


		public void addRecord(TransRecord oneRecord) {
			if (lastRecord == null) {
				firstRecord = oneRecord;
				lastRecord = oneRecord;
				return;
			}
			lastRecord.nextRecord = oneRecord;
			lastRecord = oneRecord;
		}
		
		public void show(){
			System.out.print("<");
			if(firstRecord == lastRecord){
				firstRecord.show();
			}else{
				TransRecord tmpPointer = firstRecord;
				tmpPointer.show();
				while (tmpPointer.nextRecord != null) {
					tmpPointer = tmpPointer.nextRecord;
					System.out.print(",");
					tmpPointer.show();
				}
			}
			System.out.print(">");
		}
		
		public String toString(){
			String result = "<";
			if(firstRecord == lastRecord){
				result += firstRecord.toString();
			}else{
				TransRecord tmpPointer = firstRecord;
				result += tmpPointer.toString();
				while (tmpPointer.nextRecord != null) {
					tmpPointer = tmpPointer.nextRecord;
					result += "," + tmpPointer.toString();
				}
			}
			result  += ">";
			
			return result;
		}
	}

	private class TransRecord {
		public ItemNode firstItem;
		public ItemNode lastItem;
		public TransRecord nextRecord;
		
		public TransRecord(){
			firstItem = null;
			lastItem = null;
			nextRecord = null;
		}
		public TransRecord(ItemNode first, ItemNode last) {
			firstItem = first;
			lastItem = last;
			nextRecord = null;
		}

		public void show() {
			System.out.print("(");
			if(firstItem == lastItem){
				System.out.print(firstItem.itemNum);
			}else{
				ItemNode ItemNodeTmpPointer = firstItem;
				System.out.print(ItemNodeTmpPointer.itemNum);
				while(ItemNodeTmpPointer.nextItem != null){
					ItemNodeTmpPointer = ItemNodeTmpPointer.nextItem;
					System.out.print("," + ItemNodeTmpPointer.itemNum);
				}
			}
			System.out.print(")");
		}
		
		public String toString(){
			String result = "(";
			if(firstItem == lastItem){
				result += firstItem.itemNum;
			}else{
				ItemNode ItemNodeTmpPointer = firstItem;
				result += ItemNodeTmpPointer.itemNum;
				while(ItemNodeTmpPointer.nextItem != null){
					ItemNodeTmpPointer = ItemNodeTmpPointer.nextItem;
					result += "," + ItemNodeTmpPointer.itemNum;
				}
			}
			result += ")";
			
			return result;
		}
		
		public void addItemNode(ItemNode oneItemNode) {
			if (lastItem == null) {
				firstItem = oneItemNode;
				lastItem = oneItemNode;
				return;
			}
			lastItem.nextItem = oneItemNode;
			lastItem = oneItemNode;
		}
		
		public ItemNode containIn(TransRecord otherRecord){
		// return the last fit node, null for not found.
			ItemNode ptrThis = firstItem;
			ItemNode ptrOther = otherRecord.firstItem;
			
			while(ptrThis != null){
				while(ptrOther != null){
					if(ptrOther.itemNum > ptrThis.itemNum)
						return null;
					else if(ptrOther.itemNum == ptrThis.itemNum)
						break;
					ptrOther = ptrOther.nextItem;
				}
				if(ptrOther == null)
					return null;
				if(ptrThis.nextItem == null)
					return ptrOther;
				ptrOther = ptrOther.nextItem;
				ptrThis = ptrThis.nextItem;
			}
			System.out.println("something error!");
			return ptrOther;
		}
	}
	
	private class ItemNode {
		public int itemNum;
		public ItemNode nextItem;
		
		public ItemNode(int i) {
			itemNum = i;
			nextItem = null;
		}
	}
	
	private class ItemSupComparator implements Comparator<ItemSup> {
		@Override
		public int compare(ItemSup is1, ItemSup is2) {
			return is1.sup - is1.sup;
		}
	}
}


