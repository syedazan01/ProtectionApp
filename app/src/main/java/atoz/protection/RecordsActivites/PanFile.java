package atoz.protection.RecordsActivites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.model.PanBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PanFile extends AppCompatActivity {
    TextInputLayout FullName, FatherName, dob, PermanentAccountNumber;
    TextInputEditText dobET;
    Activity activity = this;
    UploadingDialog uploadingDialog = new UploadingDialog(PanFile.this);
    private ImageView ivPAN, pan_imageview;
    private TextView tvToolbarTitle;
    private ImageView pan_imageview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_file);
        initViews();
        initActions();
    }

    private void initViews() {

        FullName = findViewById(R.id.panFullname);
        FatherName = findViewById(R.id.panFathersname);
        pan_imageview2 = findViewById(R.id.pan_imageview2);
        pan_imageview = findViewById(R.id.pan_imageview1);
        dob = findViewById(R.id.pan_dob);
        PermanentAccountNumber = findViewById(R.id.pan_number);
        dobET = findViewById(R.id.pandob_calender);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("PAN Detail Form");
        ivPAN = findViewById(R.id.ivBack);

        PanBean panBean = (PanBean) getIntent().getSerializableExtra(AppConstant.PAN);
        FullName.getEditText().setText(panBean.getFullName());
        FatherName.getEditText().setText(panBean.getFatherName());
        PermanentAccountNumber.getEditText().setText(panBean.getPermanentAccountNumber());
        dobET.setText(panBean.getDateOfBirth());
        final ProgressDialog pd = Utils.getProgressDialog(PanFile.this);
        pd.show();
        Utils.getStorageReference().child(AppConstant.PAN + "/" + panBean.getPanimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                pd.dismiss();
                if (task.isSuccessful()) {

                    Glide.with(PanFile.this).load(task.getResult())
                            .error(R.drawable.login_logo)
                            .placeholder(R.drawable.login_logo)
                            .into(pan_imageview);
                }
            }
        });
        Utils.getStorageReference().child(AppConstant.PAN + "/" + panBean.getPanimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                pd.dismiss();
                if (task.isSuccessful()) {
                    Glide.with(PanFile.this).load(task.getResult())
                            .error(R.drawable.login_logo)
                            .placeholder(R.drawable.login_logo)
                            .into(pan_imageview2);
                }
            }
        });
    }

    private void initActions() {

        //open dialog for send file

        ivPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}