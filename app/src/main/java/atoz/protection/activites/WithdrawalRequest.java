package atoz.protection.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import atoz.protection.R;
import atoz.protection.model.PayRequestBean;
import atoz.protection.model.WalletHistory;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;

public class WithdrawalRequest extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvToolbarTitle;
    private EditText etAccountName, etUpiId, etAmount;
    private Button payRequest;
    private Activity activity=this;
    private int walletAmount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_request);
        initViews();
        initActions();
    }

    private void initActions() {
    ivBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    });
    payRequest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(TextUtils.isEmpty(etAccountName.getText()))
            {
                Utils.showToast(activity,"Enter Google Pay Account Name", AppConstant.errorColor);
                etAccountName.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(etUpiId.getText()))
            {
                Utils.showToast(activity,"Enter Valid Google Pay UPI ID", AppConstant.errorColor);
                etUpiId.requestFocus();
                return;
            }
            WalletHistory walletHistory=new WalletHistory();
            walletHistory.setWalletmobile(PrefManager.getString(AppConstant.USER_MOBILE));
            walletHistory.setMobile("Withdrawal Request");
            walletHistory.setAmount(walletAmount);
            walletHistory.setStatus(AppConstant.WALLET_STATUS_WITHDRAWAL);
            walletHistory.setCreated(new SimpleDateFormat("dd MMM yyyy ", Locale.getDefault()).format(new Date()));
            Utils.storeWalletHistory(walletHistory);

            PayRequestBean payRequestBean=new PayRequestBean();
            payRequestBean.setRegisteredMobile(PrefManager.getString(AppConstant.USER_MOBILE));
            payRequestBean.setAccountName(etAccountName.getText().toString());
            payRequestBean.setAmount(walletAmount);
            payRequestBean.setUpiId(etUpiId.getText().toString());
            payRequestBean.setPayrequestDateAndTime(new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
            Utils.storeWithodrawalRequest(payRequestBean);
            finish();
        }
    });
    }
    private void initViews() {
        walletAmount=getIntent().getIntExtra("walletAmount",0);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        etAccountName = findViewById(R.id.etAccountName);
        etUpiId = findViewById(R.id.etUpi);
        etAmount = findViewById(R.id.etAmount);
        payRequest = findViewById(R.id.payRequest);

        tvToolbarTitle.setText("Withdrawal Request");

        payRequest.setBackground(Utils.getThemeGradient(50F));
    }
}