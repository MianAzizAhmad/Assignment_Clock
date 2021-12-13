package com.example.clock18l_1072;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;

public class Notes implements Parcelable {
    private String city_name;
    private String id;
    private String ZoneId;
    private boolean selected;
    private String time;
    private transient ClockDAO dao ;

    Notes(String name, String ID, String region, boolean checked, ClockDAO dao, String time){
        this.city_name = name;
        this.id = ID;
        this.ZoneId = region;
        this.selected = checked;
        this.dao = dao;
        this.time = time;
    }

    Notes(String ID, boolean checked, ClockDAO dao){
        this.id = ID;
        this.selected = checked;
        this.dao = dao;
        this.city_name = "";
        this.ZoneId = "";
    }

    Notes(){ }

    private Notes(Parcel in){
        city_name = in.readString();
        id = in.readString();
        ZoneId = in.readString();
        selected = in.readBoolean();
    }


    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>() {
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(city_name);
        out.writeString(id);
        out.writeString(ZoneId);
        out.writeBoolean(selected);
    }

    public int describeContents() {
        return 0;
    }

    public void SetDao(ClockDAO dao){
        this.dao = dao;
    }

    public String getContent(){
        return city_name;
    }

    public String getId(){
        return id;
    }

    public String getZone(){
        return ZoneId;
    }

    public boolean getBox(){
        return selected;
    }

    public String getTime(){ return this.time;}

    public void setTime(String Time){ this.time = Time;}

    public void setBox(boolean check){
        this.selected = check;
    }

    public void delete(){
        if(dao != null){
            dao.clearTable();
        }
    }

    public void save(){

        if (dao != null){

            Hashtable<String,String> data = new Hashtable<String, String>();

            data.put("id",id);
            data.put("city",city_name);
            data.put("zone",ZoneId);
            data.put("selected",Boolean.toString(selected));

            dao.save(data);
        }
    }

    public void load(Hashtable<String,String> data){

        id = data.get("id");
        city_name = data.get("city");
        ZoneId = data.get("zone");
        selected = Boolean.parseBoolean(data.get("selected"));
    }

    public static ArrayList<Notes> load(ClockDAO dao){
        ArrayList<Notes> clocks = new ArrayList<Notes>();
        if(dao != null){

            ArrayList<Hashtable<String,String>> objects = dao.load();
            for(Hashtable<String,String> obj : objects){
                Notes note = new Notes("",false ,dao);
                note.load(obj);
                clocks.add(note);
            }
        }
        return clocks;
    }

    public void updateOne(){
        if(dao != null){
            dao.updateTable(this.id);
        }
    }

}
