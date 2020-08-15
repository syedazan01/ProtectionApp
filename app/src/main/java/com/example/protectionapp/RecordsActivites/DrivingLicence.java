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
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.protectionapp.model.DlicenceBean;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DrivingLicence extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnDLscan, btnDLsave, btnDLSend;
    private ImageView ivDL, ivDLscan;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, sonOf, LicenceNumber, BloodGroup, dob, dateofissue, validity;
    TextInputEditText dobET, dateOfIssueET, ValidityET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private Uri fileUri;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(DrivingLicence.this);
    private List<FileShareBean> fileShareBeans = new ArrayList<>();
    private String password, msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_licence);
        initViews();
        initActions();
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(DrivingLicence.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DrivingLicence.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        btnDLscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(DrivingLicence.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void initActions() {
        //open dialog for send file
        btnDLSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        ivDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnDLsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (TextUtils.isEmpty(FullName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    FullName.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sonOf.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    sonOf.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(LicenceNumber.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    LicenceNumber.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(BloodGroup.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    BloodGroup.getEditText().requestFocus();
                    return;
                }
                // get all the values
                String FullNames = FullName.getEditText().getText().toString();
                String sonOfName = sonOf.getEditText().getText().toString();
                String licenceNumber = LicenceNumber.getEditText().getText().toString();
                String bloddgroups = BloodGroup.getEditText().getText().toString();
                String DLdob = dob.getEditText().getText().toString();
                String dateofIssue = dateofissue.getEditText().getText().toString();
                String DLvaliditys = validity.getEditText().getText().toString();

                //progress dialog
                uploadingDialog.startloadingDialog();

                DlicenceBean dlicenceBean = new DlicenceBean();
                dlicenceBean.setFullname(FullNames);
                dlicenceBean.setSon_of(sonOfName);
                dlicenceBean.setLicenceNumber(licenceNumber);
                dlicenceBean.setBloodGroup(bloddgroups);
                dlicenceBean.setDateOfBirth(DLdob);
                dlicenceBean.setDateOfIssue(dateofIssue);
                dlicenceBean.setValidity(DLvaliditys);
                dlicenceBean.setDLimage(fileUri.getLastPathSegment());
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.DRIVING_LICENSE + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
                Utils.storeDocumentsInRTD(AppConstant.DRIVING_LICENSE, Utils.toJson(dlicenceBean, DlicenceBean.class));
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

        final Calendar calendar = Calendar.getInstance();
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        //date of issue
        final Calendar calendar2 = Calendar.getInstance();
        dateOfIssueET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar2.get(Calendar.YEAR);
                monthofdob = calendar2.get(Calendar.MONTH);
                dayofdob = calendar2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateOfIssueET.setText(SimpleDateFormat.getDateInstance().format(calendar2.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        //validity
        final Calendar calendar3 = Calendar.getInstance();
        ValidityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar3.get(Calendar.YEAR);
                monthofdob = calendar3.get(Calendar.MONTH);
                dayofdob = calendar3.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        ValidityET.setText(SimpleDateFormat.getDateInstance().format(calendar3.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });

    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true);
        sendDailog.show(getSupportFragmentManager(),"Send Dialog");
    }

    private void initViews() {
        btnDLscan = findViewById(R.id.btdl_scan);
        btnDLsave = findViewById(R.id.DL_savebt);
        btnDLSend = findViewById(R.id.driving_sendBT);
        FullName = findViewById(R.id.drivingFullname);
        sonOf = findViewById(R.id.drivingFathersname);
        LicenceNumber = findViewById(R.id.LicenceNumber);
        BloodGroup = findViewById(R.id.bloodgroupDriving);
        dob = findViewById(R.id.Driving_dob);
        dateofissue= findViewById(R.id.Drivingdofissue);
        validity = findViewById(R.id.licence_validity);
        ivDLscan = findViewById(R.id.dl_imageview);


        dobET = findViewById(R.id.Drivingdob_calender);
        dateOfIssueET = findViewById(R.id.dofissue_calender);
        ValidityET = findViewById(R.id.Licence_datevalidity);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivDL = findViewById(R.id.ivBack);
        Utils.makeButton(btnDLscan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnDLsave, getResources().getColor(R.color.colorPrimary), 40F);
        if (getIntent().hasExtra(AppConstant.DRIVING_LICENSE)) {
            btnDLsave.setText("Update");
            DlicenceBean dlicenceBean = (DlicenceBean) getIntent().getSerializableExtra(AppConstant.DRIVING_LICENSE);
            FullName.getEditText().setText(dlicenceBean.getFullname());
            sonOf.getEditText().setText(dlicenceBean.getSon_of());
            LicenceNumber.getEditText().setText(dlicenceBean.getLicenceNumber());
            BloodGroup.getEditText().setText(dlicenceBean.getBloodGroup());
            dob.getEditText().setText(dlicenceBean.getDateOfBirth());
            dateofissue.getEditText().setText(dlicenceBean.getDateOfBirth());
            validity.getEditText().setText(dlicenceBean.getDateOfBirth());

            final ProgressDialog pd = Utils.getProgressDialog(DrivingLicence.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.ATM + "/" + dlicenceBean.getDLimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(DrivingLicence.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivDLscan);
                    }
                }
            });
        } else {
            btnDLsave.setText("Save");
            btnDLSend.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            ivDLscan.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);


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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, DrivingLicence.this));
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
            fileShareBean.setDocument_type(AppConstant.DRIVING_LICENSE);
            fileShareBean.setPassword(password);
            fileShareBean.setMsg(msg);
            fileShareBeans.add(fileShareBean);
        } else {
            fileShareBeans.remove(position);
        }
    }
}
