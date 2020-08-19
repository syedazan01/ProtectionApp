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
import com.example.protectionapp.model.BirthCertificateBean;
import java.util.List;
import java.util.Random;

public class BirthCertificateAdapter extends RecyclerView.Adapter<BirthCertificateAdapter.BirthCerificateHolder> {
    private Activity activity;
    private List<BirthCertificateBean> birthCertificateBeanList;
    private DocumentClickListener documentClickListener;

    public BirthCertificateAdapter(Activity activity, List<BirthCertificateBean> birthCertificateBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.birthCertificateBeanList = birthCertificateBeanList;
        this.documentClickListener = documentClickListener;
    }



    @NonNull
    @Override
    public BirthCertificateAdapter.BirthCerificateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_document_item, parent, false);
        return new BirthCerificateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthCertificateAdapter.BirthCerificateHolder holder, int position) {
        BirthCertificateBean birthCertificateBean = birthCertificateBeanList.get(position);
        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
        holder.constMain.setBackgroundColor(color);
        holder.tvFileName.setText(birthCertificateBean.getFathername());
        holder.constMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectBirthCertificate(birthCertificateBeanList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return birthCertificateBeanList.size();
    }

    public class BirthCerificateHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constMain;
        TextView tvFileName;

        public BirthCerificateHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
