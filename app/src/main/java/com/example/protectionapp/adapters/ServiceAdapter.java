package com.example.protectionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.ServiceBean;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<ServiceBean> serviceBeanList = new ArrayList<>();
    private RecyclerViewOnClick recyclerViewOnClick;

    public ServiceAdapter(List<ServiceBean> serviceBeanList) {
        this.serviceBeanList = serviceBeanList;
    }

    public void setRecyclerViewOnClick(RecyclerViewOnClick recyclerViewOnClick) {
        this.recyclerViewOnClick = recyclerViewOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceBean serviceBean = serviceBeanList.get(position);
        holder.tvServiceName.setText(serviceBean.getServiceName());
        holder.ivService.setImageResource(serviceBean.getServiceResId());
    }

    @Override
    public int getItemCount() {
        return serviceBeanList.size();
    }

    public interface RecyclerViewOnClick {
        void onServiceClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName;
        ImageView ivService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            ivService = itemView.findViewById(R.id.ivService);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewOnClick.onServiceClick(getAdapterPosition());
                }
            });
        }
    }
}
