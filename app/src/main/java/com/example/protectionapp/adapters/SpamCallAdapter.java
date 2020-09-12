package com.example.protectionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.SpamBean;
import com.example.protectionapp.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class SpamCallAdapter extends RecyclerView.Adapter<SpamCallAdapter.SpamHolder> {
    private List<SpamBean> spamBeanList = new ArrayList<>();
    private SpamCallRecyclerViewListener spamCallRecyclerViewListener;

    public SpamCallAdapter(SpamCallRecyclerViewListener spamCallRecyclerViewListener) {
        this.spamCallRecyclerViewListener = spamCallRecyclerViewListener;
    }

    @NonNull
    @Override
    public SpamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spam_call_items, parent, false);
        return new SpamHolder(view);
    }

    public void updateList(List<SpamBean> spamBeanList) {
        this.spamBeanList = spamBeanList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SpamHolder holder, int position) {
        SpamBean spamBean=spamBeanList.get(position);
        holder.tvCallerName.setText(spamBean.getCallName());
        holder.tvCallerNumber.setText(spamBean.getCallerNumber());
        holder.swSpam.setChecked(spamBean.isChecked());
        holder.swSpam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spamCallRecyclerViewListener.onChecked(b,spamBeanList.get(position).getId());
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spamCallRecyclerViewListener.onDelete(spamBeanList.get(position).getId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return spamBeanList.size();
    }

    public class SpamHolder extends RecyclerView.ViewHolder {
        CardView cardSpam;
        TextView tvCallerName, tvCallerNumber;
        ImageView ivDelete;
        Switch swSpam;

        public SpamHolder(@NonNull View itemView) {
            super(itemView);
            cardSpam = itemView.findViewById(R.id.cardSpam);
            tvCallerName = itemView.findViewById(R.id.tvcallerName);
            tvCallerNumber = itemView.findViewById(R.id.tvcallerNumber);
            ivDelete = itemView.findViewById(R.id.ivDeleteCalls);
            swSpam = itemView.findViewById(R.id.swSpam);
        }
    }
    public interface SpamCallRecyclerViewListener
    {
        void onDelete(int id,int position);
        void  onChecked(boolean b,int id);
    }
}
