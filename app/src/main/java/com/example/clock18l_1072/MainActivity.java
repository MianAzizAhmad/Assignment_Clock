package com.example.clock18l_1072;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Notes> list;
    RecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setTitle("World Clocks");
        Intent intent = getIntent();
        list = intent.getParcelableArrayListExtra("list");
        if(list == null){
            list = new ArrayList<Notes>();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecycleAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_search, menu);
        MenuItem search_item = menu.findItem(R.id.search_icon);
        if(search_item != null){
            search_item.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    refreshSearch("");
                    return true;
                }
            });
        }
        android.widget.SearchView search = (android.widget.SearchView) search_item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String Text){
                adapter.getFilter().filter(Text);
                return false;
            }

        });

        return true;
    }

    private void refreshSearch(String s) {
        adapter.getFilter().filter(s);
    }

    private void prepareResult(){
        Intent intent = new Intent();
        intent.putExtra("list",list);
        setResult(RESULT_OK,intent);
    }
    @Override
    public  void onBackPressed(){
        prepareResult();
        super.onBackPressed();
    }
}