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
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.DlicenceBean;

import java.util.List;
import java.util.Random;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.AdhaarHolder> {
    private Activity activity;
    private List<BankBean> bankBeanList;
    private DocumentClickListener documentClickListener;

    public BankAdapter(Activity activity, List<BankBean> bankBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.bankBeanList = bankBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        BankBean bankBean = bankBeanList.get(position);
        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
        holder.constMain.setBackgroundColor(color);
        holder.tvFileName.setText(bankBean.getAccountHolderName());
        holder.constMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectBank(bankBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        ConstraintLayout constMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            constMain = itemView.findViewById(R.id.ConstrainMain);
        }
    }
}
