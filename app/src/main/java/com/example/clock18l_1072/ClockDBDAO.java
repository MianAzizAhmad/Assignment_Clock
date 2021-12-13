package com.example.clock18l_1072;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClockDBDAO implements ClockDAO {
    private Context context;

    public ClockDBDAO(Context ctx){
        context = ctx;
    }

    public void clearTable(){
        ClockDBHelper dbHelper = new ClockDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Notes",null,null);
    }

    public void updateTable(String id){
        ClockDBHelper dbHelper = new ClockDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Selected","false");
        db.update("Notes", cv, "Id = ?", new String[]{id});

    }

    @Override
    public void save(Hashtable<String, String> attributes) {
        ClockDBHelper dbHelper = new ClockDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        Enumeration<String> keys = attributes.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            content.put(key,attributes.get(key));
        }

        db.insert("Notes",null,content);
    }

    @Override
    public void save(ArrayList<Hashtable<String, String>> objects) {
        for(Hashtable<String,String> obj : objects){
            save(obj);
        }
    }

    @Override
    public ArrayList<Hashtable<String, String>> load() {
        ClockDBHelper dbHelper = new ClockDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Notes";
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Hashtable<String,String>> objects = new ArrayList<Hashtable<String, String>>();
        while(cursor.moveToNext()){
            Hashtable<String,String> obj = new Hashtable<String, String>();
            String [] columns = cursor.getColumnNames();
            for(String col : columns){
                obj.put(col.toLowerCase(),cursor.getString(cursor.getColumnIndex(col)));
            }
            objects.add(obj);
        }

        return objects;
    }

}
