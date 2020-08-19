package com.example.protectionapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.interfacecallbacks.DocumentClickListener;
import com.example.protectionapp.model.PassportBean;

import java.util.List;
import java.util.Random;

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.PassportHolder> {
    private Activity activity;
    private List<PassportBean> passportBeanList;
    private DocumentClickListener documentClickListener;

    public PassportAdapter(Activity activity, List<PassportBean> passportBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.passportBeanList = passportBeanList;
        this.documentClickListener = documentClickListener;
    }


    @NonNull
    @Override
    public PassportAdapter.PassportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_document_item, parent, false);
        return new PassportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassportAdapter.PassportHolder holder, int position) {
        PassportBean passportBean = passportBeanList.get(position);
        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
        holder.constMain.setBackgroundColor(color);
        holder.tvFileName.setText(passportBean.getFullname());
        holder.constMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectPassport(passportBeanList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return passportBeanList.size();
    }

    public class PassportHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constMain;
        TextView tvFileName;

        public PassportHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
