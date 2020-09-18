package atoz.protection.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.model.MediaDocBean;
import atoz.protection.utils.AppConstant;

import java.util.List;

public class MediaDocsAdapter extends RecyclerView.Adapter<MediaDocsAdapter.ViewHolder> {
    private Activity activity;
    private List<MediaDocBean> mediaDocBeanList;
    private MediaDocsClickListener mediaDocsClickListener;

    public MediaDocsAdapter(Activity activity, List<MediaDocBean> mediaDocBeanList,MediaDocsClickListener mediaDocsClickListener) {
        this.activity = activity;
        this.mediaDocBeanList = mediaDocBeanList;
        this.mediaDocsClickListener = mediaDocsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_share_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaDocBean mediaDocBean=mediaDocBeanList.get(position);
        /*int redColor = new Random().nextInt(100) + 150;
        int greenColor = new Random().nextInt(100) + 150;
        int blueColor = new Random().nextInt(100) + 150;
        int startcolor = Color.argb(255, redColor, greenColor, blueColor);
        int endcolor = Color.argb(200, redColor, greenColor, blueColor);

        holder.cardFileShare.setBackground(Utils.getColoredDrawable(startcolor, endcolor));*/
        holder.tvSentTo.setText(mediaDocBean.getFileName());
        if(mediaDocBean.getDocType().equals(AppConstant.DOC_IMAGE))
            holder.ivType.setImageResource(R.drawable.cameradetector_logo);
        else
            holder.ivType.setImageResource(R.drawable.file);
        holder.cardFileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaDocsClickListener.onClick(mediaDocBeanList.get(position));
        }
        });
    }

    @Override
    public int getItemCount() {
        return mediaDocBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSentTo, tvMsg, tvCreatedDate;
        ImageView ivType;
        CardView cardFileShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSentTo = itemView.findViewById(R.id.tvsentTo);
            tvMsg = itemView.findViewById(R.id.tvSentMsg);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            ivType = itemView.findViewById(R.id.ivType);
            cardFileShare = itemView.findViewById(R.id.cardFileShare);
        }
    }
    public interface MediaDocsClickListener{
        void onClick(MediaDocBean mediaDocBean);
    }
}
