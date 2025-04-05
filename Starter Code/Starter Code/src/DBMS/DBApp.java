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
	static int dataPageSize = -100;

	public static void createTable(String tableName, String[] columnsNames) {
		Table table = new Table(tableName, columnsNames);
		FileManager.storeTable(tableName, table);
		// trace??

	}

	public static void insert(String tableName, String[] record) {
		Table table = FileManager.loadTable(tableName);
		// add check if table is created??
		Page lastPage = null;
		if (!table.pages.isEmpty())
			lastPage = FileManager.loadTablePage(tableName, table.pages.size() - 1);
		if (lastPage == null || lastPage.isFull()) {
			int newPageNumber = table.pages.size();
			lastPage = new Page(newPageNumber);
			table.pages.add(lastPage);
		}

		lastPage.addRecord(record);
		// trace ???
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
		//trace??
		return result;
	}

	public static ArrayList<String[]> select(String tableName, int pageNumber, int recordNumber) {

		return new ArrayList<String[]>();
	}

	public static ArrayList<String[]> select(String tableName, String[] cols, String[] vals) {

		return new ArrayList<String[]>();
	}

	public static String getFullTrace(String tableName) {

		return "";
	}

	public static String getLastTrace(String tableName) {

		return "";
	}

	public static void main(String[] args) throws IOException {

	}

}
