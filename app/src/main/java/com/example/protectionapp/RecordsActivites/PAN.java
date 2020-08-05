package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.DlicenceHelperClass;
import com.example.protectionapp.utils.PanHelperClass;
import com.example.protectionapp.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PAN extends AppCompatActivity {
    private Button btnPANscan, btnPANsave;
    private ImageView ivPAN;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, FatherName, dob, PermanentAccountNumber;
    TextInputEditText dobET;
    int yearofdob, monthofdob, dayofdob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_a_n);
        initViews();
        initActions();
        if (ContextCompat.checkSelfPermission(PAN.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PAN.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        btnPANscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(PAN.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }

    private void initViews() {
        btnPANscan = findViewById(R.id.panscanbt);
        btnPANsave = findViewById(R.id.pansavebt);
        FullName = findViewById(R.id.panFullname);
        FatherName = findViewById(R.id.panFathersname);
        dob = findViewById(R.id.pan_dob);
        PermanentAccountNumber = findViewById(R.id.pan_number);
        dobET=findViewById(R.id.pandob_calender);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("PAN Detail Form");
        ivPAN = findViewById(R.id.ivBack);
        Utils.makeButton(btnPANscan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnPANsave, getResources().getColor(R.color.colorPrimary), 40F);
    }

    private void initActions() {
        ivPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnPANsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FullNames = FullName.getEditText().getText().toString();
                String fathersname = FatherName.getEditText().getText().toString();
                String pandob = dob.getEditText().getText().toString();
                String pannumber = PermanentAccountNumber.getEditText().getText().toString();


                PanHelperClass panHelperClass = new PanHelperClass(FullNames, fathersname, pandob, pannumber);
                Utils.storeDocumentsInRTD(PAN.this, AppConstant.DRIVING_LICENSE, Utils.toJson(panHelperClass, PanHelperClass.class));
            }
        });
        final Calendar calendar = Calendar.getInstance();
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PAN.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
    }
}