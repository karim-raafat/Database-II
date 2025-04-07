package DBMS;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;;

public class Table implements Serializable {
    String tableName;
    String[] columnNames;
    ArrayList<Page> pages;
    ArrayList<String> trace;

    public Table(String tableName, String[] columnsNames) {
        this.tableName = tableName;
        this.columnNames = columnsNames;
        this.trace = new ArrayList<String>();
    }

    public boolean match(String[] columns, String[] vals, String[] records) {
        for (int i = 0; i < columns.length; i++) {
            for (int j = 0; j < columnNames.length; j++) {
                if (columnNames[j].equals(columns[i]) && records[j].equals(vals[i]))
                    return true;
            }
        }
        return false;
    }

    public void addTrace(String msg) {
        trace.add(msg);
    }

    public String getFullTrace() {
        return String.join("\n", trace);
    }

    public String getLastTrace() {
        return trace.isEmpty() ? "" : trace.get(trace.size() - 1);
    }

}