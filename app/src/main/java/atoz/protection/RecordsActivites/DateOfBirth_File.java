package atoz.protection.RecordsActivites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.model.BirthCertificateBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DateOfBirth_File extends AppCompatActivity {

    TextInputLayout dateofbirth,fathername, mothername, childname, hospitalname;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_of_birth__file);
        initViews();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        fathername = findViewById(R.id.birth_fathername);
        mothername = findViewById(R.id.birth_mothername);
        childname = findViewById(R.id.birth_childname);
        dateofbirth = findViewById(R.id.birth_dob);
        hospitalname = findViewById(R.id.birth_hospitalname);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        imageView = findViewById(R.id.birth_imageview1);
        radioOther = findViewById(R.id.Gen_other);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Passport Form");
        if (getIntent().hasExtra(AppConstant.BIRTH_CERTIFICATE)) {
            BirthCertificateBean birthCertificateBean = (BirthCertificateBean) getIntent().getSerializableExtra(AppConstant.BIRTH_CERTIFICATE);
            fathername.getEditText().setText(birthCertificateBean.getFathername());
            mothername.getEditText().setText(birthCertificateBean.getMothername());
            childname.getEditText().setText(birthCertificateBean.getChildname());
            if (birthCertificateBean.getSex().equals("Male"))
                radioMale.setChecked(true);
            else if (birthCertificateBean.getSex().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            dateofbirth.getEditText().setText(birthCertificateBean.getDateofbirth());
            hospitalname.getEditText().setText(birthCertificateBean.getHospitalname());

            final ProgressDialog pd = Utils.getProgressDialog(DateOfBirth_File.this);
            pd.show();

            Utils.getStorageReference().child(AppConstant.BIRTH_CERTIFICATE + "/" + birthCertificateBean.getImageview1()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(DateOfBirth_File.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView);
                    }
                }

            });
           /* Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + birthCertificateBean.getImageview2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(DateOfBirth_File.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }

            });*/
        }
    }
}