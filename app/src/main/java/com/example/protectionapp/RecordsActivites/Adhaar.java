package com.example.protectionapp.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.protectionapp.R;
import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.Firebase;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Adhaar extends AppCompatActivity implements SendDailog.SendDialogListener {
    private TextInputEditText dobinput;
    public URL imgaeurl;
    TextInputLayout adhharfullname, adharnumber, adharaddres, dob;
    Button addharsavebt;
    int yearofdob, monthofdob, dayofdob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView;
    Button opencam,sendBT;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(Adhaar.this);
    String Gender;
    //firebase realtime database
    FirebaseDatabase rootNode;
    //    DatabaseReference reference;
    //firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;
    Firebase reference;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;
    private Uri fileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to image  view
            imageView.setImageBitmap(captureImage);
        }*/
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            imageView.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar);
        initViews();
        initActions();
        //firebase storgae
        storageReference = FirebaseStorage.getInstance().getReference();
        //progress dialog

    }

    private void initActions() {

        //open dialog for send file
        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        //save data on SAVE button click firebase
        addharsavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation
                if (TextUtils.isEmpty(adhharfullname.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    adhharfullname.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(adhharfullname.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    adhharfullname.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(dob.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    return;
                }
                if (adharnumber.getEditText().getText().toString().length() < 12) {
                    Utils.showToast(activity, getResources().getString(R.string.adhaar_error), AppConstant.errorColor);
                    adharnumber.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(adharaddres.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    adharaddres.getEditText().requestFocus();
                    return;
                }
                if (fileUri == null) {
                    Utils.showToast(activity, getResources().getString(R.string.adhaar_scan_error), AppConstant.errorColor);
                    return;
                }
                String Fullname = adhharfullname.getEditText().getText().toString();
                String aadharNumber = adharnumber.getEditText().getText().toString();
                String Address = adharaddres.getEditText().getText().toString();
                String dateofbirth = dob.getEditText().getText().toString();

                String gender;
                if (radioMale.isChecked())
                    gender = "Male";
                else if (radioFemale.isChecked())
                    gender = "Female";
                else gender = "Other";

                //progress dialog
                uploadingDialog.startloadingDialog();

                UserHelperClass userHelperClass = new UserHelperClass(Fullname, dateofbirth, gender, aadharNumber, Address);
                Utils.storeDocumentsInRTD(Adhaar.this, AppConstant.ADHAAR, Utils.toJson(userHelperClass, UserHelperClass.class));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadingDialog.dismissdialog();

                    }
                },4000);


            }
        });


        //Request for camera permission
        if (ContextCompat.checkSelfPermission(Adhaar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Adhaar.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        opencam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(Adhaar.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        final Calendar calendar = Calendar.getInstance();
        dobinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Adhaar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobinput.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

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
/*
    @Override
    public void onBackPressed() {
        //dismiss progress dialog
        progressDialog.dismiss();
    }*/

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this);
        sendDailog.show(getSupportFragmentManager(),"Send Dialog");
    }

    private void initViews() {
        adhharfullname = findViewById(R.id.adhhar_fullname);
        adharnumber = findViewById(R.id.adhar_number);
        adharaddres = findViewById(R.id.adhar_addres);
        addharsavebt = findViewById(R.id.addhr_savebt);
        dob = findViewById(R.id.dob);
        imageView = findViewById(R.id.adhar_imageView);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        imageView = findViewById(R.id.adhar_imageView);
        opencam = findViewById(R.id.cam_openbt);
        sendBT = findViewById(R.id.adhaar_sendBT);
        dobinput = findViewById(R.id.dob_calender);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Adhaar Form");
        Utils.makeButton(opencam, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(addharsavebt, getResources().getColor(R.color.colorPrimary), 40F);
    }

    @Override
    public void applyTexts(String message, String password) {
        //where to send and set the password and message

    }
}