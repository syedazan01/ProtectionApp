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
import atoz.protection.model.AtmBean;

import java.util.List;


public class AtmAdapter extends RecyclerView.Adapter<AtmAdapter.AdhaarHolder> {
    private Activity activity;
    private List<AtmBean> atmBeanList;
    private DocumentClickListener documentClickListener;

    public AtmAdapter(Activity activity, List<AtmBean> atmBeanList, DocumentClickListener documentClickListener) {
        this.activity = activity;
        this.atmBeanList = atmBeanList;
        this.documentClickListener = documentClickListener;
    }

    @NonNull
    @Override
    public AdhaarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atm_document_item, parent, false);
        return new AdhaarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdhaarHolder holder, final int position) {
        AtmBean atmBean = atmBeanList.get(position);
//        int color = Color.argb(200, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150, new Random().nextInt(100) + 150);
//        holder.constMain.setBackgroundColor(color);
        holder.atmHolderName.setText(atmBean.getNameoncard());
        holder.atmDate.setText(atmBean.getCardVailidity());
        holder.atmAccountNumber.setText(atmBean.getAtmnumber());
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentClickListener.onSelectAtm(atmBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return atmBeanList.size();
    }

    public class AdhaarHolder extends RecyclerView.ViewHolder {
        CardView cardMain;
        TextView atmHolderName,atmDate,atmAccountNumber;


        public AdhaarHolder(@NonNull View itemView) {
            super(itemView);
            atmHolderName = itemView.findViewById(R.id.atmHolderName);
            atmDate = itemView.findViewById(R.id.atmDate);
            atmAccountNumber = itemView.findViewById(R.id.atmAccountNumber);
            cardMain = itemView.findViewById(R.id.cardMain);
        }
    }
}
