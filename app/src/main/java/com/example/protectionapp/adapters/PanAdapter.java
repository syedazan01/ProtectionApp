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
import com.example.protectionapp.model.PanBean;

import java.util.List;
import java.util.Random;


public class PanAdapter extends RecyclerView.Adapter<PanAdapter.AdhaarHolder> {
    private Activity activity;
    private List<PanBean> panBeanList;
    private DocumentClickListener documentClickListener;

    public PanAdapter(Activity activity, List<PanBean> panBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.panBeanList = panBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pan_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        PanBean panBean = panBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.panName.setText(panBean.getFullName());
        holder.panFName.setText(panBean.getFatherName());
        holder.panDOB.setText(panBean.getDateOfBirth());
        holder.panNumber.setText(panBean.getPermanentAccountNumber());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectPan(panBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return panBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView panName,panFName,panDOB,panNumber;
        CardView cardMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            panName = itemView.findViewById(R.id.panName);
            panFName = itemView.findViewById(R.id.panFName);
            panDOB = itemView.findViewById(R.id.panDOB);
            panNumber = itemView.findViewById(R.id.panNumber);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
