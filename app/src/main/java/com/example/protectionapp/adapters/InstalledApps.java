package com.example.protectionapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.PInfo;

import java.util.ArrayList;

public class InstalledApps extends RecyclerView.Adapter<InstalledApps.InstalledHolder> {
    private Activity activity;
    private ArrayList<PInfo> pInfos;

    public InstalledApps(Activity activity, ArrayList<PInfo> pInfos) {
        this.activity = activity;
        this.pInfos = pInfos;
    }

    @NonNull
    @Override
    public InstalledHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.install_app_list_items,parent,false);
        return new InstalledHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstalledHolder holder, int position) {
        holder.tvAppName.setText(pInfos.get(position).getAppname());
    }

    @Override
    public int getItemCount() {
        return pInfos.size();
    }

    public class InstalledHolder extends RecyclerView.ViewHolder {
       TextView tvAppName;
        public InstalledHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName=itemView.findViewById(R.id.tvAppName);
        }
    }
}
