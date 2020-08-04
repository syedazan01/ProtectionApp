package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

public class ATM extends AppCompatActivity {
    private Button btnAtmScan,btnAtmSave;
    private ImageView ivATM;
    private TextView tvToolbarTitle;
    TextInputLayout bankname,atmnumber,nameoncard,cardVailidity,cvvcode;
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
                if (TextUtils.isEmpty(bankname.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    bankname.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(atmnumber.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    atmnumber.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(nameoncard.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    return;
                }


                if (TextUtils.isEmpty(cardVailidity.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    adharaddres.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cvvcode.getEditText().getText().toString())) {
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

                UserHelperClass userHelperClass = new UserHelperClass(Fullname, dateofbirth, gender, aadharNumber, Address);
                Utils.storeDocumentsInRTD(Adhaar.this, AppConstant.ADHAAR, Utils.toJson(userHelperClass, UserHelperClass.class));
            }
        });
            }
        });
    }

    private void initViews() {
        bankname=findViewById(R.id.Bank_name);
        atmnumber=findViewById(R.id.atmnumberET);
        nameoncard=findViewById(R.id.cardnameET);
        cardVailidity=findViewById(R.id.cardvaliET);
        cvvcode=findViewById(R.id.cvvET);
        btnAtmScan=findViewById(R.id.btnAtmScan);
        btnAtmSave=findViewById(R.id.btnAtmSave);
        ivATM=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("ATM Detail Form");
        Utils.makeButton(btnAtmScan,getResources().getColor(R.color.colorAccent),40F);
        Utils.makeButton(btnAtmSave,getResources().getColor(R.color.colorPrimary),40F);
    }
}