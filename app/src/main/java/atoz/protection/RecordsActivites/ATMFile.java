package atoz.protection.RecordsActivites;

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
import atoz.protection.model.AtmBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ATMFile extends AppCompatActivity {
    TextInputEditText cardvaliET;
    TextInputLayout bankname, atmnumber, nameoncard, cardVailidity, cvvcode;
    private TextView tvToolbarTitle;
    private ImageView ivATM, ivatmscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_m_file);
        initViews();
        ivATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        bankname = findViewById(R.id.Bank_name);
        atmnumber = findViewById(R.id.atmnumberET);
        nameoncard = findViewById(R.id.cardnameET);
        cardVailidity = findViewById(R.id.cardvaliTxtIL);
        cardvaliET = findViewById(R.id.cardvaliET);
        cvvcode = findViewById(R.id.cvvET);

        ivATM = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("ATM Detail Form");
        ivatmscan = findViewById(R.id.atm_imageView1);
        if (getIntent().hasExtra(AppConstant.ATM)) {

            AtmBean atmBean = (AtmBean) getIntent().getSerializableExtra(AppConstant.ATM);
            bankname.getEditText().setText(atmBean.getBankname());
            atmnumber.getEditText().setText(atmBean.getAtmnumber());
            nameoncard.getEditText().setText(atmBean.getNameoncard());
            cardVailidity.getEditText().setText(atmBean.getCardVailidity());
            cvvcode.getEditText().setText(atmBean.getCvvcode());

            final ProgressDialog pd = Utils.getProgressDialog(ATMFile.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.ATM + "/" + atmBean.getAtmimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Glide.with(ATMFile.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivatmscan);
                    }
                }
            });
        }
    }
}