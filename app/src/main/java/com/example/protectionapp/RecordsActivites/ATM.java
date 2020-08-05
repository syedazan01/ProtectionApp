package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.AtmHelperClass;
import com.example.protectionapp.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ATM extends AppCompatActivity {
    private Button btnAtmScan, btnAtmSave;
    private ImageView ivATM;
    private TextView tvToolbarTitle;
    TextInputEditText cardvaliET;
    TextInputLayout bankname, atmnumber, nameoncard, cardVailidity, cvvcode;
    int yearofdob, monthofdob, dayofdob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_m);
        initViews();
        initActions();


        //Request for camera permission
        if (ContextCompat.checkSelfPermission(ATM.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ATM.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        btnAtmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(ATM.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


    }

    private void initActions() {
        ivATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAtmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the values
                String banknames = bankname.getEditText().getText().toString();
                String atmnumbers = atmnumber.getEditText().getText().toString();
                String nameoncards = nameoncard.getEditText().getText().toString();
                String cardVailiditys = cardVailidity.getEditText().getText().toString();
                String cvvcodes = cvvcode.getEditText().getText().toString();


                AtmHelperClass atmHelperClass = new AtmHelperClass(banknames,atmnumbers,nameoncards,cardVailiditys,cvvcodes);
                Utils.storeDocumentsInRTD(ATM.this, AppConstant.ATM, Utils.toJson(atmHelperClass, AtmHelperClass.class));
            }
        });
        final Calendar calendar = Calendar.getInstance();
        cardvaliET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ATM.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        cardvaliET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });


    }


    private void initViews(){
        bankname=findViewById(R.id.Bank_name);
        atmnumber=findViewById(R.id.atmnumberET);
        nameoncard=findViewById(R.id.cardnameET);
        cardVailidity=findViewById(R.id.cardvaliTxtIL);
        cardvaliET=findViewById(R.id.cardvaliET);
        cvvcode=findViewById(R.id.cvvET);
        btnAtmScan=findViewById(R.id.btnAtmScan);
        btnAtmSave=findViewById(R.id.btnAtmSave);
        ivATM=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("ATM Detail Form");
        Utils.makeButton(btnAtmScan,getResources().getColor(R.color.colorAccent),40F);
        Utils.makeButton(btnAtmSave,getResources().getColor(R.color.colorPrimary),40F);
    }

};




