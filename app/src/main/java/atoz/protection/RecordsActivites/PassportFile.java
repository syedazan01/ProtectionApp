package atoz.protection.RecordsActivites;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.PassportBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class PassportFile extends AppCompatActivity {
    TextInputEditText dobinput, dateofissue, dateofexpiry;
    TextInputLayout passportnumber, type, countrycode, nationality, fullname, placeofissue, placeofbirth;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView, imageView2;
    ImageView ivBack;
    TextView tvToolbarTitle;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    private Uri fileUri, fileUri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport_file);
        initViews();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        passportnumber = findViewById(R.id.passport_number);
        type = findViewById(R.id.passport_type);
        countrycode = findViewById(R.id.passport_countrycode);
        nationality = findViewById(R.id.passport_nationality);
        fullname = findViewById(R.id.passport_fullname);
        placeofissue = findViewById(R.id.passport_placeofissue);
        placeofbirth = findViewById(R.id.passport_placeofbirth);
        dobinput = findViewById(R.id.dob_calender_passport);
        dateofissue = findViewById(R.id.dofissue_calender);
        dateofexpiry = findViewById(R.id.passport_datevalidity);
        imageView = findViewById(R.id.passport_imageView1);
        imageView2 = findViewById(R.id.passport_imageView2);
        ivBack = findViewById(R.id.ivBack);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Passport Form");
        if (getIntent().hasExtra(AppConstant.PASSPORT)) {
            PassportBean passportBean = (PassportBean) getIntent().getSerializableExtra(AppConstant.PASSPORT);
            passportnumber.getEditText().setText(passportBean.getPassportnumber());
            type.getEditText().setText(passportBean.getType());
            countrycode.getEditText().setText(passportBean.getCountrycode());
            nationality.getEditText().setText(passportBean.getNationalty());
            fullname.getEditText().setText(passportBean.getFullname());
            dobinput.setText(passportBean.getDateofbirth());
            placeofbirth.getEditText().setText(passportBean.getPlaceofbirth());
            placeofissue.getEditText().setText(passportBean.getPlaceofissue());
            dateofissue.setText(passportBean.getDateofissue());
            dateofexpiry.setText(passportBean.getDateofexpiry());
            if (passportBean.getSex().equals("Male"))
                radioMale.setChecked(true);
            else if (passportBean.getSex().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            final ProgressDialog pd = Utils.getProgressDialog(PassportFile.this);
            pd.show();

            Utils.getStorageReference().child(AppConstant.PASSPORT + "/" + passportBean.getPassportimage1()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(PassportFile.this).load(task.getResult())
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
                        Glide.with(PassportFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }

            });
        }
    }
}