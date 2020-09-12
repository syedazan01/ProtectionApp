package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.BirthCertificateBean;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.NotificationBean;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

public class DateOfBirth extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    TextInputEditText dateofbirth;
    TextInputLayout fathername, mothername, childname, hospitalname;
    Button scanbirthBT, sendbirthBT, savebirthBT;
    int yearofdob, monthofdob, dayofdob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView, imageView2;
    String msg = "", password = "";
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(DateOfBirth.this);
    StorageReference storageReference;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    List<String> tokenList = new ArrayList<>();
    private Uri fileUri, fileUri2;
    private Boolean imagepicker = false;
    private BirthCertificateBean birthCertificateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_of_birth);
        initViews();
        initActioin();
        //firebase storgae
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void initActioin() {
        //open dialog for send file
        sendbirthBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                openDialog();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        savebirthBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (!validateForm())
                    return;
                //get all the values
                String Fathersname = fathername.getEditText().getText().toString();
                String Mothersname = mothername.getEditText().getText().toString();
                String Childname = childname.getEditText().getText().toString();
                String gender;
                if (radioMale.isChecked())
                    gender = "Male";
                else if (radioFemale.isChecked())
                    gender = "Female";
                else gender = "Other";
                String Dateofbirth = dateofbirth.getText().toString();
                String Hosptialname = hospitalname.getEditText().getText().toString();

                //progress dialog
                uploadingDialog.startloadingDialog();

                birthCertificateBean = new BirthCertificateBean();
                birthCertificateBean.setId((int)System.currentTimeMillis());
                birthCertificateBean.setFathername(Fathersname);
                birthCertificateBean.setMothername(Mothersname);
                birthCertificateBean.setChildname(Childname);
                birthCertificateBean.setSex(gender);
                birthCertificateBean.setDateofbirth(Dateofbirth);
                birthCertificateBean.setHospitalname(Hosptialname);
                birthCertificateBean.setImageview1(fileUri.getLastPathSegment());
                birthCertificateBean.setImageview2(fileUri2.getLastPathSegment());
                birthCertificateBean.setMoblilenumber(PrefManager.getString(AppConstant.USER_MOBILE));
                Utils.getStorageReference().child(AppConstant.PAN + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.PAN + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                Utils.storeDocumentsInRTD(AppConstant.PAN, Utils.toJson(birthCertificateBean, BirthCertificateBean.class));
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
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateOfBirth.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        dateofbirth.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        scanbirthBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMediaChooseBottomSheet(DateOfBirth.this);
            }
        });
    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //sendDailog.show(getSupportFragmentManager(), "Send Dialog");


    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(fathername.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            fathername.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mothername.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            mothername.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(childname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            childname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dateofbirth.getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            return false;
        }
        if (TextUtils.isEmpty(hospitalname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            hospitalname.getEditText().requestFocus();
            return false;
        }
        if (fileUri == null) {
            Utils.showToast(activity, getResources().getString(R.string.birth_scan_error), AppConstant.errorColor);
            return false;

        }
        if (fileUri2 == null) {
            Utils.showToast(activity, getResources().getString(R.string.birth_scan_error), AppConstant.errorColor);
            return false;
        }
        return true;
    }

    private void initViews() {
        fathername = findViewById(R.id.birth_fathername);
        mothername = findViewById(R.id.birth_mothername);
        childname = findViewById(R.id.birth_childname);
        dateofbirth = findViewById(R.id.dob_calender_birthcertificate);
        hospitalname = findViewById(R.id.birth_hospitalname);
        scanbirthBT = findViewById(R.id.birth_cam_openbt);
        sendbirthBT = findViewById(R.id.birth_sendBT);
        savebirthBT = findViewById(R.id.birth_savebt);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        imageView = findViewById(R.id.birth_imageview1);
        imageView2 = findViewById(R.id.birth_imageView2);
        radioOther = findViewById(R.id.Gen_other);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Birth Certificate Form");
//        Utils.makeButton(scanbirthBT, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(savebirthBT, getResources().getColor(R.color.colorPrimary), 40F);
       scanbirthBT.setBackground(getThemeGradient(50F));
       savebirthBT.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.BIRTH_CERTIFICATE)) {
            savebirthBT.setText("Update");
             birthCertificateBean = (BirthCertificateBean) getIntent().getSerializableExtra(AppConstant.BIRTH_CERTIFICATE);
            fathername.getEditText().setText(birthCertificateBean.getFathername());
            mothername.getEditText().setText(birthCertificateBean.getMothername());
            childname.getEditText().setText(birthCertificateBean.getChildname());
            if (birthCertificateBean.getSex().equals("Male"))
                radioMale.setChecked(true);
            else if (birthCertificateBean.getSex().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            dateofbirth.setText(birthCertificateBean.getDateofbirth());
            hospitalname.getEditText().setText(birthCertificateBean.getHospitalname());

            final ProgressDialog pd = Utils.getProgressDialog(DateOfBirth.this);
            pd.show();

            Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + birthCertificateBean.getImageview1()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(DateOfBirth.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView);
                    }
                }

            });
            Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + birthCertificateBean.getImageview2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(DateOfBirth.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }

            });
        } else {
            savebirthBT.setText("Save");
            sendbirthBT.setVisibility(View.GONE);
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, DateOfBirth.this));
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
        fileShareBean.setId((int)birthCertificateBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.ADHAAR);
        fileShareBean.setPassword(password);
        fileShareBean.setMsg(msg);
        if (isChecked) {

            fileShareBeans.add(fileShareBean);
            tokenList.add(userBean.getFcmToken());
        } else {
            fileShareBeans.remove(fileShareBean);
            tokenList.remove(userBean.getFcmToken());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (imagepicker == false) {
                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();
                imageView.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker == true) {
                fileUri2 = data.getData();
                imageView2.setImageURI(fileUri2);
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
}
