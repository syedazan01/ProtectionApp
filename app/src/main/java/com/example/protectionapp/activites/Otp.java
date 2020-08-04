package com.example.protectionapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    Button otpsubmit;
    TextView tvSentMsg, tvToolbarTitle, tvResend;
    ImageView ivBack;
    Pinview otp_View;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String verficationId;
    PhoneAuthProvider.ForceResendingToken resendToken;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initViews();
        initActions();

    }

    private void initActions() {
        initOtpCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE), 30, TimeUnit.SECONDS, this, callbacks);
        tvResend.setVisibility(View.VISIBLE);
        tvResend.setEnabled(false);
        otpSendTimer();
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(verficationId,30L,TimeUnit.SECONDS,Otp.this,callbacks,resendToken);
                tvResend.setEnabled(false);
                otpSendTimer();
            }
        });
        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(otp_View.getValue())) {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verficationId,otp_View.getValue());
                    signInWithPhoneAuthCredential(credential);

                }
                else
                {
                    Utils.showToast(Otp.this,"Invalid Otp",AppConstant.errorColor);
                }
            }
        });
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
                    otp_View.setValue(otp);
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
                Utils.showToast(Otp.this, getResources().getString(R.string.sent_msg) + " " + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE), AppConstant.succeedColor);

            }
        }
        ;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Otp Screen>>>", "signInWithCredential:success");
                            PrefManager.putString(AppConstant.USER_MOBILE,"+91"+getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
                            PrefManager.putBoolean(AppConstant.ISLOGGEDIN,true);
                            UserBean userBean=new UserBean();
                            userBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                            userBean.setProfilePic("");
                            Utils.storeUserDetailsToRTD(Otp.this,userBean);
                            finishAffinity();
                            Intent intent = new Intent(Otp.this, HomePage.class);
                            startActivity(intent);
//                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Otp Screen>>>", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Utils.showToast(Otp.this, "The verification code entered was invalid", AppConstant.errorColor);
                            }
                        }
                    }
                });
    }

    private void initViews() {
        otpsubmit = findViewById(R.id.otpsubmitbtn);
        tvSentMsg = findViewById(R.id.tvSentMsg);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        otp_View = findViewById(R.id.otp_View);
        tvResend = findViewById(R.id.tvResend);
        tvToolbarTitle.setText("Otp");
        tvSentMsg.setText(getResources().getString(R.string.sent_msg) + " " + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
        Utils.makeButton(otpsubmit, getResources().getColor(R.color.colorPrimary), 50F);
    }

    private void otpSendTimer() {
        new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvResend.setText("00: " + TimeUnit.MILLISECONDS.toSeconds(l));
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setText("Resend");
            }
        }.start();
    }
}
