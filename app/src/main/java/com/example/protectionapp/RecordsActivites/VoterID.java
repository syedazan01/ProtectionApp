package com.example.protectionapp.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.NotificationBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.model.VoteridBean;
import com.example.protectionapp.network.ApiResonse;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.protectionapp.utils.Utils.getThemeGradient;

public class VoterID extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnVoteridscan, btnVoteridsave, btnVoteridsend;
    String password, msg;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, FatherName, dob, Address, AssemblyName;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    TextInputEditText dobET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private ImageView ivVid, ivVoterid, ivVoterid2;
    private Uri fileUri, fileUri2;
    List<String> tokenList = new ArrayList<>();
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(VoterID.this);
    private List<FileShareBean> fileShareBeans = new ArrayList<>();
    private Boolean imagepicker;
    private VoteridBean voteridBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_i_d);
        initViews();
        initActions();
        if (ContextCompat.checkSelfPermission(VoterID.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VoterID.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        btnVoteridscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMediaChooseBottomSheet(VoterID.this);
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
                ivVoterid.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker == true) {
                fileUri2 = data.getData();
                ivVoterid2.setImageURI(fileUri2);
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
        btnVoteridsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        ivVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnVoteridsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (TextUtils.isEmpty(FullName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    FullName.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(FatherName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    FatherName.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(Address.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    Address.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(AssemblyName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    AssemblyName.getEditText().requestFocus();
                    return;
                }
                if (fileUri == null) {
                    Utils.showToast(activity, getResources().getString(R.string.voterId_scan_error), AppConstant.errorColor);
                    return;
                }
                if (fileUri2 == null) {
                    Utils.showToast(activity, getResources().getString(R.string.voterId_scan_error), AppConstant.errorColor);
                    return;
                }
                //get  all the values
                String FullNames = FullName.getEditText().getText().toString();
                String fathersname = FatherName.getEditText().getText().toString();
                String Voterdob = dob.getEditText().getText().toString();
                String address = Address.getEditText().getText().toString();
                String assemblyNames = AssemblyName.getEditText().getText().toString();
                String gender;
                if (radioMale.isChecked())
                    gender = "Male";
                else if (radioFemale.isChecked())
                    gender = "Female";
                else gender = "Other";

                //progress dialog
                uploadingDialog.startloadingDialog();

                voteridBean = new VoteridBean();
                voteridBean.setId((int)System.currentTimeMillis());
                voteridBean.setFullName(FullNames);
                voteridBean.setFathersName(fathersname);
                voteridBean.setDateofbirth(Voterdob);
                voteridBean.setAddress(address);
                voteridBean.setAssemblyname(assemblyNames);
                voteridBean.setGender(gender);
                voteridBean.setVoterMobileNo((PrefManager.getString(AppConstant.USER_MOBILE)));
                voteridBean.setVoterImage(fileUri.getLastPathSegment());
                voteridBean.setVoterImage2(fileUri2.getLastPathSegment());
                Utils.storeDocumentsInRTD(AppConstant.VOTER_ID, Utils.toJson(voteridBean, VoteridBean.class));
                Utils.getStorageReference().child(AppConstant.VOTER_ID + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.VOTER_ID + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
            }
        });
        final Calendar calendar = Calendar.getInstance();
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(VoterID.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

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
        btnVoteridscan = findViewById(R.id.voterscanbt);
        btnVoteridsave = findViewById(R.id.voterid_savebt);
        btnVoteridsend = findViewById(R.id.voter_sendBT);
        FullName = findViewById(R.id.voteridFullname);
        FatherName = findViewById(R.id.voteridFathername);
        dob = findViewById(R.id.voterid_dob);
        ivVoterid = findViewById(R.id.ivVoterid_1);
        ivVoterid2 = findViewById(R.id.ivVoterid_2);
        Address = findViewById(R.id.voterid_addres);
        AssemblyName = findViewById(R.id.elction_constituency);
        radioGender = findViewById(R.id.VoteridGradio);
        radioMale = findViewById(R.id.Gen_votermale);
        radioFemale = findViewById(R.id.Gen_voterfemale);
        radioOther = findViewById(R.id.Gen_voterother);
        dobET = findViewById(R.id.voteriddob_calender);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Voter id Detail Form");
        ivVid = findViewById(R.id.ivBack);
//        Utils.makeButton(btnVoteridscan, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(btnVoteridsave, getResources().getColor(R.color.colorPrimary), 40F);
        btnVoteridsave.setBackground(getThemeGradient(50F));
        btnVoteridscan.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.VOTER_ID)) {
            btnVoteridsave.setText("Update");
             voteridBean = (VoteridBean) getIntent().getSerializableExtra(AppConstant.VOTER_ID);
            FullName.getEditText().setText(voteridBean.getFullName());
            FatherName.getEditText().setText(voteridBean.getFathersName());
            dob.getEditText().setText(voteridBean.getDateofbirth());
            Address.getEditText().setText(voteridBean.getAddress());
            AssemblyName.getEditText().setText(voteridBean.getAssemblyname());
            if (voteridBean.getGender().equals("Male"))
                radioMale.setChecked(true);
            else if (voteridBean.getGender().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            final ProgressDialog pd = Utils.getProgressDialog(VoterID.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.VOTER_ID + "/" + voteridBean.getVoterImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(VoterID.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivVoterid);
                    }
                }
            });
        } else {
            btnVoteridsave.setText("Save");
            btnVoteridsend.setVisibility(View.GONE);
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, VoterID.this));
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
        fileShareBean.setId((int)voteridBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.VOTER_ID);
        fileShareBean.setPassword(password);
        fileShareBean.setMsg(msg);
        if (isChecked) {

            tokenList.add(userBean.getFcmToken());
            fileShareBeans.add(fileShareBean);
        } else {
            tokenList.remove(userBean.getFcmToken());
            fileShareBeans.remove(fileShareBean);
        }
    }
}