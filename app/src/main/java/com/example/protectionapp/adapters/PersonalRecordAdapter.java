package com.example.protectionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.PersonalRecord;

import java.util.List;

public class PersonalRecordAdapter extends RecyclerView.Adapter<PersonalRecordAdapter.PersonalRecordHolder> {
    List<PersonalRecord> recordList;
    private RecyclerViewClickListener recyclerViewClickListener;


    public PersonalRecordAdapter(List<PersonalRecord> recordList, RecyclerViewClickListener recyclerViewClickListener) {
        this.recordList = recordList;
        this.recyclerViewClickListener=recyclerViewClickListener;
    }

    @NonNull
    @Override
    public PersonalRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem_personalrecords, parent, false);
        return new PersonalRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalRecordHolder holder, int position) {
//        int color= Color.argb(255,new Random().nextInt(100)+150,new Random().nextInt(100)+150,new Random().nextInt(100)+150);
//        holder.llPersonal.setBackgroundColor(color);
        holder.tvDoc.setText(recordList.get(position).getRecordsName());
        holder.ivDoc.setImageResource(recordList.get(position).getDocResId());

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class PersonalRecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDoc;
        ImageView ivDoc;

        public PersonalRecordHolder(@NonNull View itemView) {
            super(itemView);
            tvDoc = itemView.findViewById(R.id.tvDoc);
            ivDoc = itemView.findViewById(R.id.ivDoc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());

        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
}
