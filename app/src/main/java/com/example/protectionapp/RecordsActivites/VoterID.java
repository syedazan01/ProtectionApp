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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PanHelperClass;
import com.example.protectionapp.utils.Utils;
import com.example.protectionapp.utils.VoteridHelperClass;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VoterID extends AppCompatActivity  implements SendDailog.SendDialogListener{
    private Button btnVoteridscan, btnVoteridsave, btnVoteridsend;
    private ImageView ivVid;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, FatherName,dob,Address, AssemblyName;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    TextInputEditText dobET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private Uri fileUri;

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
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(VoterID.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
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


                VoteridHelperClass voteridHelperClass = new VoteridHelperClass(FullNames,fathersname,gender,Voterdob,address,assemblyNames);
                Utils.storeDocumentsInRTD(VoterID.this, AppConstant.DRIVING_LICENSE, Utils.toJson(voteridHelperClass, VoteridHelperClass.class));
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
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

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
        btnVoteridscan=findViewById(R.id.voterscanbt);
        btnVoteridsave=findViewById(R.id.voterid_savebt);
        btnVoteridsend = findViewById(R.id.voter_sendBT);
        FullName=findViewById(R.id.voteridFullname);
        FatherName=findViewById(R.id.voteridFathername);
        dob=findViewById(R.id.voterid_dob);
        Address=findViewById(R.id.voterid_addres);
        AssemblyName=findViewById(R.id.elction_constituency);
        radioGender = findViewById(R.id.VoteridGradio);
        radioMale = findViewById(R.id.Gen_votermale);
        radioFemale = findViewById(R.id.Gen_voterfemale);
        radioOther = findViewById(R.id.Gen_voterother);
        dobET=findViewById(R.id.voteriddob_calender);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Voter id Detail Form");
        ivVid = findViewById(R.id.ivBack);
        Utils.makeButton(btnVoteridscan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnVoteridsave, getResources().getColor(R.color.colorPrimary), 40F);
    }

    @Override
    public void applyTexts(String message, String password) {

    }
}