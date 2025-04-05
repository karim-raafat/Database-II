package DBMS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;;

public class Table implements Serializable {
    String tableName;
    String[] columnNames;
    ArrayList<Page> pages;

    public Table(String tableName, String[] columnsNames) {
        this.tableName = tableName;
        this.columnNames = columnsNames;
        pages = new ArrayList<Page>();
    }

}