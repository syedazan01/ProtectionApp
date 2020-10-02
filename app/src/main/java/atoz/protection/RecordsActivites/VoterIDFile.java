package atoz.protection.RecordsActivites;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.model.VoteridBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class VoterIDFile extends AppCompatActivity {
    TextInputLayout FullName, FatherName, dob, Address, AssemblyName;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    private TextView tvToolbarTitle;
    private ImageView ivVid, ivVoterid,ivVoterid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_i_d_file);
        initViews();
        ivVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {

        FullName = findViewById(R.id.voteridFullname);
        FatherName = findViewById(R.id.voteridFathername);
        dob = findViewById(R.id.voterid_dob);
        ivVoterid = findViewById(R.id.ivVoterid_1);
        ivVoterid2 = findViewById(R.id.ivVoterid_2);
        Address = findViewById(R.id.voterid_addres);
        AssemblyName = findViewById(R.id.elction_constituency);
        radioGender = findViewById(R.id.VoteridGradio);
        radioMale = findViewById(R.id.Gen_votermale);
        radioFemale = findViewById(R.id.Gen_voterfemale);
        radioOther = findViewById(R.id.Gen_voterother);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Voter id Detail Form");
        ivVid = findViewById(R.id.ivBack);
        if (getIntent().hasExtra(AppConstant.VOTER_ID)) {
            VoteridBean voteridBean = (VoteridBean) getIntent().getSerializableExtra(AppConstant.VOTER_ID);
            FullName.getEditText().setText(voteridBean.getFullName());
            FatherName.getEditText().setText(voteridBean.getFathersName());
            dob.getEditText().setText(voteridBean.getDateofbirth());
            Address.getEditText().setText(voteridBean.getAddress());
            AssemblyName.getEditText().setText(voteridBean.getAssemblyname());
            if (voteridBean.getGender().equals("Male"))
                radioMale.setChecked(true);
            else if (voteridBean.getGender().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            final ProgressDialog pd = Utils.getProgressDialog(VoterIDFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.VOTER_ID + "/" + voteridBean.getVoterImage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(VoterIDFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivVoterid);
                    }
                }
            });

            Utils.getStorageReference().child(AppConstant.VOTER_ID + "/" + voteridBean.getVoterImage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(VoterIDFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivVoterid2);
                    }
                }
            });
        }
    }
}