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
	static int dataPageSize = 100;

	public static void createTable(String tableName, String[] columnsNames) {
		Table table = new Table(tableName, columnsNames);
		table.addTrace("Table created name:" + tableName + ", columnsNames:" + Arrays.toString(columnsNames));
		FileManager.storeTable(tableName, table);

	}

	public static void insert(String tableName, String[] record) {
		Table table = FileManager.loadTable(tableName);
		// add check if table is created??
		// add check if the record.size = table.columnNames.size??
		Page lastPage = null;
		if (!table.pages.isEmpty())
			lastPage = FileManager.loadTablePage(tableName, table.pages.size() - 1);
		if (lastPage == null || lastPage.isFull()) {
			int newPageNumber = table.pages.size();
			lastPage = new Page(newPageNumber);
			table.pages.add(lastPage);
		}

		lastPage.addRecord(record);
		table.addTrace("Row inserted into table:" + tableName + ", record:" + Arrays.toString(record));
		FileManager.storeTablePage(tableName, lastPage.pageNumber, lastPage);
		FileManager.storeTable(tableName, table);
	}

	public static ArrayList<String[]> select(String tableName) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		Table table = FileManager.loadTable(tableName);
		for (int i = 0; i < table.pages.size(); i++) {
			Page page = FileManager.loadTablePage(tableName, i);
			result.addAll(page.getAllRecords());
		}
		table.addTrace("Select all from table:" + tableName);
		FileManager.storeTable(tableName, table);
		return result;
	}

	public static ArrayList<String[]> select(String tableName, int pageNumber, int recordNumber) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		Table table = FileManager.loadTable(tableName);
		if (pageNumber < table.pages.size()) {
			Page page = FileManager.loadTablePage(tableName, pageNumber);
			if (recordNumber < page.records.size()) {
				result.add(page.getRecordByIndex(recordNumber));
			}
		}
		table.addTrace("Select from table:" + tableName + ", page:" + pageNumber + ", record:" + recordNumber);
		FileManager.storeTable(tableName, table);
		return result;
	}

	public static ArrayList<String[]> select(String tableName, String[] cols, String[] vals) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		Table table = FileManager.loadTable(tableName);
		for (int i = 0; i < table.pages.size(); i++) {
			Page page = FileManager.loadTablePage(tableName, i);
			for (String[] record : page.getAllRecords()) {
				if (table.match(cols, vals, record))
					result.add(record);
			}
		}
		table.addTrace("Select from table:" + tableName + ", condition: " + Arrays.toString(cols) + "=" + Arrays.toString(vals));
		FileManager.storeTable(tableName, table);

		return result;
	}

	public static String getFullTrace(String tableName) {
		Table table = FileManager.loadTable(tableName);
		return table.getFullTrace();
	}

	public static String getLastTrace(String tableName) {
		Table table = FileManager.loadTable(tableName);
		return table.getLastTrace();
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
