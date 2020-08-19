package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.protectionapp.R;
import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdhaarFile extends AppCompatActivity {
    TextInputLayout adhharfullname, adharnumber, adharaddres, dob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView, imageView2;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;
    private TextInputEditText dobinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar_file);
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
    }

    private void initViews() {
        adhharfullname = findViewById(R.id.adhhar_fullname);
        adharnumber = findViewById(R.id.adhar_number);
        adharaddres = findViewById(R.id.adhar_addres);
        dob = findViewById(R.id.dob);
        imageView = findViewById(R.id.adhar_imageView1);
        imageView2 = findViewById(R.id.adhar_imageView2);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        dobinput = findViewById(R.id.dob_calender);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Adhaar Form");
        if (getIntent().hasExtra(AppConstant.ADHAAR)) {
            AdhaarBean adhaarBean = (AdhaarBean) getIntent().getSerializableExtra(AppConstant.ADHAAR);
            adhharfullname.getEditText().setText(adhaarBean.getFullname());
            adharaddres.getEditText().setText(adhaarBean.getAddress());
            adharnumber.getEditText().setText(adhaarBean.getAadharNumber());
            dobinput.setText(adhaarBean.getDateofbirth());
            if (adhaarBean.getGender().equals("Male"))
                radioMale.setChecked(true);
            else if (adhaarBean.getGender().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            ProgressDialog pd = Utils.getProgressDialog(AdhaarFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + adhaarBean.getAdhaarimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(AdhaarFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView);
                    }
                }
            });
            Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + adhaarBean.getAdhaarimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(AdhaarFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }
            });
        }
    }
}