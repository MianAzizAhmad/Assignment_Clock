package com.example.clock18l_1072;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Notes> list;
    private ArrayList<Notes> originallist;

    public RecycleAdapter(ArrayList<Notes> list2) {
        this.list = list2;
        originallist = new ArrayList<>(this.list);
    }

    public Notes getItem(int position){
        return list.get(position);
    }
    public ArrayList<Notes> getList() {
        return list;
    }

    private void setoriginallistcheck(String zoneid, boolean check){
        for(int i = 0; i < originallist.size() ; i++){
            if(originallist.get(i).getZone() == zoneid){
                originallist.get(i).setBox(check);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.listdata, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notes note = getItem(position);
        holder.textView1.setChecked(note.getBox());
        holder.textView1.setText(note.getContent());
        holder.textView2.setTimeZone(note.getZone());

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckBox textView1;
        public TextClock textView2;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView1 = (CheckBox) itemView.findViewById(R.id.check);
            this.textView2 = (TextClock) itemView.findViewById(R.id.time);
            textView1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // toggle the checked view based on the checked field in the model
            int adapterPosition = getAdapterPosition();
            if (getItem(adapterPosition).getBox()) {
                textView1.setChecked(false);
                getItem(adapterPosition).setBox(false);
                setoriginallistcheck(getItem(adapterPosition).getZone(),false);
            }
            else {
                textView1.setChecked(true);
                getItem(adapterPosition).setBox(true);
                setoriginallistcheck(getItem(adapterPosition).getZone(),true);
            }
        }
    }

    public Filter getFilter(){
        return listfilter;
    }

    private Filter listfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Notes> FilteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                FilteredList.addAll(originallist);
            }else {
                String FilterSequence = constraint.toString().toLowerCase().trim();
                for(Notes item : originallist){
                    if(item.getContent().toLowerCase().contains(FilterSequence)){
                        FilteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = FilteredList;
            return results;
        }



        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();;
            list.addAll((ArrayList<Notes>) results.values);
            notifyDataSetChanged();
        }
    };
}
