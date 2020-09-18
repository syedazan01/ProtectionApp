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
import atoz.protection.model.BirthCertificateBean;

import java.util.List;

public class BirthCertificateAdapter extends RecyclerView.Adapter<BirthCertificateAdapter.BirthCerificateHolder> {
    private Activity activity;
    private List<BirthCertificateBean> birthCertificateBeanList;
    private DocumentClickListener documentClickListener;

    public BirthCertificateAdapter(Activity activity, List<BirthCertificateBean> birthCertificateBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.birthCertificateBeanList = birthCertificateBeanList;
        this.documentClickListener = documentClickListener;
    }


    @NonNull
    @Override
    public BirthCertificateAdapter.BirthCerificateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birth_document_item, parent, false);
        return new BirthCerificateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthCertificateAdapter.BirthCerificateHolder holder, int position) {
        BirthCertificateBean birthCertificateBean = birthCertificateBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.birthName.setText(birthCertificateBean.getChildname());
        holder.birthFName.setText(birthCertificateBean.getFathername());
        holder.dobDate.setText(birthCertificateBean.getDateofbirth());
        holder.motherName.setText(birthCertificateBean.getMothername());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectBirthCertificate(birthCertificateBeanList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return birthCertificateBeanList.size();
    }

    public class BirthCerificateHolder extends RecyclerView.ViewHolder {
        CardView cardMain;
        TextView birthName,birthFName,motherName,dobDate;

        public BirthCerificateHolder(@NonNull View itemView) {
            super(itemView);
            cardMain=itemView.findViewById(R.id.cardMain);
            birthName=itemView.findViewById(R.id.birthName);
            birthFName=itemView.findViewById(R.id.birthFName);
            motherName=itemView.findViewById(R.id.motherName);
            dobDate=itemView.findViewById(R.id.dobDate);
        }
    }
}
