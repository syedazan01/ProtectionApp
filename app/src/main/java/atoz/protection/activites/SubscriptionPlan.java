package atoz.protection.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import atoz.protection.R;
import atoz.protection.adapters.AdapterSubscription;
import atoz.protection.billing.GooglePaySetup;
import atoz.protection.model.PlansBean;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlan extends AppCompatActivity implements AdapterSubscription.RecyclerViewClickListener {
    ImageView ivBack;
    CardView cardMyPlan;
    RecyclerView rvPlans;
    List<PlansBean> plansBeans=new ArrayList<>();
    private ProgressDialog pd;
    GooglePaySetup googlePaySetup;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);
        initViews();
        initActions();
    }

    private void initViews() {
        rvPlans = findViewById(R.id.rvPlans);
        cardMyPlan = findViewById(R.id.cardMyPlan);
        ivBack = findViewById(R.id.ivBack);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Premium Subscription");
        cardMyPlan.setBackground(Utils.getThemeGradient(50F));
        rvPlans.setLayoutManager(new LinearLayoutManager(this));
        googlePaySetup = new GooglePaySetup(this);
        try {
            Task<Boolean> task = googlePaySetup.readyToPay();
            task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful()) {
//Utils.showToast(getActivity(),"Success",AppConstant.succeedColor);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        pd=Utils.getProgressDialog(this);
        pd.show();
        Utils.getPlansReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                    PlansBean plansBean = postShot.getValue(PlansBean.class);
                    plansBeans.add(plansBean);
                }
                rvPlans.setAdapter(new AdapterSubscription(SubscriptionPlan.this, plansBeans, SubscriptionPlan.this));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSelectPlan(PlansBean plansBean) {
        try {
            final JSONObject paymentRequestJson = googlePaySetup.baseConfigurationJson();
            paymentRequestJson.put("transactionInfo", new JSONObject()
                    .put("totalPrice", plansBean.getPlanPrice().replace("\u20B9", ""))
                    .put("totalPriceStatus", "FINAL")
                    .put("currencyCode", "INR")
            );
            paymentRequestJson.put("merchantInfo", new JSONObject()
                    .put("merchantId", "BCR2DN6TVOM7ZAI4")
                    .put("merchantName", "SANJAY")

            );
            final PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequestJson.toString());
            AutoResolveHelper.resolveTask(googlePaySetup.paymentsClient.loadPaymentData(request),this, LOAD_PAYMENT_DATA_REQUEST_CODE
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            PaymentData paymentData = PaymentData.getFromIntent(data);
//                String json = data.toJSon()
            try {
                JSONObject paymentMethodData = new JSONObject(paymentData.toJson())
                        .getJSONObject("paymentMethodData");
                String paymentToken = paymentMethodData
                        .getJSONObject("tokenizationData")
                        .getString("token");
                Log.e("payment??>>", paymentToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        } else if (resultCode == 1 && requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            Log.e("dfvbdfbdfbn", status.getStatusMessage());
            return;
        }
    }
}