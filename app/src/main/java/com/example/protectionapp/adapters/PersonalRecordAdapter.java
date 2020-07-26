package com.example.protectionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.PersonalRecord;

import java.util.List;

public class PersonalRecordAdapter extends RecyclerView.Adapter<PersonalRecordAdapter.PersonalRecordHolder> {
    List<PersonalRecord> recordList;


    public PersonalRecordAdapter(List<PersonalRecord> recordList) {
        this.recordList=recordList;
    }

    @NonNull
    @Override
    public PersonalRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem_personalrecords,parent,false);
        return new PersonalRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalRecordHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class PersonalRecordHolder extends RecyclerView.ViewHolder{
        public PersonalRecordHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
