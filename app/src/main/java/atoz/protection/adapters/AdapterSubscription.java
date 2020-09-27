package atoz.protection.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.model.PlansBean;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubscription extends RecyclerView.Adapter<AdapterSubscription.SubscribeHolder> {
    private Activity activity;
    private List<PlansBean> plansBeans=new ArrayList<>();
    private RecyclerViewClickListener recyclerViewClickListener;

    public AdapterSubscription(Activity activity, RecyclerViewClickListener recyclerViewClickListener) {
        this.activity = activity;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }
public void submitList(List<PlansBean> plansBeans)
{
    this.plansBeans=plansBeans;
    notifyDataSetChanged();
}
    @NonNull
    @Override
    public AdapterSubscription.SubscribeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribtion_item, parent, false);
        return new SubscribeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubscription.SubscribeHolder holder, int position) {
        PlansBean plansBean = plansBeans.get(position);
        holder.tvPrice.setText("\u20B9 "+plansBean.getPlanPrice());
        holder.tvPeriod.setText("For "+plansBean.getPlan_duration()+" Months");
        holder.tvScheme.setText(plansBean.getPlan_name());
    }

    @Override
    public int getItemCount() {
        return plansBeans.size();
    }

    public interface RecyclerViewClickListener {
        void onSelectPlan(PlansBean plansBean);
    }

    public class SubscribeHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvPeriod, tvScheme;

        public SubscribeHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvScheme = itemView.findViewById(R.id.tvScheme);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onSelectPlan(plansBeans.get(getAdapterPosition()));
                }
            });
        }
    }
}
