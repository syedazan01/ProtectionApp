package com.example.protectionapp.activites;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.SosBean;

import java.util.List;

public class SosAdapter extends RecyclerView.Adapter<SosAdapter.SosHolder> {
    private Activity activity;
    private List<SosBean> sosBeanList;
    private RecyclerViewHandleOnCheck viewHandleOnCheck;

    public SosAdapter(Activity activity, List<SosBean> sosBeanList,RecyclerViewHandleOnCheck viewHandleOnCheck) {
        this.activity = activity;
        this.sosBeanList = sosBeanList;
        this.viewHandleOnCheck = viewHandleOnCheck;
    }

    @NonNull
    @Override
    public SosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_list_item, parent, false);
        return new SosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SosHolder holder, int position) {
        SosBean sosBean=sosBeanList.get(position);
        holder.cbNum.setOnCheckedChangeListener(null);
        holder.cbNum.setChecked(sosBean.isChecked());
        if (TextUtils.isEmpty(sosBean.getName()))
            holder.cbNum.setText(sosBean.getMobile());
        else
            holder.cbNum.setText(sosBean.getName());
        holder.constrainMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHandleOnCheck.onCheckedChanged(sosBeanList.get(position),!sosBean.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sosBeanList.size();
    }

    public class SosHolder extends RecyclerView.ViewHolder {
        CheckBox cbNum;
        ConstraintLayout constrainMain;

        public SosHolder(@NonNull View itemView) {
            super(itemView);
            cbNum = itemView.findViewById(R.id.cbNumber);
            constrainMain = itemView.findViewById(R.id.constrainMain);

        }
    }
    public interface RecyclerViewHandleOnCheck{
        void onCheckedChanged(SosBean sosBean,boolean isChecked);

    }
}
