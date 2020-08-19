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
import com.example.protectionapp.model.PassportBean;
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

public class Passport extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    TextInputEditText dobinput, dateofissue, dateofexpiry;
    TextInputLayout passportnumber, type, countrycode, nationality, fullname, placeofissue, placeofbirth;
    Button scanpassportBT, sendpassportBT, savepassportBT;
    int yearofdob, monthofdob, dayofdob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView, imageView2;
    String msg = "", password = "";
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(Passport.this);
    StorageReference storageReference;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    List<String> tokenList = new ArrayList<>();
    Boolean imagepick = true;
    private Uri fileUri, fileUri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        initViews();
        initAction();
        //firebase storgae
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void initAction() {
        //open dialog for send file
        sendpassportBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                openDialog();
            }
        });
        //save data on SAVE button click firebase
        savepassportBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation
                if (!validateForm())
                    return;
                String Passportnumber = passportnumber.getEditText().getText().toString();
                String Type = type.getEditText().getText().toString();
                String Countrycode = countrycode.getEditText().getText().toString();
                String Nationality = nationality.getEditText().getText().toString();
                String passportfullname = fullname.getEditText().getText().toString();
                String gender;
                if (radioMale.isChecked())
                    gender = "Male";
                else if (radioFemale.isChecked())
                    gender = "Female";
                else gender = "Other";
                String dateofbirthpassport = dobinput.getText().toString();
                String placeofbirthpassport = placeofbirth.getEditText().getText().toString();
                String placeofissuepassport = placeofissue.getEditText().getText().toString();
                String dateofissuepassport = dateofissue.getText().toString();
                String dateofexiprypassport = dateofexpiry.getText().toString();


                //progress dialog
                uploadingDialog.startloadingDialog();

                PassportBean passportBean = new PassportBean();
                passportBean.setPassportnumber(Passportnumber);
                passportBean.setType(Type);
                passportBean.setCountrycode(Countrycode);
                passportBean.setNationalty(Nationality);
                passportBean.setFullname(passportfullname);
                passportBean.setSex(gender);
                passportBean.setDateofbirth(dateofbirthpassport);
                passportBean.setPlaceofbirth(placeofbirthpassport);
                passportBean.setPlaceofissue(placeofissuepassport);
                passportBean.setDateofissue(dateofissuepassport);
                passportBean.setDateofexpiry(dateofexiprypassport);
                passportBean.setPassportimage1(fileUri.getLastPathSegment());
                passportBean.setPassportimage2(fileUri2.getLastPathSegment());
                passportBean.setMobilenumber(PrefManager.getString(AppConstant.USER_MOBILE));
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                Utils.storeDocumentsInRTD(AppConstant.PASSPORT, Utils.toJson(passportBean, PassportBean.class));
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });


            }
        });
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(Passport.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Passport.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        //open camera to capture image
        scanpassportBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(Passport.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        //date picker
        final Calendar calendar = Calendar.getInstance();
        dobinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Passport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobinput.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();
            }
        });
        //date picker
        final Calendar calendar2 = Calendar.getInstance();
        dateofissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar2.get(Calendar.YEAR);
                monthofdob = calendar2.get(Calendar.MONTH);
                dayofdob = calendar2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Passport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateofissue.setText(SimpleDateFormat.getDateInstance().format(calendar2.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();
            }
        });
        //date picker
        final Calendar calendar3 = Calendar.getInstance();
        dateofexpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar3.get(Calendar.YEAR);
                monthofdob = calendar3.get(Calendar.MONTH);
                dayofdob = calendar3.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Passport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateofexpiry.setText(SimpleDateFormat.getDateInstance().format(calendar3.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();
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
        SendDailog sendDailog = new SendDailog(this, true);
        sendDailog.show(getSupportFragmentManager(), "Send Dialog");
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(passportnumber.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            passportnumber.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(type.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            type.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(countrycode.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            countrycode.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(nationality.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            nationality.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(fullname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            fullname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dobinput.getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            return false;
        }
        if (TextUtils.isEmpty(placeofbirth.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            placeofbirth.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(placeofissue.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            placeofissue.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dateofissue.getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            return false;
        }
        if (TextUtils.isEmpty(dateofexpiry.getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            return false;
        }
        if (fileUri == null) {
            Utils.showToast(activity, getResources().getString(R.string.adhaar_scan_error), AppConstant.errorColor);
            return false;
        }
        if (fileUri2 == null) {
            Utils.showToast(activity, getResources().getString(R.string.adhaar_scan_error), AppConstant.errorColor);
            return false;
        }
        return true;
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        if (isChecked) {
            FileShareBean fileShareBean = new FileShareBean();
            fileShareBean.setSentTo(userBean.getMobile());
            fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
            fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            fileShareBean.setDocument_type(AppConstant.ADHAAR);
            fileShareBean.setPassword(password);
            fileShareBean.setMsg(msg);
            fileShareBeans.add(fileShareBean);
            tokenList.add(userBean.getFcmToken());
        } else {
            fileShareBeans.remove(position);
        }
    }

    private void initViews() {
        passportnumber = findViewById(R.id.passport_number);
        type = findViewById(R.id.passport_type);
        countrycode = findViewById(R.id.passport_countrycode);
        nationality = findViewById(R.id.passport_nationality);
        fullname = findViewById(R.id.passport_fullname);
        placeofissue = findViewById(R.id.passport_placeofissue);
        placeofbirth = findViewById(R.id.passport_placeofbirth);
        savepassportBT = findViewById(R.id.passport_savebt);
        scanpassportBT = findViewById(R.id.cam_openbt);
        sendpassportBT = findViewById(R.id.passport_sendBT);
        dobinput = findViewById(R.id.dob_calender_passport);
        dateofissue = findViewById(R.id.dofissue_calender);
        dateofexpiry = findViewById(R.id.passport_datevalidity);
        radioGender = findViewById(R.id.Gradio);
        radioOther = findViewById(R.id.Gen_other);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        imageView = findViewById(R.id.passport_imageView1);
        imageView2 = findViewById(R.id.passport_imageView2);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Passport Form");
        Utils.makeButton(scanpassportBT, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(savepassportBT, getResources().getColor(R.color.colorPrimary), 40F);
        if (getIntent().hasExtra(AppConstant.PASSPORT)) {
            savepassportBT.setText("Update");
            PassportBean passportBean = (PassportBean) getIntent().getSerializableExtra(AppConstant.PASSPORT);
            passportnumber.getEditText().setText(passportBean.getPassportnumber());
            type.getEditText().setText(passportBean.getType());
            countrycode.getEditText().setText(passportBean.getCountrycode());
            nationality.getEditText().setText(passportBean.getNationalty());
            fullname.getEditText().setText(passportBean.getFullname());
            dobinput.setText(passportBean.getDateofbirth());
            if (passportBean.getSex().equals("Male"))
                radioMale.setChecked(true);
            else if (passportBean.getSex().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            final ProgressDialog pd = Utils.getProgressDialog(Passport.this);
            pd.show();

            Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + passportBean.getPassportimage1()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(Passport.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView);
                    }
                }

            });
            Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + passportBean.getPassportimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(Passport.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }

            });
        } else {
            savepassportBT.setText("Save");
            sendpassportBT.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (imagepick == true) {
                fileUri = data.getData();
                imageView.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepick = false;
            } else if (imagepick == false) {
                fileUri2 = data.getData();
                imageView2.setImageURI(fileUri2);
                //You can get File object from intent
                File file2 = ImagePicker.Companion.getFile(data);
                imagepick = true;

            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, Passport.this));
                dialog.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        sendpassportBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final ProgressDialog pd = Utils.getProgressDialog(activity);
                pd.show();
                dialog.dismiss();
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
                for (FileShareBean fileShareBean : fileShareBeans) {
                    Utils.storeFileShareToRTD(fileShareBean);
                }
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
            }
        });
    }

}