package DBMS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class DBApp {
	static int dataPageSize = 2;

	public static void createTable(String tableName, String[] columnsNames) {
		Table table = new Table(tableName, columnsNames);
		table.addTrace("Table created name:" + tableName + ", columnsNames:" + Arrays.toString(columnsNames));
		FileManager.storeTable(tableName, table);

	}

	public static void insert(String tableName, String[] record) {
	    long start = System.currentTimeMillis();
	    Table table = FileManager.loadTable(tableName);
	    Page lastPage = null;
	    if (!table.pages.isEmpty())
	        lastPage = FileManager.loadTablePage(tableName, table.pages.size() - 1);
	    if (lastPage == null || lastPage.isFull()) {
	        int newPageNumber = table.pages.size();
	        lastPage = new Page(newPageNumber);
	        table.pages.add(lastPage);
	    }

	    lastPage.addRecord(record);
	    FileManager.storeTablePage(tableName, lastPage.pageNumber, lastPage);
	    FileManager.storeTable(tableName, table);
	    long end = System.currentTimeMillis();

	    table.addTrace("Inserted:" + Arrays.toString(record)
	        + ", at page number:" + lastPage.pageNumber
	        + ", execution time (mil):" + (end - start));
	    FileManager.storeTable(tableName, table);
	}

	
	public static ArrayList<String[]> select(String tableName) {
	    long start = System.currentTimeMillis();
	    ArrayList<String[]> result = new ArrayList<>();
	    Table table = FileManager.loadTable(tableName);
	    int totalRecords = 0;

	    for (int i = 0; i < table.pages.size(); i++) {
	        Page page = FileManager.loadTablePage(tableName, i);
	        ArrayList<String[]> pageRecords = page.getAllRecords();
	        totalRecords += pageRecords.size();
	        result.addAll(pageRecords);
	    }

	    long end = System.currentTimeMillis();
	    table.addTrace("Select all pages:" + table.pages.size()
	        + ", records:" + totalRecords
	        + ", execution time (mil):" + (end - start));
	    FileManager.storeTable(tableName, table);

	    return result;
	}


	public static ArrayList<String[]> select(String tableName, int pageNumber, int recordNumber) {
	    long start = System.currentTimeMillis();
	    ArrayList<String[]> result = new ArrayList<>();
	    Table table = FileManager.loadTable(tableName);

	    if (pageNumber < table.pages.size()) {
	        Page page = FileManager.loadTablePage(tableName, pageNumber);
	        if (recordNumber < page.records.size()) {
	            result.add(page.getRecordByIndex(recordNumber));
	        }
	    }

	    long end = System.currentTimeMillis();
	    table.addTrace("Select pointer page:" + pageNumber
	        + ", record:" + recordNumber
	        + ", total output count:" + result.size()
	        + ", execution time (mil):" + (end - start));
	    FileManager.storeTable(tableName, table);

	    return result;
	}

	
	public static String getFullTrace(String tableName) {
		Table table = FileManager.loadTable(tableName);
		return table.getFullTrace() + '\n' +"Pages Count: "+table.pages.size() + ", Records Count: "+ table.getRecordsCount();
	}
	

	public static String getLastTrace(String tableName) {
		Table table = FileManager.loadTable(tableName);
		return table.getLastTrace();
	}
	
	
	public static ArrayList<String[]> select(String tableName, String[] cols, String[] vals) {
	    long start = System.currentTimeMillis();
	    ArrayList<String[]> result = new ArrayList<>();
	    Table table = FileManager.loadTable(tableName);
	    ArrayList<String> recordCountsPerPage = new ArrayList<>();
	    int totalRecords = 0;

	    for (int i = 0; i < table.pages.size(); i++) {
	        Page page = FileManager.loadTablePage(tableName, i);
	        int count = 0;
	        for (String[] record : page.getAllRecords()) {
	            if (table.match(cols, vals, record)) {
	                result.add(record);
	                count++;
	            }
	        }
	        if (count > 0)
	            recordCountsPerPage.add("[" + i + ", " + count + "]");
	        totalRecords += count;
	    }

	    long end = System.currentTimeMillis();
	    table.addTrace("Select condition:" + Arrays.toString(cols)
	        + "->" + Arrays.toString(vals)
	        + ", Records per page:" + recordCountsPerPage
	        + ", records:" + totalRecords
	        + ", execution time (mil):" + (end - start));
	    FileManager.storeTable(tableName, table);

	    return result;
	}


	public static void main(String[] args) throws IOException {
		String[] cols = { "id", "name", "major", "semester", "gpa" };
		createTable("student", cols);
		String[] r1 = { "1", "stud1", "CS", "5", "0.9" };
		insert("student", r1);
		String[] r2 = { "2", "stud2", "BI", "7", "1.2" };
		insert("student", r2);
		String[] r3 = { "3", "stud3", "CS", "2", "2.4" };
		insert("student", r3);
		String[] r4 = { "4", "stud4", "DMET", "9", "1.2" };
		insert("student", r4);
		String[] r5 = { "5", "stud5", "BI", "4", "3.5" };
		insert("student", r5);
		System.out.println("Output of selecting the whole table content:");
		ArrayList<String[]> result1 = select("student");
		for (String[] array : result1) {
			for (String str : array) {
				System.out.print(str + " ");
			}
			System.out.println();

		}

		System.out.println("--------------------------------");
		System.out.println("Output of selecting the output by position:");
		ArrayList<String[]> result2 = select("student", 1, 1);
		for (String[] array : result2) {
			for (String str : array) {
				System.out.print(str + " ");
			}
			System.out.println();
		}

		System.out.println("--------------------------------");
		System.out.println("Output of selecting the output by column condition:");
		ArrayList<String[]> result3 = select("student", new String[] { "gpa" }, new String[] { "1.2" });
		for (String[] array : result3) {
			for (String str : array) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
		System.out.println("--------------------------------");
		System.out.println("Full Trace of the table:");
		System.out.println(getFullTrace("student"));
		System.out.println("--------------------------------");
		System.out.println("Last Trace of the table:");
		System.out.println(getLastTrace("student"));
		System.out.println("--------------------------------");
		System.out.println("The trace of the Tables Folder:");
		System.out.println(FileManager.trace());
		FileManager.reset();
		System.out.println("--------------------------------");
		System.out.println("The trace of the Tables Folder after resetting:");
		System.out.println(FileManager.trace());		
		
	}

}
