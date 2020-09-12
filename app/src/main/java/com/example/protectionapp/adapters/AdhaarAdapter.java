package com.example.protectionapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.interfacecallbacks.DocumentClickListener;
import com.example.protectionapp.model.AdhaarBean;

import java.util.List;
import java.util.Random;


public class AdhaarAdapter extends RecyclerView.Adapter<AdhaarAdapter.AdhaarHolder> {
    private Activity activity;
    private List<AdhaarBean> adhaarBeanList;
    private DocumentClickListener documentClickListener;

    public AdhaarAdapter(Activity activity, List<AdhaarBean> adhaarBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.adhaarBeanList = adhaarBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adhaar_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, int position) {
        AdhaarBean adhaarBean = adhaarBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.adhaarName.setText(adhaarBean.getFullname());
        holder.adhaarDOB.setText(adhaarBean.getDateofbirth());
        holder.adhaarNumber.setText(adhaarBean.getAadharNumber());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectAdhaar(adhaarBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adhaarBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView adhaarName,adhaarDOB,adhaarNumber;
        CardView cardMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            adhaarName = itemView.findViewById(R.id.adhaarName);
            adhaarDOB = itemView.findViewById(R.id.adhaarDOB);
            adhaarNumber = itemView.findViewById(R.id.adhaarNumber);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
