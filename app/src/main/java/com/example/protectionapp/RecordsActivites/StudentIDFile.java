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
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class StudentIDFile extends AppCompatActivity {
    TextInputLayout institutionname, enroll, rollno, fullname, fathername, branch;
    private ImageView ivBack, imageViewstid;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_i_d_file);
        initViws();
    }

    private void initViws() {
        institutionname = findViewById(R.id.institution_name_ET);
        enroll = findViewById(R.id.enroll_NoTL);
        rollno = findViewById(R.id.roll_noTL);
        fullname = findViewById(R.id.STfullname_TL);
        fathername = findViewById(R.id.fathername_IL);
        branch = findViewById(R.id.branch_name_IL);
        imageViewstid = findViewById(R.id.ivStudentid_1);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Student Detail Form");
        ivBack = findViewById(R.id.ivBack);

        //filling view document
        if (getIntent().hasExtra(AppConstant.STUDENT_ID)) {
            StudentIdBean studentIdBean = (StudentIdBean) getIntent().getSerializableExtra(AppConstant.STUDENT_ID);
            institutionname.getEditText().setText(studentIdBean.getInstitutionname());
            enroll.getEditText().setText(studentIdBean.getEnroll());
            rollno.getEditText().setText(studentIdBean.getRollno());
            fullname.getEditText().setText(studentIdBean.getFullname());
            fathername.getEditText().setText(studentIdBean.getFathername());
            branch.getEditText().setText(studentIdBean.getBranch());
            final ProgressDialog pd = Utils.getProgressDialog(StudentIDFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.PAN + "/" + studentIdBean.getStudntidimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(StudentIDFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageViewstid);
                    }
                }
            });
        }
    }
}