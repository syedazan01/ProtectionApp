package atoz.protection.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.model.RecordingFileData;
import atoz.protection.room.AppDatabase;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class RecordingFileAdapter extends RecyclerView.Adapter<RecordingFileAdapter.ViewHolder> {
    public static atoz.protection.interfacecallbacks.onPlay onPlay=null;
    List<RecordingFileData> recordingFileDataList;
    Activity activity;

    public RecordingFileAdapter(List<RecordingFileData> recordingFileDataList,Activity activity) {
        this.recordingFileDataList = recordingFileDataList;
        this.activity=activity;

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
    public void onBindViewHolder(@NonNull final RecordingFileAdapter.ViewHolder holder, final int position) {
       final RecordingFileData recordingFileData=recordingFileDataList.get(position);
        holder.tvFileName.setText(recordingFileData.getFileName());

        Long Seconds = recordingFileData.getEllipseMillis() / 1000 % 60;
        Long Minutes = recordingFileData.getEllipseMillis() / (60 * 1000) % 60;
        String minuteString=Minutes+"";
        String secondString=Seconds+"";
        if(Minutes<10L)
        {
            minuteString="0"+Minutes;
        }
        if(Seconds<10L)
        {
            secondString="0"+Seconds;
        }
        String total=minuteString+" : "+secondString;
        holder.tvDuration.setText(total);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onPlay!=null)
                {
                    onPlay.playMusic(recordingFileData);
                }
            }
        });
        /*holder.llMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (holder.ivDelete.getVisibility()== View.GONE) {
//                    holder.ivPlay.setVisibility(View.GONE);
                    holder.ivDelete.setVisibility(View.VISIBLE);
                    return true;
                }
              return false;
            }
        });*/
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getAppDataBase(activity).getRecordFileDao().deleteRecord(recordingFileData.getId());

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                File mFile=new File(recordingFileData.getFilePath());
                                if(mFile.exists())
                                    mFile.delete();
                                recordingFileDataList.remove(recordingFileData);
                                holder.ivDelete.setVisibility(View.GONE);
//                                holder.ivPlay.setVisibility(View.VISIBLE);
                                notifyItemRemoved(position);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordingFileDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName,tvDuration;
        LinearLayout llMain;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            llMain = itemView.findViewById(R.id.llMain);
//            ivPlay = itemView.findViewById(R.id.ivPlay);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}