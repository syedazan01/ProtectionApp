package com.example.protectionapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.UserBean;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.UserHolder> {
    private Activity activity;
    private List<UserBean> userBeans = new ArrayList<>();
    private RecyclerViewListener recyclerViewListener;

    public AdapterUsers(Activity activity, List<UserBean> userBeans, RecyclerViewListener recyclerViewListener) {
        this.activity = activity;
        this.userBeans = userBeans;
        this.recyclerViewListener = recyclerViewListener;
        Log.e("xcvdvbdf",userBeans.size()+"");
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.cbUser.setText(userBeans.get(position).getMobile());
        holder.cbUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                recyclerViewListener.onCheck(position, userBeans.get(position), b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userBeans.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        CheckBox cbUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            cbUser = itemView.findViewById(R.id.cbUser);
        }
    }

    public interface RecyclerViewListener {
        void onCheck(int position, UserBean userBean, boolean isChecked);
    }
}
