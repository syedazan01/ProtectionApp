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
import com.example.protectionapp.model.StudentIdBean;

import java.util.List;
import java.util.Random;


public class StudentIDAdapter extends RecyclerView.Adapter<StudentIDAdapter.AdhaarHolder> {
    private Activity activity;
    private List<StudentIdBean> studentIdBeanList;
    private DocumentClickListener documentClickListener;

    public StudentIDAdapter(Activity activity, List<StudentIdBean> studentIdBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.studentIdBeanList = studentIdBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_id_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        StudentIdBean studentIdBean = studentIdBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.studentIdName.setText(studentIdBean.getFullname());
        holder.studentIdFName.setText(studentIdBean.getFathername());
        holder.studentStd.setText(studentIdBean.getBranch());
        holder.studentIdNum.setText(studentIdBean.getEnroll());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectStudentID(studentIdBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentIdBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView studentIdName,studentIdFName,studentStd,studentIdNum;
        CardView cardMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            studentIdName = itemView.findViewById(R.id.studentIdName);
            studentIdFName = itemView.findViewById(R.id.studentIdFName);
            studentStd = itemView.findViewById(R.id.studentStd);
            studentIdNum = itemView.findViewById(R.id.studentIdNum);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
