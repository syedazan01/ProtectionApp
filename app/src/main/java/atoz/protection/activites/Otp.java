package atoz.protection.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import okhttp3.internal.Util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {
    Button otpsubmit;
    TextView tvSentMsg, tvToolbarTitle, tvResend;
    ImageView ivBack;
    PinView otp_View;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String verficationId;
    PhoneAuthProvider.ForceResendingToken resendToken;
    FirebaseAuth mAuth;
    Activity activity = Otp.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        handleDeepLink();
        initViews();
        initActions();
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String id = deepLink.getQueryParameter(AppConstant.INVITED_BY);
                            Log.e("LINK>>>", deepLink.getPath());
                            Log.e("LINK>>>", "" + id);
                            if (id != null) {
                                PrefManager.putString(AppConstant.INVITED_BY, id);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LINK>>>", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void initActions() {
        otp_View.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_View.getText().toString().length() == 6) {
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
        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(otp_View.getText().toString())) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationId, otp_View.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                } else {
                    Utils.showToast(activity, "Invalid Otp", AppConstant.errorColor);
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final ProgressDialog pd = Utils.getProgressDialog(activity);
                            pd.show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Otp Screen>>>", "signInWithCredential:success");
                            PrefManager.putString(AppConstant.USER_MOBILE, "+91" + getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
                            PrefManager.putBoolean(AppConstant.ISLOGGEDIN, true);
                            Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    pd.dismiss();
                                    UserBean userBean = dataSnapshot.getValue(UserBean.class);

                                    if (userBean == null)
                                        userBean = new UserBean();
                                    PrefManager.putBoolean(AppConstant.IS_SUBSCRIBE, userBean.isSubscribe());
                                    userBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                                    if (!PrefManager.getString(AppConstant.INVITED_BY).isEmpty() && (userBean.getReferBy() != null && userBean.getReferBy().isEmpty()) {
                                        PrefManager.putBoolean(AppConstant.ISREFERED, true);
                                        userBean.setReferBy(PrefManager.getString(AppConstant.INVITED_BY));
                                        userBean.setWalletAmount(userBean.getWalletAmount() + 10F);
                                    }
                                    userBean.setFcmToken(PrefManager.getString(AppConstant.FCMTOKEN));
                                    Utils.storeUserDetailsToRTD(userBean);
                                    finishAffinity();
                                    Intent intent = new Intent(activity, HomePage.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    pd.dismiss();
                                }
                            });

//                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
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
}
