package com.example.protectionapp.RecordsActivites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdhaarAdapter;
import com.example.protectionapp.adapters.PanAdapter;
import com.example.protectionapp.interfacecallbacks.DocumentClickListener;
import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.model.AtmBean;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.VoteridBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DocumentList extends AppCompatActivity implements DocumentClickListener {
    TextView tvToolbarTitle;
    ImageView ivBack;
    RecyclerView rvDoc;
    FloatingActionButton fabInsertDoc;
    List<AdhaarBean> adhaarBeanList = new ArrayList<>();
    List<PanBean> panBeanList = new ArrayList<>();
    String personal_document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        initViews();
        initActions();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rvDoc.setLayoutManager(new LinearLayoutManager(this));
        final ProgressDialog pd = Utils.getProgressDialog(this);
        pd.show();
        personal_document = getIntent().getStringExtra(AppConstant.PERSONAL_DOCUMENT);
        Utils.getPersonalDocReference(personal_document).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                adhaarBeanList.clear();
                panBeanList.clear();
                for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                    if (personal_document.equals(AppConstant.ADHAAR)) {
                        AdhaarBean adhaarBean = postShot.getValue(AdhaarBean.class);
                        if (adhaarBean.getMobileNo().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            adhaarBeanList.add(adhaarBean);
                        }
                    } else if (personal_document.equals(AppConstant.PAN)) {
                        Log.e("eroor", postShot.getValue() + "");
                        PanBean panBean = postShot.getValue(PanBean.class);
                        if (panBean.getPanmobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            panBeanList.add(panBean);
                        }
                    }
                }
                if (adhaarBeanList.size() > 0) {
                    AdhaarAdapter adhaarAdapter = new AdhaarAdapter(DocumentList.this, adhaarBeanList, DocumentList.this);
                    rvDoc.setAdapter(adhaarAdapter);
                } else if (panBeanList.size() > 0) {
                    PanAdapter panAdapter = new PanAdapter(DocumentList.this, panBeanList, DocumentList.this);
                    rvDoc.setAdapter(panAdapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
        fabInsertDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personal_document.equals(AppConstant.ADHAAR)) {
                    startActivity(new Intent(DocumentList.this, Adhaar.class));
                } else if (personal_document.equals(AppConstant.PAN)) {
                    startActivity(new Intent(DocumentList.this, PAN.class));
                } else if (personal_document.equals(AppConstant.DRIVING_LICENSE)) {
                    startActivity(new Intent(DocumentList.this, DrivingLicence.class));
                } else if (personal_document.equals(AppConstant.BANK)) {
                    startActivity(new Intent(DocumentList.this, Bank.class));
                } else if (personal_document.equals(AppConstant.ATM)) {
                    startActivity(new Intent(DocumentList.this, ATM.class));
                } else if (personal_document.equals(AppConstant.VOTER_ID)) {
                    startActivity(new Intent(DocumentList.this, VoterID.class));
                } else if (personal_document.equals(AppConstant.STUDENT_ID)) {
                    startActivity(new Intent(DocumentList.this, StudentID.class));
                }
            }
        });
    }

    private void initViews() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBack = findViewById(R.id.ivBack);
        rvDoc = findViewById(R.id.rvDocument);
        fabInsertDoc = findViewById(R.id.fabInsertDoc);
        tvToolbarTitle.setText(getIntent().getStringExtra(AppConstant.PERSONAL_DOCUMENT));
    }

    @Override
    public void onSelectAdhaar(AdhaarBean adhaarBean) {
        Intent intent = new Intent(this, Adhaar.class);
        intent.putExtra(AppConstant.ADHAAR, adhaarBean);
        startActivity(intent);
    }

    @Override
    public void onSelectPan(PanBean panBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.PAN, panBean);
        startActivity(intent);
    }

    @Override
    public void onSelectDL(DlicenceBean dlicenceBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.DRIVING_LICENSE, dlicenceBean);
        startActivity(intent);
    }

    @Override
    public void onSelectBank(BankBean bankBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.BANK, bankBean);
        startActivity(intent);
    }

    @Override
    public void onSelectAtm(AtmBean atmBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.ATM, atmBean);
        startActivity(intent);
    }

    @Override
    public void onSelectVoterID(VoteridBean voteridBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.VOTER_ID, voteridBean);
        startActivity(intent);
    }

    @Override
    public void onSelectStudentID(StudentIdBean studentIdBean) {
        Intent intent = new Intent(this, PAN.class);
        intent.putExtra(AppConstant.STUDENT_ID, studentIdBean);
        startActivity(intent);
    }
}