package com.example.protectionapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;

import java.util.List;
import java.util.Random;

public class AdapterFileShare extends RecyclerView.Adapter<AdapterFileShare.FileShareHolder> {
    private Activity activity;
    private List<FileShareBean> fileShareBeans;
    private FileShareClickListener fileShareClickListener;
    private String shareType;

    public AdapterFileShare(Activity activity, List<FileShareBean> fileShareBeans, FileShareClickListener fileShareClickListener, String shareType) {
        this.activity = activity;
        this.fileShareBeans = fileShareBeans;
        this.fileShareClickListener = fileShareClickListener;
        this.shareType = shareType;
    }

    @NonNull
    @Override
    public FileShareHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_share_item, parent, false);
        return new FileShareHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileShareHolder holder, int position) {
        FileShareBean fileShareBean = fileShareBeans.get(position);
        if (shareType.equals("Sent")) {
            holder.tvSentTo.setText(fileShareBean.getSentFrom());
        } else
            holder.tvSentTo.setText(fileShareBean.getSentTo());
        holder.tvCreatedDate.setText(Utils.getTimeAgo(fileShareBean.getCreatedDate()));
        holder.tvMsg.setText(fileShareBean.getMsg());
        int redColor = new Random().nextInt(100) + 150;
        int greenColor = new Random().nextInt(100) + 150;
        int blueColor = new Random().nextInt(100) + 150;
        int startcolor = Color.argb(255, redColor, greenColor, blueColor);
        int endcolor = Color.argb(200, redColor, greenColor, blueColor);

        holder.cardFileShare.setBackground(Utils.getColoredDrawable(startcolor, endcolor));
        switch (fileShareBean.getDocument_type()) {
            case AppConstant.ADHAAR:
                holder.ivType.setImageResource(R.drawable.aadharlogo);
                break;
            case AppConstant.PAN:
                holder.ivType.setImageResource(R.drawable.panlogo);
                break;
            case AppConstant.DRIVING_LICENSE:
                holder.ivType.setImageResource(R.drawable.drivinglicenselogo);
                break;
            case AppConstant.BANK:
                holder.ivType.setImageResource(R.drawable.banknew);
                break;
            case AppConstant.VOTER_ID:
                holder.ivType.setImageResource(R.drawable.voteridlogo);
                break;
            case AppConstant.STUDENT_ID:
                holder.ivType.setImageResource(R.drawable.student_id);
                break;
            case AppConstant.PASSPORT:
                holder.ivType.setImageResource(R.drawable.passportlogo);
                break;
            case AppConstant.BIRTH_CERTIFICATE:
                holder.ivType.setImageResource(R.drawable.birthlogo);
                break;
            case AppConstant.PDF_DOC:
                holder.ivType.setImageResource(R.drawable.file);
                break;
            case AppConstant.DOC_IMAGE:
                holder.ivType.setImageResource(R.drawable.cameradetector_logo);
                break;
        }
        holder.cardFileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileShareClickListener.onSelect(fileShareBeans.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileShareBeans.size();
    }

    public interface FileShareClickListener {
        void onSelect(FileShareBean fileShareBean);
    }

    public class FileShareHolder extends RecyclerView.ViewHolder {
        TextView tvSentTo, tvMsg, tvCreatedDate;
        ImageView ivType;
        CardView cardFileShare;

        public FileShareHolder(@NonNull View itemView) {
            super(itemView);
            tvSentTo = itemView.findViewById(R.id.tvsentTo);
            tvMsg = itemView.findViewById(R.id.tvSentMsg);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            ivType = itemView.findViewById(R.id.ivType);
            cardFileShare = itemView.findViewById(R.id.cardFileShare);
        }
    }
}
