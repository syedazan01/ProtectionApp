package com.example.protectionapp.RecordsActivites;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.protectionapp.R;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DrivingLicenseFile extends AppCompatActivity {

    TextInputLayout FullName, sonOf, LicenceNumber, BloodGroup, dob, dateofissue, validity;
    TextInputEditText dobET, dateOfIssueET, ValidityET;
    private ImageView ivDL, ivDLscan;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivinglicence_file);
        initViews();

    }

    private void initViews() {
        FullName = findViewById(R.id.drivingFullname);
        sonOf = findViewById(R.id.drivingFathersname);
        LicenceNumber = findViewById(R.id.LicenceNumber);
        BloodGroup = findViewById(R.id.bloodgroupDriving);
        dob = findViewById(R.id.Driving_dob);
        dateofissue = findViewById(R.id.Drivingdofissue);
        validity = findViewById(R.id.licence_validity);
        ivDLscan = findViewById(R.id.dl_imageview1);


        dobET = findViewById(R.id.Drivingdob_calender);
        dateOfIssueET = findViewById(R.id.dofissue_calender);
        ValidityET = findViewById(R.id.Licence_datevalidity);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Bank Detail Form");
        ivDL = findViewById(R.id.ivBack);
        if (getIntent().hasExtra(AppConstant.DRIVING_LICENSE)) {
            DlicenceBean dlicenceBean = (DlicenceBean) getIntent().getSerializableExtra(AppConstant.DRIVING_LICENSE);
            FullName.getEditText().setText(dlicenceBean.getFullname());
            sonOf.getEditText().setText(dlicenceBean.getSon_of());
            LicenceNumber.getEditText().setText(dlicenceBean.getLicenceNumber());
            BloodGroup.getEditText().setText(dlicenceBean.getBloodGroup());
            dob.getEditText().setText(dlicenceBean.getDateOfBirth());
            dateofissue.getEditText().setText(dlicenceBean.getDateOfBirth());
            validity.getEditText().setText(dlicenceBean.getDateOfBirth());

            final ProgressDialog pd = Utils.getProgressDialog(DrivingLicenseFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.ATM + "/" + dlicenceBean.getDLimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(DrivingLicenseFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivDLscan);
                    }
                }
            });
        }
    }

}