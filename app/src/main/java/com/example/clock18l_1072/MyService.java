package com.example.clock18l_1072;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    private final IBinder mBinder = new MyLocalBinder();
    public ArrayList<Notes> templist;
    ClockDAO dao;
    private static String url = "http://api.timezonedb.com/v2.1/list-time-zone?key=L1NHBGWEZNM2&format=json";

    public MyService() {
    }


    public void getTimeZones(){
        dao = new ClockDBDAO(this);
        templist = new ArrayList<Notes>();
        templist = Notes.load(dao);
        if(templist.size() == 0) {
            new GetZones().execute();
        }
    }

    private class GetZones extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("zones");
                    templist = new ArrayList<Notes>();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String zone = c.getString("zoneName");
                        zone.replace("\\", "");
                        int slash = zone.indexOf('/');
                        String temp = Integer.toString(i);
                        String stripped = zone.substring(slash + 1).replace("_", " ");
                        Notes clock = new Notes(stripped, temp, zone, false, dao, "");

                        templist.add(clock);
                        if(i == 0){
                            templist.get(0).delete();
                        }
                        templist.get(i).save();
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }


        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyLocalBinder extends android.os.Binder{
        public MyService getService(){
            return MyService.this;
        }
    }
}
