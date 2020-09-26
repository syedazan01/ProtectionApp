package atoz.protection.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import atoz.protection.R;
import atoz.protection.adapters.WalletHistoryAdapter;
import atoz.protection.model.UserBean;
import atoz.protection.model.WalletHistory;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;

public class Wallet extends AppCompatActivity implements ValueEventListener {
    private ProgressDialog pd;
    private Activity activity=this;

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        swipeRefreshLayout.setRefreshing(false);
        if (myWalletFirebase == MyWalletFirebase.FIRST_TIME) {
            myWalletFirebase = MyWalletFirebase.SECOND_TIME;
            UserBean userBean=dataSnapshot.getValue(UserBean.class);
            if(userBean!=null)
            {
                Log.e("WALLET>>>",userBean.getWalletAmount()+"");
                tvCurrentAmount.setText("\u20B9 "+userBean.getWalletAmount());
            }
            }

        pd.dismiss();
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        pd.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }

    private enum MyWalletFirebase {
        FIRST_TIME, SECOND_TIME
    }

    private enum WalletHistoryFirebase {
        LOAD, NO_LOAD
    }

    private ImageView ivBack;
    private RecyclerView rvWalletHistory;
    private CardView cardMyWallet;
    private WalletHistoryAdapter walletHistoryAdapter;
    TextView tvNoData, tvWalletHistory,tvCurrentAmount,tvWithdrawal;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyWalletFirebase myWalletFirebase=MyWalletFirebase.FIRST_TIME;
    private WalletHistoryFirebase walletHistoryFirebase=WalletHistoryFirebase.LOAD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initViews();
        initActions();
    }


    private void initViews() {

        pd = Utils.getProgressDialog(this);
        tvWithdrawal = findViewById(R.id.tvWithdrawal);
        ivBack = findViewById(R.id.ivBack);
        tvCurrentAmount = findViewById(R.id.tvCurrentAmount);
        tvNoData = findViewById(R.id.tvNoData);
        tvWalletHistory = findViewById(R.id.tvWalletHistory);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        cardMyWallet = findViewById(R.id.cardMyWallet);
        rvWalletHistory = findViewById(R.id.rvWalletHistory);
        walletHistoryAdapter = new WalletHistoryAdapter();
        rvWalletHistory.setAdapter(walletHistoryAdapter);
        cardMyWallet.setBackground(Utils.getThemeGradient(50F));
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Wallet");
        pd.show();
        Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(this);

        Utils.getMyWalletHistoryReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(walletHistoryFirebase== WalletHistoryFirebase.LOAD)
                {
                    walletHistoryFirebase=WalletHistoryFirebase.NO_LOAD;
                    List<WalletHistory> walletHistoryList=new ArrayList<>();
                    Log.e("HISTORY>>>",dataSnapshot.getValue()+"");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        try {
                            WalletHistory walletHistory=snapshot.getValue(WalletHistory.class);
                            if(walletHistory!=null && walletHistory.getWalletmobile().equals(PrefManager.getString(AppConstant.USER_MOBILE)))
                                walletHistoryList.add(walletHistory);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(walletHistoryList.size()==0)
                    {
                        tvWalletHistory.setVisibility(View.GONE);
                        rvWalletHistory.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvWalletHistory.setVisibility(View.VISIBLE);
                        rvWalletHistory.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    }
                    walletHistoryAdapter.submitList(walletHistoryList);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void initActions() {
        tvWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount=Integer.parseInt(tvCurrentAmount.getText().toString().trim().replaceAll("[\u20B9 ]",""));
                Log.e("AMOUNT>>>",amount+"");
                if(amount<100)
                    Utils.showToast(activity,"Amount should be equal or greater than \u20B9 100",AppConstant.errorColor);
                else
                {
                    Intent intent=new Intent(activity,WithdrawalRequest.class);
                    intent.putExtra("walletAmount",amount);
                    startActivity(intent);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWalletFirebase=MyWalletFirebase.FIRST_TIME;
                walletHistoryFirebase=WalletHistoryFirebase.LOAD;
                Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(Wallet.this);

                Utils.getMyWalletHistoryReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(walletHistoryFirebase== WalletHistoryFirebase.LOAD)
                        {
                            walletHistoryFirebase=WalletHistoryFirebase.NO_LOAD;
                            List<WalletHistory> walletHistoryList=new ArrayList<>();
                            Log.e("HISTORY>>>",dataSnapshot.getValue()+"");
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                try {
                                    WalletHistory walletHistory=snapshot.getValue(WalletHistory.class);
                                    if(walletHistory!=null && walletHistory.getWalletmobile().equals(PrefManager.getString(AppConstant.USER_MOBILE)))
                                        walletHistoryList.add(walletHistory);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if(walletHistoryList.size()==0)
                            {
                                tvWalletHistory.setVisibility(View.GONE);
                                rvWalletHistory.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tvWalletHistory.setVisibility(View.VISIBLE);
                                rvWalletHistory.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                            }
                            walletHistoryAdapter.submitList(walletHistoryList);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}