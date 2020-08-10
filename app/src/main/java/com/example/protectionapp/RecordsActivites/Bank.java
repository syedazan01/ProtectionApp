package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.AtmHelperClass;
import com.example.protectionapp.utils.Utils;
import com.example.protectionapp.utils.views.BankHelperClass;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Bank extends AppCompatActivity {
    private Button btnBankScan, btnBankSave;
    private ImageView ivBank;
    private TextView tvToolbarTitle;
    TextInputLayout accountHolderName, accountNumber, ifscCode, branchName, bankName;
    Activity activity = this;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        initViews();
        initActions();
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(Bank.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Bank.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        btnBankScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(Bank.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void initActions() {
        ivBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnBankSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (TextUtils.isEmpty(accountHolderName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    accountHolderName.getEditText().requestFocus();
                    return;
                }

                if (accountNumber.getEditText().getText().toString().length() < 16) {
                    Utils.showToast(activity, getResources().getString(R.string.adhaar_error), AppConstant.errorColor);
                    accountNumber.getEditText().requestFocus();
                    return;
                }
                if (ifscCode.getEditText().getText().toString().length() < 11) {
                    Utils.showToast(activity, getResources().getString(R.string.adhaar_error), AppConstant.errorColor);
                    ifscCode.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(branchName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    branchName.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(bankName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    bankName.getEditText().requestFocus();
                    return;
                }
                // get all the values
                String accountHoldernames = accountHolderName.getEditText().getText().toString();
                String accountNumbers = accountNumber.getEditText().getText().toString();
                String ifscCodes = ifscCode.getEditText().getText().toString();
                String branchNames = branchName.getEditText().getText().toString();
                String banknames = bankName.getEditText().getText().toString();


                BankHelperClass bankHelperClass = new BankHelperClass(accountHoldernames, accountNumbers, ifscCodes, branchNames, banknames);
                Utils.storeDocumentsInRTD(Bank.this, AppConstant.BANK, Utils.toJson(bankHelperClass, BankHelperClass.class));
            }
        });
    }

    private void initViews() {
        btnBankScan = findViewById(R.id.bank_scanbt);
        btnBankSave = findViewById(R.id.bank_savebt);
        accountHolderName = findViewById(R.id.accHolderName);
        accountNumber = findViewById(R.id.account_number);
        ifscCode = findViewById(R.id.ifsc_code);
        branchName = findViewById(R.id.branch_name);
        bankName = findViewById(R.id.Bank_name);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivBank = findViewById(R.id.ivBack);
        Utils.makeButton(btnBankScan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnBankSave, getResources().getColor(R.color.colorPrimary), 40F);
    }
}