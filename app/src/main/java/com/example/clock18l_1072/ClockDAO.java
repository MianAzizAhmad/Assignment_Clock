package com.example.clock18l_1072;

import java.util.ArrayList;
import java.util.Hashtable;

public interface ClockDAO {
    public void save(Hashtable<String,String> attributes);
    public void save(ArrayList<Hashtable<String,String>> objects);
    public ArrayList<Hashtable<String,String>> load();
    public void clearTable();
    public void updateTable(String id);
}
