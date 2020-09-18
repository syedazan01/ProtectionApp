package atoz.protection.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.interfacecallbacks.DocumentClickListener;
import atoz.protection.model.VoteridBean;

import java.util.List;


public class VoterIDAdapter extends RecyclerView.Adapter<VoterIDAdapter.AdhaarHolder> {
    private Activity activity;
    private List<VoteridBean> voteridBeanList;
    private DocumentClickListener documentClickListener;

    public VoterIDAdapter(Activity activity, List<VoteridBean> voteridBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.voteridBeanList = voteridBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voter_id_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        VoteridBean voteridBean = voteridBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.voterIdName.setText(voteridBean.getFullName());
        holder.voterIdFName.setText(voteridBean.getFathersName());
        holder.voterIDNumber.setText(voteridBean.getId()+" ");
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectVoterID(voteridBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return voteridBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView voterIdName,voterIdFName,voterIDNumber;
        CardView cardMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            voterIdName = itemView.findViewById(R.id.voterIdName);
            voterIdFName = itemView.findViewById(R.id.voterIdFName);
            voterIDNumber = itemView.findViewById(R.id.voterIDNumber);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
