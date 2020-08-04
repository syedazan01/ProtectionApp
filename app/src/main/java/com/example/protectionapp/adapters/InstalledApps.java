package com.example.protectionapp.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.interfacecallbacks.OnNotificationChecked;
import com.example.protectionapp.model.PInfo;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstalledApps extends RecyclerView.Adapter<InstalledApps.InstalledHolder> {
    private Activity activity;
    private ArrayList<PInfo> pInfos;
    private OnNotificationChecked notificationChecked;
    private SharedPreferences pref;

    public InstalledApps(Activity activity, ArrayList<PInfo> pInfos, OnNotificationChecked notificationChecked) {
        this.activity = activity;
        this.pInfos = pInfos;
        this.notificationChecked = notificationChecked;
        pref= Utils.getDefaultManager(activity);
    }

    @NonNull
    @Override
    public InstalledHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.install_app_list_items,parent,false);
        return new InstalledHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstalledHolder holder, final int position) {
        PInfo pinfo = pInfos.get(position);
        HashSet blocked =new  HashSet(Arrays.asList(pref.getString(AppConstant.PREF_PACKAGES_BLOCKED, "").split(";")));
        holder.appIcon.setImageDrawable(pinfo.getIcon());
        holder.swOnOff.setChecked(blocked.contains(pinfo.getPname()));
        holder.tvAppName.setText(pInfos.get(position).getAppname());
        holder.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationChecked.onCheckboxAppChecked(position,b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pInfos.size();
    }

    public void notifyList(ArrayList<PInfo> pInfos) {
    this.pInfos=pInfos;
    notifyDataSetChanged();
    }
    public PInfo getItem(int position){
        return pInfos.get(position);
    }
    public class InstalledHolder extends RecyclerView.ViewHolder {
       TextView tvAppName;
       Switch swOnOff;
       CircleImageView appIcon;
        public InstalledHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName=itemView.findViewById(R.id.tvAppName);
            swOnOff=itemView.findViewById(R.id.swOnOff);
            appIcon=itemView.findViewById(R.id.appIcon);
        }
    }
}
