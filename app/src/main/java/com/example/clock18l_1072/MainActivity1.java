package com.example.clock18l_1072;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;
import java.io.Serializable;

public class MainActivity1 extends AppCompatActivity {
    public ArrayList<Notes> list;
    RecycleAdapter1 adapter;
    public ArrayList<Notes> list3;
    ClockDAO dao;
    public int returned = 0;
    final int REQUEST_CODE = 1;
    public ArrayList<Notes> list2;
    MyService boundService;
    boolean bound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Favourites");
        setContentView(R.layout.main1);
        String[] availableTimezones = TimeZone.getAvailableIDs();
        dao = new ClockDBDAO(this);

        Intent dbFiller = new Intent(this, MyService.class);
        startService(dbFiller);
        getApplicationContext().bindService(dbFiller,connection, Context.BIND_AUTO_CREATE);

        if(savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList("clocks");
        }


        dao = new ClockDBDAO(this);
        if(list == null ) {
            list = new ArrayList<Notes>();
            list = Notes.load(dao);
        }
        newView();


        FloatingActionButton openactivity2 = findViewById(R.id.floatingActionButton);
        openactivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1.this, MainActivity.class);
                if(list.size() == 0 ) {
                    list = new ArrayList<Notes>();
                    list = Notes.load(dao);
                }
                intent.putExtra("list", list);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        try{
            outState.putParcelableArrayList("clocks",list);
        }catch(Exception ex){}
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        returned = 1;
        if(requestcode == REQUEST_CODE){
            if(resultcode == RESULT_OK){
                list = (ArrayList<Notes>) data.getSerializableExtra("list");
                list2.clear();
                for(int i = 0;i < list.size();i++){
                    list.get(i).SetDao(dao);
                    if(list.get(i).getBox() == true){
                        list2.add(list.get(i));
                        String time;
                        Calendar caLen = Calendar.getInstance();
                        caLen.setTimeZone(TimeZone.getTimeZone(list2.get(list2.size()-1).getZone()));
                        time = caLen.get(Calendar.HOUR_OF_DAY) + ":" + caLen.get(Calendar.MINUTE) + ":" + caLen.get(Calendar.SECOND);
                        list2.get(list2.size()-1).setTime(time);
                    }
                }
                if(list.size() != 0) {
                    list.get(0).delete();
                }
                for(Notes clock : list){
                    clock.save();
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
                adapter = new RecycleAdapter1(list2, this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public void newView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        adapter = new RecycleAdapter1(list2, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //timeUpdater();
    }

    public void removeOne(int position){
        String ID = list2.get(position).getId();
        list2.remove(list2.get(position));
        for(int i = 0; i < list.size();i ++){
            if(list.get(i).getId()==ID){
                list.get(i).setBox(false);
                list.get(i).updateOne();
            }
        }
    }

    public void onResume(){
        super.onResume();

        if(returned == 0) {
            list = Notes.load(dao);
            if(list2 != null) {
                list2.clear();
            }else {
                list2 = new ArrayList<Notes>();
            }
            for(int i = 0;i < list.size();i++){
                if(list.get(i).getBox()==true){
                    list2.add(list.get(i));
                    String time;
                    Calendar caLen = Calendar.getInstance();
                    caLen.setTimeZone(TimeZone.getTimeZone(list2.get(list2.size()-1).getZone()));
                    time = caLen.get(Calendar.HOUR_OF_DAY) + ":" + caLen.get(Calendar.MINUTE) + ":" + caLen.get(Calendar.SECOND);
                    list2.get(list2.size()-1).setTime(time);
                }
            }
            newView();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyLocalBinder binder = (MyService.MyLocalBinder) service;
            boundService = binder.getService();
            bound = true;
            boundService.getTimeZones();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { bound = false; }
    };

    //Function for updating time second by second manually

 /*   public void timeUpdater() {
        final Handler hander = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < list2.size(); i++) {
                            String time;
                            Calendar caLen = Calendar.getInstance();
                            caLen.setTimeZone(TimeZone.getTimeZone(list2.get(i).getZone()));
                            String hour;
                            String min;
                            String sec;
                            if (String.valueOf(caLen.get(Calendar.HOUR_OF_DAY)).length() == 1){
                                hour = "0" + String.valueOf(caLen.get(Calendar.HOUR_OF_DAY));
                            }else{
                                hour = String.valueOf(caLen.get(Calendar.HOUR_OF_DAY));
                            }

                            if (String.valueOf(caLen.get(Calendar.MINUTE)).length() == 1){
                                min = "0" + String.valueOf(caLen.get(Calendar.MINUTE));
                            }else{
                                min = String.valueOf(caLen.get(Calendar.MINUTE));
                            }

                            if (String.valueOf(caLen.get(Calendar.SECOND)).length() == 1){
                                sec = "0" + String.valueOf(caLen.get(Calendar.SECOND));
                            }else{
                                sec = String.valueOf(caLen.get(Calendar.SECOND));
                            }

                            time = hour + ":" + min + ":" + sec;
                            list2.get(i).setTime(time);
                            adapter.notifyItemChanged(i);
                        }
                        timeUpdater();
                    }
                });
            }
        }).start();
    }*/

}




