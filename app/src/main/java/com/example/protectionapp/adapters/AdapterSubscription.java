package com.example.protectionapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.PlansBean;

import java.util.List;

public class AdapterSubscription extends RecyclerView.Adapter<AdapterSubscription.SubscribeHolder> {
    private Activity activity;
    private List<PlansBean> plansBeans;

    public AdapterSubscription(Activity activity, List<PlansBean> plansBeans) {
        this.activity = activity;
        this.plansBeans = plansBeans;
    }

    @NonNull
    @Override
    public AdapterSubscription.SubscribeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribtion_item, parent, false);
        return new SubscribeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubscription.SubscribeHolder holder, int position) {
        PlansBean plansBean = plansBeans.get(position);
        holder.tvPrice.setText(plansBean.getPlanPrice());
        holder.tvPeriod.setText(plansBean.getPlan_duration());
        holder.tvScheme.setText(plansBean.getPlan_name());
    }

    @Override
    public int getItemCount() {
        return plansBeans.size();
    }

    public class SubscribeHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvPeriod, tvScheme;

        public SubscribeHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvScheme = itemView.findViewById(R.id.tvScheme);
        }
    }
}
