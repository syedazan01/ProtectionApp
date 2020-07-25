package com.example.protectionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.RecordingFileData;

import java.util.List;

public class RecordingFileAdapter extends RecyclerView.Adapter<RecordingFileAdapter.ViewHolder> {
    List<RecordingFileData> recordingFileDataList;

    public RecordingFileAdapter(List<RecordingFileData> recordingFileDataList) {
        this.recordingFileDataList = recordingFileDataList;
    }
    public void updateList(List<RecordingFileData> recordingFileDataList)
    {
        this.recordingFileDataList=recordingFileDataList;
    }

    @NonNull
    @Override
    public RecordingFileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingFileAdapter.ViewHolder holder, int position) {
        holder.tvFileName.setText(recordingFileDataList.get(position).getFileName());
    }

    @Override
    public int getItemCount() {
        return recordingFileDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
        }
    }
}
