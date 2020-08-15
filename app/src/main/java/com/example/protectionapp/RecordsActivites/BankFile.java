package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.protectionapp.R;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class BankFile extends AppCompatActivity {
    TextInputLayout accountHolderName, accountNumber, ifscCode, branchName, bankName;
    Activity activity = this;
    private TextView tvToolbarTitle;
    private ImageView ivBank, ivbankscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_file);
        initViews();
        ivBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        accountHolderName = findViewById(R.id.accHolderName);
        accountNumber = findViewById(R.id.account_number);
        ifscCode = findViewById(R.id.ifsc_code);
        branchName = findViewById(R.id.branch_name);
        bankName = findViewById(R.id.Bank_name);
        ivbankscan = findViewById(R.id.bank_imageview);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivBank = findViewById(R.id.ivBack);

        if (getIntent().hasExtra(AppConstant.BANK)) {
            BankBean bankBean = (BankBean) getIntent().getSerializableExtra(AppConstant.BANK);
            accountHolderName.getEditText().setText(bankBean.getAccountHolderName());
            accountNumber.getEditText().setText(bankBean.getAccountNumber());
            ifscCode.getEditText().setText(bankBean.getIfscCode());
            branchName.getEditText().setText(bankBean.getBranchName());
            bankName.getEditText().setText(bankBean.getBankName());


            final ProgressDialog pd = Utils.getProgressDialog(BankFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.BANK + "/" + bankBean.getBankimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(BankFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivbankscan);
                    }
                }
            });
        }
    }
}