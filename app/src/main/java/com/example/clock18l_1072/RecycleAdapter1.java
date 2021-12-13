package com.example.clock18l_1072;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextClock;
import android.widget.TextView;

public class RecycleAdapter1 extends RecyclerView.Adapter<RecycleAdapter1.MyViewHolder> {
    private ArrayList<Notes> list;
    private Context mContext;

    public RecycleAdapter1(ArrayList<Notes> list2, Context context) {
        this.list = list2;
        this.mContext = context;
    }

    public Notes getItem(int position){
        return list.get(position);
    }
    public ArrayList<Notes> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecycleAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mainlistdata, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notes note = getItem(position);
        holder.textView1.setText(note.getContent());
        holder.textView2.setTimeZone(note.getZone());

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckBox textView1;
        public TextClock textView2;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView1 = (CheckBox) itemView.findViewById(R.id.name);
            this.textView2 = (TextClock) itemView.findViewById(R.id.Time);
            textView1.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // toggle the checked view based on the checked field in the model
            int adapterPosition = getAdapterPosition();
                textView1.setChecked(false);
                getItem(adapterPosition).setBox(false);
            if (mContext instanceof MainActivity1) {
                ((MainActivity1)mContext).removeOne(adapterPosition);
            }
                notifyDataSetChanged();
        }
    }
}