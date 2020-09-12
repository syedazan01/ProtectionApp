package com.example.protectionapp.RecordsActivites;

import android.Manifest;
import android.app.Activity;
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
import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.NotificationBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.UserBean;
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

public class StudentID extends AppCompatActivity implements AdapterUsers.RecyclerViewListener, SendDailog.SendDialogListener {
    TextInputLayout institutionname, enroll, rollno, fullname, fathername, branch;
    UploadingDialog uploadingDialog = new UploadingDialog(StudentID.this);
    private Button btnSTDScan, btnSTDSave, btnSTDSend;
    private ImageView ivBack, imageViewstid, imageViewstid2;
    private TextView tvToolbarTitle;
    private Uri fileUri, fileUri2;
    private Activity activity = this;
    private List<FileShareBean> fileShareBeans = new ArrayList<>();
    private String password, msg;
    List<String> tokenList = new ArrayList<>();
    private Boolean imagepicker;
    private StudentIdBean studentIdBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_i_d);
        initViws();
        initAction();
        if (ContextCompat.checkSelfPermission(StudentID.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentID.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void initAction() {
        btnSTDSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                openDialog();


            }
        });
        btnSTDScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMediaChooseBottomSheet(StudentID.this);
            }
        });
        btnSTDSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (!validateForm())
                    return;
                //get all the values
                String Institutionname = institutionname.getEditText().getText().toString();
                String Enroll = enroll.getEditText().getText().toString();
                String Rollno = rollno.getEditText().getText().toString();
                String Fullname = fullname.getEditText().getText().toString();
                String Fathername = fathername.getEditText().getText().toString();
                String Branch = branch.getEditText().getText().toString();
                //progress dialog
                uploadingDialog.startloadingDialog();


                studentIdBean = new StudentIdBean();
                studentIdBean.setId((int)System.currentTimeMillis());
                studentIdBean.setInstitutionname(Institutionname);
                studentIdBean.setEnroll(Enroll);
                studentIdBean.setRollno(Rollno);
                studentIdBean.setFullname(Fullname);
                studentIdBean.setFathername(Fathername);
                studentIdBean.setBranch(Branch);
                studentIdBean.setStudntidimage(fileUri.getLastPathSegment());
                studentIdBean.setStudntidimage2(fileUri2.getLastPathSegment());
                studentIdBean.setMobilenumber(PrefManager.getString(AppConstant.USER_MOBILE));
                Utils.getStorageReference().child(AppConstant.STUDENT_ID + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.STUDENT_ID + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                Utils.storeDocumentsInRTD(AppConstant.STUDENT_ID, Utils.toJson(studentIdBean, StudentIdBean.class));
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //sendDailog.show(getSupportFragmentManager(), "send dialog");
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, StudentID.this));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to image  view
            imageView.setImageBitmap(captureImage);
        }*/
        if (resultCode == Activity.RESULT_OK) {
            if (imagepicker == false) {
                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();
                imageViewstid.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker == true) {
                fileUri2 = data.getData();
                imageViewstid2.setImageURI(fileUri2);
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

    private boolean validateForm() {
        if (TextUtils.isEmpty(institutionname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            institutionname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(enroll.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            enroll.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(rollno.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            rollno.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(fullname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            fullname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(fathername.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            fathername.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(branch.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            branch.getEditText().requestFocus();
            return false;
        }
        if (fileUri == null) {
            Utils.showToast(activity, getResources().getString(R.string.studentId_scan_error), AppConstant.errorColor);
            return false;
        }
        if (fileUri2 == null) {
            Utils.showToast(activity, getResources().getString(R.string.studentId_scan_error), AppConstant.errorColor);
            return false;
        }
        return true;
    }

    private void initViws() {
        institutionname = findViewById(R.id.institution_name_ET);
        enroll = findViewById(R.id.enroll_NoTL);
        rollno = findViewById(R.id.roll_noTL);
        fullname = findViewById(R.id.STfullname_TL);
        fathername = findViewById(R.id.fathername_IL);
        branch = findViewById(R.id.branch_name_IL);
        btnSTDSave = findViewById(R.id.btnSTDSave);
        btnSTDScan = findViewById(R.id.btnSTDscan);
        btnSTDSend = findViewById(R.id.send_STD_BT);
        imageViewstid = findViewById(R.id.ivStudentid_1);
        imageViewstid2 = findViewById(R.id.ivStudentid_2);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Student Detail Form");
        ivBack = findViewById(R.id.ivBack);
        Utils.makeButton(btnSTDScan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnSTDSave, getResources().getColor(R.color.colorPrimary), 40F);

        //filling view document
        if (getIntent().hasExtra(AppConstant.STUDENT_ID)) {
            btnSTDSave.setText("Update");
             studentIdBean = (StudentIdBean) getIntent().getSerializableExtra(AppConstant.STUDENT_ID);
            institutionname.getEditText().setText(studentIdBean.getInstitutionname());
            enroll.getEditText().setText(studentIdBean.getEnroll());
            rollno.getEditText().setText(studentIdBean.getRollno());
            fullname.getEditText().setText(studentIdBean.getFullname());
            fathername.getEditText().setText(studentIdBean.getFathername());
            branch.getEditText().setText(studentIdBean.getBranch());
            final ProgressDialog pd = Utils.getProgressDialog(StudentID.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.PAN + "/" + studentIdBean.getStudntidimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(StudentID.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageViewstid);
                    }
                }
            });
            Utils.getStorageReference().child(AppConstant.PAN + "/" + studentIdBean.getStudntidimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(StudentID.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageViewstid2);
                    }
                }
            });
        } else {
            btnSTDSave.setText("Save");
            btnSTDSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId((int)studentIdBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.STUDENT_ID);
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