package atoz.protection.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.adapters.AdapterUsers;
import atoz.protection.model.BankBean;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.NotificationBean;
import atoz.protection.model.UserBean;
import atoz.protection.network.ApiResonse;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static atoz.protection.utils.Utils.getThemeGradient;

public class Bank extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnBankScan, btnBankSave, btnbankSend;
    String msg, password;
    private TextView tvToolbarTitle;
    TextInputLayout accountHolderName, accountNumber, ifscCode, branchName, bankName;
    Activity activity = this;
    private Uri fileUri, fileUri2;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    private ImageView ivBank, ivbankscan, ivbankscan2;
    List<String> tokenList = new ArrayList<>();
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(Bank.this);
    private Boolean imagepicker;
    private BankBean bankBean;

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
                Utils.showMediaChooseBottomSheet(Bank.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (imagepicker == false) {
                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();
                ivbankscan.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker == true) {
                fileUri2 = data.getData();
                ivbankscan2.setImageURI(fileUri2);
                //You can get File object from intent
                File file2 = ImagePicker.Companion.getFile(data);
                imagepicker = false;
            }


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
                if (fileUri == null) {
                    Utils.showToast(activity, getResources().getString(R.string.bank_scan_error), AppConstant.errorColor);
                    return;
                }
                if (fileUri2 == null) {
                    Utils.showToast(activity, getResources().getString(R.string.bank_scan_error), AppConstant.errorColor);
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

                bankBean = new BankBean();
                bankBean.setId((int)System.currentTimeMillis());
                bankBean.setAccountHolderName(accountHoldernames);
                bankBean.setAccountNumber(accountNumbers);
                bankBean.setIfscCode(ifscCodes);
                bankBean.setBranchName(branchNames);
                bankBean.setBankName(banknames);
                bankBean.setBankimage(fileUri.getLastPathSegment());
                bankBean.setBankimage2(fileUri2.getLastPathSegment());
                bankBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                Utils.getStorageReference().child(AppConstant.BANK + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.BANK + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
                Utils.storeDocumentsInRTD(AppConstant.BANK, Utils.toJson(bankBean, BankBean.class));
            }
        });
    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //sendDailog.show(getSupportFragmentManager(),"Send Dialog");
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
        ivbankscan = findViewById(R.id.bank_imageview1);
        ivbankscan2 = findViewById(R.id.bank_imageview2);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivBank = findViewById(R.id.ivBack);
//        Utils.makeButton(btnBankScan, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(btnBankSave, getResources().getColor(R.color.colorPrimary), 40F);
       btnBankSave.setBackground(getThemeGradient(50F));
       btnBankScan.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.BANK)) {
            btnBankSave.setText("Update");
            bankBean = (BankBean) getIntent().getSerializableExtra(AppConstant.BANK);
            accountHolderName.getEditText().setText(bankBean.getAccountHolderName());
            accountNumber.getEditText().setText(bankBean.getAccountNumber());
            ifscCode.getEditText().setText(bankBean.getIfscCode());
            branchName.getEditText().setText(bankBean.getBranchName());
            bankName.getEditText().setText(bankBean.getBankName());


            final ProgressDialog pd = Utils.getProgressDialog(Bank.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.BANK + "/" + bankBean.getBankimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(Bank.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivbankscan);
                    }
                }
            });
            Utils.getStorageReference().child(AppConstant.BANK + "/" + bankBean.getBankimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(Bank.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivbankscan2);
                    }
                }
            });
        } else {
            btnBankSave.setText("Save");
            btnbankSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;
        final ProgressDialog pd = Utils.getProgressDialog(activity);
        pd.show();
        final BottomSheetDialog dialog = Utils.getRegisteredUserList(activity);
        Button btnSend = dialog.findViewById(R.id.btnSend);
//        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
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
                pd.dismiss();
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
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://a2zcreation.000webhostapp.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();
                ApiResonse apiResonse = retrofit.create(ApiResonse.class);
                String tokens = tokenList.toString();
                tokens = tokens.replaceAll("[\\[\\](){}]", "");
                tokens = tokens.replace("\"", "");
                tokens = tokens.replaceAll(" ", "");
                Log.e("dvdfbtrghtbe", tokens + message);
                Call<NotificationBean> call = apiResonse.fileSendMsg(tokens, message);
                tokenList.clear();
                call.enqueue(new Callback<NotificationBean>() {
                    @Override
                    public void onResponse(Call<NotificationBean> call, Response<NotificationBean> response) {
                        Log.e("vdfbdbedtbher", String.valueOf(response.body().getSuccess()));
                    }

                    @Override
                    public void onFailure(Call<NotificationBean> call, Throwable t) {
                        Log.e("vdfbdbedtbher", t.getMessage());
                    }
                });
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
            }
        });

    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId((int)bankBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.BANK);
        fileShareBean.setPassword(password);
        fileShareBean.setMsg(msg);
        if (isChecked) {
            tokenList.add(userBean.getFcmToken());
            fileShareBeans.add(fileShareBean);
        } else {
            fileShareBeans.remove(fileShareBean);
            tokenList.remove(userBean.getFcmToken());
        }

    }
}