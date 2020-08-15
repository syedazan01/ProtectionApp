package com.example.protectionapp.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnBankScan, btnBankSave, btnbankSend;
    String msg, password;
    private TextView tvToolbarTitle;
    TextInputLayout accountHolderName, accountNumber, ifscCode, branchName, bankName;
    Activity activity = this;
    private Uri fileUri;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    private ImageView ivBank, ivbankscan;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(Bank.this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            ivbankscan.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void initActions() {

        //open dialog for send file
        btnbankSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

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
                //progress dialog
                uploadingDialog.startloadingDialog();

                BankBean bankBean = new BankBean();
                bankBean.setAccountHolderName(accountHoldernames);
                bankBean.setAccountNumber(accountNumbers);
                bankBean.setIfscCode(ifscCodes);
                bankBean.setBranchName(branchNames);
                bankBean.setBankName(banknames);
                bankBean.setBankimage(fileUri.getLastPathSegment());
                bankBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.BANK + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
                Utils.storeDocumentsInRTD(AppConstant.BANK, Utils.toJson(bankBean, BankBean.class));
                //progress dialog
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadingDialog.dismissdialog();

                    }
                }, 4000);
            }
        });
    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true);
        sendDailog.show(getSupportFragmentManager(),"Send Dialog");
    }

    private void initViews() {
        btnBankScan = findViewById(R.id.bank_scanbt);
        btnBankSave = findViewById(R.id.bank_savebt);
        btnbankSend = findViewById(R.id.bank_sendBT);
        accountHolderName = findViewById(R.id.accHolderName);
        accountNumber = findViewById(R.id.account_number);
        ifscCode = findViewById(R.id.ifsc_code);
        branchName = findViewById(R.id.branch_name);
        bankName = findViewById(R.id.Bank_name);
        ivbankscan = findViewById(R.id.bank_imageview);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivBank = findViewById(R.id.ivBack);
        Utils.makeButton(btnBankScan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnBankSave, getResources().getColor(R.color.colorPrimary), 40F);
    }

    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;
        final ProgressDialog pd = Utils.getProgressDialog(activity);
        pd.show();
        final Dialog dialog = Utils.getRegisteredUserList(activity);
        Button btnSend = dialog.findViewById(R.id.btnSend);
        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
        final RecyclerView rvUser = dialog.findViewById(R.id.rvUser);
        rvUser.setLayoutManager(new LinearLayoutManager(activity));
        rvUser.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
        Utils.getUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                List<UserBean> userBeans = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserBean userBean = postSnapshot.getValue(UserBean.class);
                    if (userBean.getMobile() != null) {
                        if (!userBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            userBeans.add(userBean);
                        }
                    }
                }

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, Bank.this));
                dialog.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final ProgressDialog pd = Utils.getProgressDialog(activity);
                pd.show();
                for (FileShareBean fileShareBean : fileShareBeans) {
                    Utils.storeFileShareToRTD(fileShareBean);
                }
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
            }
        });

    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        if (isChecked) {
            FileShareBean fileShareBean = new FileShareBean();
            fileShareBean.setSentTo(userBean.getMobile());
            fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
            fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            fileShareBean.setDocument_type(AppConstant.BANK);
            fileShareBean.setPassword(password);
            fileShareBean.setMsg(msg);
            fileShareBeans.add(fileShareBean);
        } else {
            fileShareBeans.remove(position);
        }

    }
}