package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.DlicenceHelperClass;
import com.example.protectionapp.utils.Utils;
import com.example.protectionapp.utils.views.BankHelperClass;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DrivingLicence extends AppCompatActivity implements SendDailog.SendDialogListener {
    private Button btnDLscan, btnDLsave, btnDLSend;
    private ImageView ivDL;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, sonOf, LicenceNumber, BloodGroup, dob, dateofissue, validity;
    TextInputEditText dobET, dateOfIssueET, ValidityET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private Uri fileUri;


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


                DlicenceHelperClass dlicenceHelperClass = new DlicenceHelperClass(FullNames, sonOfName, licenceNumber, bloddgroups, DLdob,dateofIssue,DLvaliditys);
                Utils.storeDocumentsInRTD(DrivingLicence.this, AppConstant.DRIVING_LICENSE, Utils.toJson(dlicenceHelperClass, DlicenceHelperClass.class));
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
        SendDailog sendDailog = new SendDailog();
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


        dobET = findViewById(R.id.Drivingdob_calender);
        dateOfIssueET = findViewById(R.id.dofissue_calender);
        ValidityET = findViewById(R.id.Licence_datevalidity);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivDL = findViewById(R.id.ivBack);
        Utils.makeButton(btnDLscan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnDLsave, getResources().getColor(R.color.colorPrimary), 40F);

    }

    @Override
    public void applyTexts(String message, String password) {

    }
}
