package atoz.protection.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import atoz.protection.R;
import atoz.protection.adapters.AdapterSubscription;
import atoz.protection.model.PlansBean;
import atoz.protection.model.UserBean;
import atoz.protection.model.WalletHistory;
import atoz.protection.room.AppDatabase;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static atoz.protection.utils.Utils.getColoredDrawable;
import static atoz.protection.utils.Utils.getThemeGradient;

public class SubscriptionPlan extends AppCompatActivity implements AdapterSubscription.RecyclerViewClickListener {
    private Activity activity=this;

    private enum MyPlan{
        Load,Loaded
    }
    private enum Plans{
        Load,Loaded
    }
    ImageView ivBack;
    CardView cardMyPlan;
    RecyclerView rvPlans;
    List<PlansBean> plansBeans=new ArrayList<>();
    private ProgressDialog pd;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 300;
    SwipeRefreshLayout swipeRefreshLayout;
    private MyPlan myPlan=MyPlan.Load;
    private Plans plans=Plans.Load;
    private TextView tvScheme,tvPeriod,tvPrice;
    private String remainingDate="",planName="";
    private int planPrice=0;
    private int walletAmount=0;
    private int remainingAmount=0;
    private AdapterSubscription adapterSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);
        initViews();
        initActions();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvScheme = findViewById(R.id.tvScheme);
        tvPeriod = findViewById(R.id.tvPeriod);
        tvPrice = findViewById(R.id.tvPrice);
        rvPlans = findViewById(R.id.rvPlans);
        cardMyPlan = findViewById(R.id.cardMyPlan);
        ivBack = findViewById(R.id.ivBack);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Premium Subscription");
        cardMyPlan.setBackground(getThemeGradient(50F));
        rvPlans.setLayoutManager(new LinearLayoutManager(this));
        adapterSubscription= new AdapterSubscription(SubscriptionPlan.this, SubscriptionPlan.this);
        rvPlans.setAdapter(adapterSubscription);
        pd=Utils.getProgressDialog(this);
        pd.show();
        Utils.getPlansReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                if (plans==Plans.Load) {
                    plans=Plans.Loaded;
                    plansBeans.clear();
                    for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                        PlansBean plansBean = postShot.getValue(PlansBean.class);
                        plansBeans.add(plansBean);
                    }
                    adapterSubscription.submitList(plansBeans);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });

            Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (myPlan==MyPlan.Load) {
                        myPlan=MyPlan.Loaded;
                        UserBean userBean=dataSnapshot.getValue(UserBean.class);
                        if(userBean!=null)
                        {
                            walletAmount = userBean.getWalletAmount();
                            if(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                                cardMyPlan.setVisibility(View.VISIBLE);

                                tvScheme.setText(userBean.getSchemeName());
                                tvPrice.setText("\u20B9 " + userBean.getPlanPrice());
                                if (userBean.getPlanPurchaseDate()!=null && !userBean.getPlanPurchaseDate().isEmpty()) {
                                    if (!Utils.getDaysLeft(userBean.getPlanPurchaseDate()).equalsIgnoreCase("")) {
                                        tvPeriod.setText(Utils.getDaysLeft(userBean.getPlanPurchaseDate()));
                                    } else {
                                        PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE,false);
                                        cardMyPlan.setVisibility(View.GONE);
                                    }
                                }
                                else
                                {
                                    PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE,false);
                                    cardMyPlan.setVisibility(View.GONE);
                                }
                                userBean.setSubscribe(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE));
                                Utils.storeUserDetailsToRTD(userBean);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
    }

    private void initActions() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                plans=Plans.Load;
                myPlan=MyPlan.Loaded;

                    Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (myPlan==MyPlan.Load) {
                                myPlan=MyPlan.Loaded;
                                UserBean userBean=dataSnapshot.getValue(UserBean.class);
                                if(userBean!=null) {
                                    walletAmount = userBean.getWalletAmount();
                                    if (PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                                        cardMyPlan.setVisibility(View.VISIBLE);

                                        tvScheme.setText(userBean.getSchemeName());
                                        tvPrice.setText("\u20B9 " + userBean.getPlanPrice());
                                        if (!userBean.getPlanPurchaseDate().isEmpty()) {
                                            if (!Utils.getDaysLeft(userBean.getPlanPurchaseDate()).equalsIgnoreCase("")) {
                                                tvPeriod.setText(Utils.getDaysLeft(userBean.getPlanPurchaseDate()));
                                            } else {
                                                cardMyPlan.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                Utils.getPlansReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pd.dismiss();
                        swipeRefreshLayout.setRefreshing(false);

                        if (plans==Plans.Load) {
                            plansBeans.clear();
                            plans=Plans.Loaded;
                            for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                                PlansBean plansBean = postShot.getValue(PlansBean.class);
                                plansBeans.add(plansBean);
                            }
                            adapterSubscription.submitList(plansBeans);
                             }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        pd.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onSelectPlan(PlansBean plansBean) {
        if (!PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
            int planvalidity=plansBean.getPlan_duration();
            Date planDate= new Date();
            if((planDate.getMonth())+planvalidity>12)
                planDate.setYear(planDate.getYear()+1);
            planDate.setMonth((planDate.getMonth() - 1 + planvalidity) % 12 + 1);
            remainingDate=new SimpleDateFormat("dd/MMM/yyyy",Locale.getDefault()).format(planDate);
            planPrice=plansBean.getPlanPrice();
            planName=plansBean.getPlan_name();
            showPlansDialog();
        }
        else
            Utils.showToast(activity,"You have already your plan",AppConstant.errorColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case AppConstant.UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (Utils.isOnline(this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Utils.showToast(activity,"Transaction successful.",AppConstant.succeedColor);
                Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserBean userBean=dataSnapshot.getValue(UserBean.class);
                        if(userBean!=null)
                        {
                            userBean.setSubscribe(true);
                            PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE,true);
                            userBean.setPlanPrice(planPrice);
                            userBean.setWalletAmount(0);
                            userBean.setPlanPurchaseDate(remainingDate);
                            userBean.setSchemeName(planName);
                            userBean.setPlanPurchaseDate(remainingDate);

                            walletAmount = userBean.getWalletAmount();
                            if(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                                cardMyPlan.setVisibility(View.VISIBLE);

                                tvScheme.setText(userBean.getSchemeName());
                                tvPrice.setText("\u20B9 " + userBean.getPlanPrice());
                                if (userBean.getPlanPurchaseDate() != null && !userBean.getPlanPurchaseDate().isEmpty()) {
                                    if (!Utils.getDaysLeft(userBean.getPlanPurchaseDate()).equalsIgnoreCase("")) {
                                        tvPeriod.setText(Utils.getDaysLeft(userBean.getPlanPurchaseDate()));
                                    } else {
                                        PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, false);
                                        cardMyPlan.setVisibility(View.GONE);
                                    }
                                } else {
                                    PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, false);
                                    cardMyPlan.setVisibility(View.GONE);
                                }
                                userBean.setSubscribe(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE));
                            }
                            Utils.storeUserDetailsToRTD(userBean);

                            WalletHistory walletHistory = new WalletHistory();
                            walletHistory.setWalletmobile(PrefManager.getString(AppConstant.USER_MOBILE));
                            walletHistory.setMobile("Plan Purchase");
                            walletHistory.setAmount(remainingAmount);
                            walletHistory.setStatus(AppConstant.WALLET_STATUS_PLAN_PURCHASE);
                            walletHistory.setCreated(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
                            Utils.storeWalletHistory(walletHistory);
                        }
                }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Utils.showToast(activity, "Payment cancelled by user.",AppConstant.errorColor);
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Utils.showToast(activity, "Transaction failed.Please try again",AppConstant.errorColor);
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Utils.showToast(activity, "Internet connection is not available. Please check and try again",AppConstant.errorColor);
        }
    }
    public  void showPlansDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.plans_dialog);
        Button btnOk = bottomSheetDialog.findViewById(R.id.btnOK);
        Button btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        btnOk.setBackground(getThemeGradient(50F));
        btnCancel.setBackground(getColoredDrawable(activity.getResources().getColor(R.color.black), activity.getResources().getColor(R.color.black), GradientDrawable.RECTANGLE, 50F));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int payAmount;
                Log.e("AMOUNT>>>",planPrice+" "+walletAmount);
                if(planPrice>walletAmount)
                {
                    payAmount=planPrice-walletAmount;
                    remainingAmount=payAmount;
                    Utils.setupGooglePay(activity,String.valueOf(payAmount));
                }
                else
                {
                    myPlan=MyPlan.Load;
                    payAmount=walletAmount-planPrice;
                    Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(myPlan==MyPlan.Load) {
                                myPlan=MyPlan.Loaded;
                                UserBean userBean = dataSnapshot.getValue(UserBean.class);
                                if (userBean != null) {
                                    userBean.setSubscribe(true);
                                    PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, true);
                                    userBean.setPlanPrice(planPrice);
                                    userBean.setWalletAmount(payAmount);
                                    userBean.setPlanPurchaseDate(remainingDate);
                                    userBean.setSchemeName(planName);
                                    userBean.setPlanPurchaseDate(remainingDate);
                                    walletAmount = userBean.getWalletAmount();
                                    if(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                                        cardMyPlan.setVisibility(View.VISIBLE);

                                        tvScheme.setText(userBean.getSchemeName());
                                        tvPrice.setText("\u20B9 " + userBean.getPlanPrice());
                                        if (userBean.getPlanPurchaseDate() != null && !userBean.getPlanPurchaseDate().isEmpty()) {
                                            if (!Utils.getDaysLeft(userBean.getPlanPurchaseDate()).equalsIgnoreCase("")) {
                                                tvPeriod.setText(Utils.getDaysLeft(userBean.getPlanPurchaseDate()));
                                            } else {
                                                PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, false);
                                                cardMyPlan.setVisibility(View.GONE);
                                            }
                                        } else {
                                            PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, false);
                                            cardMyPlan.setVisibility(View.GONE);
                                        }
                                        userBean.setSubscribe(PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE));
                                    }
                                    Utils.storeUserDetailsToRTD(userBean);
                                    Utils.showToast(activity, "Transaction successful.", AppConstant.succeedColor);

                                    WalletHistory walletHistory = new WalletHistory();
                                    walletHistory.setWalletmobile(PrefManager.getString(AppConstant.USER_MOBILE));
                                    walletHistory.setMobile("Plan Purchase");
                                    walletHistory.setAmount(planPrice);
                                    walletHistory.setStatus(AppConstant.WALLET_STATUS_PLAN_PURCHASE);
                                    walletHistory.setCreated(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
                                    Utils.storeWalletHistory(walletHistory);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                bottomSheetDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
}