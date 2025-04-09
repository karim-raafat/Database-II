package DBMS;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable {
    int pageNumber;
    ArrayList<String[]> records;

    public Page(int pageNumber) {
        this.pageNumber = pageNumber;
        records = new ArrayList<String[]>();
    }

    public boolean isFull() {
        return records.size() >= DBApp.dataPageSize;
    }

    public void addRecord(String[] record) {
        records.add(record);
    }

    public ArrayList<String[]> getAllRecords() {
        return records;
    }

    public String[] getRecordByIndex(int recordNumber) {
        return records.get(recordNumber);
    }
    
    public String toString() {
    	String r ="";
    	for(int i = 0; i<records.size(); i++) {
    		for (int j =0; j<records.get(i).length; j++){
    			r+= " "+ records.get(i)[j];
    		}
    		
    	}
    	
    	return r;
    }

}
