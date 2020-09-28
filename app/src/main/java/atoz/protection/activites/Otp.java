package atoz.protection.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import atoz.protection.R;
import atoz.protection.model.UserBean;
import atoz.protection.model.WalletHistory;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity implements ValueEventListener {

    Firebase reference;
    Button otpsubmit;
    TextView tvSentMsg, tvToolbarTitle, tvResend;
    ImageView ivBack;
    PinView otp_View;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String verficationId;
    PhoneAuthProvider.ForceResendingToken resendToken;
    FirebaseAuth mAuth;
    Activity activity = Otp.this;
    ProgressDialog pd;
    private View.OnClickListener otpSubmitListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!TextUtils.isEmpty(otp_View.getText().toString())) {
                PhoneAuthCredential credential = null;
                try {
                    credential = PhoneAuthProvider.getCredential(verficationId, otp_View.getText().toString());
                    otpsubmit.setOnClickListener(null);
                    signInWithPhoneAuthCredential(credential);
                } catch (Exception e) {
                    e.printStackTrace();
                    otpsubmit.setOnClickListener(otpSubmitListener);
                    Utils.showToast(activity,"Try Again",AppConstant.errorColor);
                }

            } else {
                Utils.showToast(activity, "Invalid Otp", AppConstant.errorColor);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initViews();
        initActions();
    }


    private void initActions() {
        otp_View.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_View.getText().toString().length() == 6) {
                    Utils.hideKeyboardFrom(activity,otp_View);
                    otpsubmit.setBackground(Utils.getThemeGradient(50F));
                } else {
                    Utils.makeButton(otpsubmit, getResources().getColor(R.color.grey), 50F);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initOtpCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE), 30, TimeUnit.SECONDS, this, callbacks);
        tvResend.setVisibility(View.VISIBLE);
        tvResend.setEnabled(false);
        otpSendTimer();
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE), 30L, TimeUnit.SECONDS, activity, callbacks);
                tvResend.setEnabled(false);
                otpSendTimer();
            }
        });
        otpsubmit.setOnClickListener(otpSubmitListener);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initOtpCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String otp = phoneAuthCredential.getSmsCode();
                if (otp != null) {
                    otp_View.setText(otp);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verficationId = s;
                resendToken = forceResendingToken;
                Utils.showToast(activity, getResources().getString(R.string.sent_msg) + " " + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE), AppConstant.succeedColor);

            }
        }
        ;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        otpsubmit.setOnClickListener(null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pd.show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Otp Screen>>>", "signInWithCredential:success");
                            PrefManager.putString(AppConstant.USER_MOBILE, "+91" + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
                            PrefManager.putBoolean(AppConstant.ISLOGGEDIN, true);

                            reference.child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(Otp.this);

//                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            otpsubmit.setOnClickListener(otpSubmitListener);
                            // Sign in failed, display a message and update the UI
                            Log.w("Otp Screen>>>", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Utils.showToast(activity, "The verification code entered was invalid", AppConstant.errorColor);
                            }
                        }
                    }
                });
    }

    private void initViews() {
        reference = Utils.getUserReference();
        pd = Utils.getProgressDialog(activity);
        otpsubmit = findViewById(R.id.otpsubmitbtn);
        tvSentMsg = findViewById(R.id.tvSentMsg);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        otp_View = findViewById(R.id.otp_View);
        tvResend = findViewById(R.id.tvResend);
        tvToolbarTitle.setText("Otp");
        tvSentMsg.setText(getResources().getString(R.string.sent_msg) + " " + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
        Utils.makeButton(otpsubmit, getResources().getColor(R.color.grey), 50F);

    }

    private void otpSendTimer() {
        new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvResend.setText("Resend code in 00: " + TimeUnit.MILLISECONDS.toSeconds(l) + " sec.");
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setText("Resend");
            }
        }.start();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        pd.dismiss();
        otpsubmit.setOnClickListener(otpSubmitListener);
        UserBean userBean=dataSnapshot.getValue(UserBean.class);
        if (userBean==null) {
            userBean=new UserBean();
            if (!PrefManager.getString(AppConstant.INVITED_BY).isEmpty()) {
                PrefManager.putBoolean(AppConstant.ISREFERED, true);
                userBean.setReferBy(PrefManager.getString(AppConstant.INVITED_BY));
                userBean.setWalletAmount(userBean.getWalletAmount() + 10);
                WalletHistory walletHistory=new WalletHistory();
                walletHistory.setWalletmobile(PrefManager.getString(AppConstant.USER_MOBILE));
                walletHistory.setMobile(PrefManager.getString(AppConstant.INVITED_BY));
                walletHistory.setAmount(10);
                walletHistory.setStatus(AppConstant.WALLET_STATUS_RECIEVED);
                walletHistory.setCreated(new SimpleDateFormat("dd MMM yyyy").format(new Date()));
                Utils.storeWalletHistory(walletHistory);
            }
        }
        PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, userBean.isSubscribe());
        userBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));

        userBean.setFcmToken(PrefManager.getString(AppConstant.FCMTOKEN));
        Utils.storeUserDetailsToRTD(userBean);
        reference.onDisconnect();
        finishAffinity();
        Intent intent = new Intent(activity, HomePage.class);
        startActivity(intent);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        pd.dismiss();
    }
}
