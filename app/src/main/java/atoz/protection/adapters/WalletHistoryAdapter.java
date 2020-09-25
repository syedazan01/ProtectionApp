package atoz.protection.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import atoz.protection.R;
import atoz.protection.model.WalletHistory;
import atoz.protection.utils.AppConstant;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.ViewHolder> {
    private List<WalletHistory> walletHistoryList=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WalletHistory walletHistory=walletHistoryList.get(position);
        holder.tvMobile.setText(walletHistory.getMobile());
        if(walletHistory.getStatus().equals(AppConstant.WALLET_STATUS_WITHDRAWAL))
            holder.tvAmount.setTextColor(Color.RED);
        else
            holder.tvAmount.setTextColor(Color.GREEN);

        holder.tvAmount.setText("\u20B9 "+walletHistory.getAmount());
        holder.tvDateAndStatus.setText(walletHistory.getStatus()+" | "+walletHistory.getCreated());
    }

    @Override
    public int getItemCount() {
        return walletHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMobile,tvDateAndStatus,tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMobile=itemView.findViewById(R.id.tvMobile);
            tvDateAndStatus=itemView.findViewById(R.id.tvDateAndStatus);
            tvAmount=itemView.findViewById(R.id.tvAmount);
        }
    }
    public void submitList(List<WalletHistory> walletHistoryList)
    {
        this.walletHistoryList=walletHistoryList;
        notifyDataSetChanged();
    }
}
