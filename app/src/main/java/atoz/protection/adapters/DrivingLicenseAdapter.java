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
import atoz.protection.model.DlicenceBean;

import java.util.List;


public class DrivingLicenseAdapter extends RecyclerView.Adapter<DrivingLicenseAdapter.AdhaarHolder> {
    private Activity activity;
    private List<DlicenceBean> dlicenceBeans;
    private DocumentClickListener documentClickListener;

    public DrivingLicenseAdapter(Activity activity, List<DlicenceBean> dlicenceBeans, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.dlicenceBeans = dlicenceBeans;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dl_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        DlicenceBean dlicenceBean = dlicenceBeans.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.dlName.setText(dlicenceBean.getFullname());
        holder.dlFName.setText(dlicenceBean.getSon_of());
        holder.dlNumber.setText(dlicenceBean.getLicenceNumber());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectDL(dlicenceBeans.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dlicenceBeans.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        TextView dlName,dlFName,dlNumber;
        CardView cardMain;

        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            dlName = itemView.findViewById(R.id.dlName);
            dlFName = itemView.findViewById(R.id.dlFName);
            dlNumber = itemView.findViewById(R.id.dlNumber);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
