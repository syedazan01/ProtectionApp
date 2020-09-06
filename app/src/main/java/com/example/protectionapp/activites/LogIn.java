package com.example.protectionapp.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LogIn extends AppCompatActivity {
EditText et_phone_number;
    Button OtpButton;
    Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
//            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.login_page);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful())
                    PrefManager.putString(AppConstant.FCMTOKEN, task.getResult().getToken());
            }
        });
        initViews();
        initActions();
    }

    private void initActions() {
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_phone_number.getText().toString().length() >= 10) {
                    OtpButton.setBackground(Utils.getThemeGradient(50F));
                } else {
                    Utils.makeButton(OtpButton, getResources().getColor(R.color.grey), 50F);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        OtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_phone_number.getText())) {
                    Utils.showToast(activity, "Mobile number is missing", AppConstant.errorColor);
                    return;
                }
                if (et_phone_number.getText().toString().length() < 10) {
                    Utils.showToast(activity, "Invalid mobile number", AppConstant.errorColor);
                    return;
                }
                Intent intent = new Intent(LogIn.this, Otp.class);
                intent.putExtra(AppConstant.LOGIN_MOBILE, et_phone_number.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        et_phone_number=findViewById(R.id.et_phone_number);
        et_phone_number.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        et_phone_number.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},400);

        OtpButton = findViewById(R.id.otp_button);
        Utils.makeButton(OtpButton, getResources().getColor(R.color.grey), 50F);
    }
}