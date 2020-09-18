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
import atoz.protection.model.PassportBean;

import java.util.List;

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.PassportHolder> {
    private Activity activity;
    private List<PassportBean> passportBeanList;
    private DocumentClickListener documentClickListener;

    public PassportAdapter(Activity activity, List<PassportBean> passportBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.passportBeanList = passportBeanList;
        this.documentClickListener = documentClickListener;
    }


    @NonNull
    @Override
    public PassportAdapter.PassportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passport_document_item, parent, false);
        return new PassportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassportAdapter.PassportHolder holder, int position) {
        PassportBean passportBean = passportBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.passportName.setText(passportBean.getFullname());
        holder.passportFName.setText(passportBean.getNationalty());
        holder.passportNumber.setText(passportBean.getPassportnumber());
       holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectPassport(passportBeanList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return passportBeanList.size();
    }

    public class PassportHolder extends RecyclerView.ViewHolder {
        CardView cardMain;
        TextView passportName,passportFName,passportNumber;

        public PassportHolder(@NonNull View itemView) {
            super(itemView);
            cardMain=itemView.findViewById(R.id.cardMain);
            passportName=itemView.findViewById(R.id.passportName);
            passportFName=itemView.findViewById(R.id.passportFName);
            passportNumber=itemView.findViewById(R.id.passportNumber);
        }
    }
}
