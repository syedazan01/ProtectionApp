package com.example.protectionapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.FetchNotification;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdapterSos extends RecyclerView.Adapter<AdapterSos.SosHolder> {
    private Activity activity;
    private List<FetchNotification> fetchNotifications = new ArrayList<>();

    public AdapterSos(Activity activity, List<FetchNotification> fetchNotifications) {
        this.activity = activity;
        this.fetchNotifications = fetchNotifications;
    }

    @NonNull
    @Override
    public SosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_item, parent, false);
        return new SosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SosHolder holder, int position) {
        FetchNotification fetchNotification = fetchNotifications.get(position);
        /*Utils.getStorageReference()
                .child(AppConstant.USER_DETAIL + "/" + fetchNotification.getProfile_Pic())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                                Glide.with(activity).load(task.getResult())
                                        .error(R.drawable.login_logo)
                                        .placeholder(R.drawable.login_logo)
                                        .into(holder.ivUserPic);
                            }
                    }
                });*/
        if (fetchNotification.getTo_mobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
            holder.tvFromUserMobile.setVisibility(View.VISIBLE);
            holder.tvToUserMobile.setVisibility(View.GONE);
            Utils.setShader(Color.BLACK, Color.GREEN, holder.tvType);
            holder.tvType.setText("Sent");
            holder.tvFromUserMobile.setText("To : " + fetchNotification.getFrom_mobile());


        } else {
            holder.tvFromUserMobile.setVisibility(View.GONE);
            holder.tvToUserMobile.setVisibility(View.VISIBLE);
            Utils.setShader(Color.BLACK, Color.RED, holder.tvType);
            holder.tvToUserMobile.setText("From : " + fetchNotification.getTo_mobile());
            holder.tvType.setText("Recieved");
        }

        holder.tvMsg.setText(fetchNotification.getMsg());
        holder.tvTimeAgo.setText(Utils.getTimeAgo(fetchNotification.getCreated_date()));
    }

    @Override
    public int getItemCount() {
        return fetchNotifications.size();
    }

    public class SosHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvToUserMobile, tvFromUserMobile, tvMsg, tvTimeAgo;

        public SosHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvToUserMobile = itemView.findViewById(R.id.tvToUserMobile);
            tvFromUserMobile = itemView.findViewById(R.id.tvFromUserMobile);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
        }
    }
}
