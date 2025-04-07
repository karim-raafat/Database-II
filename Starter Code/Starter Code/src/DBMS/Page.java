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

}
