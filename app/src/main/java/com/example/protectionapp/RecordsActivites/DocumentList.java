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
import com.example.protectionapp.adapters.AtmAdapter;
import com.example.protectionapp.adapters.BankAdapter;
import com.example.protectionapp.adapters.DrivingLicenseAdapter;
import com.example.protectionapp.adapters.PanAdapter;
import com.example.protectionapp.adapters.StudentIDAdapter;
import com.example.protectionapp.adapters.VoterIDAdapter;
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
    List<DlicenceBean> dlicenceBeanList = new ArrayList<>();
    List<BankBean> bankBeanList = new ArrayList<>();
    List<AtmBean> atmBeanList = new ArrayList<>();
    List<VoteridBean> voteridBeanList = new ArrayList<>();
    List<StudentIdBean> studentIdBeanList = new ArrayList<>();
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
                dlicenceBeanList.clear();
                bankBeanList.clear();
                voteridBeanList.clear();
                studentIdBeanList.clear();
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
                    } else if (personal_document.equals(AppConstant.DRIVING_LICENSE)) {
                        Log.e("eroor", postShot.getValue() + "");
                        DlicenceBean dlicenceBean = postShot.getValue(DlicenceBean.class);
                        if (dlicenceBean.getMobileno().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            dlicenceBeanList.add(dlicenceBean);
                        }
                    } else if (personal_document.equals(AppConstant.BANK)) {
                        Log.e("eroor", postShot.getValue() + "");
                        BankBean bankBean = postShot.getValue(BankBean.class);
                        if (bankBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            bankBeanList.add(bankBean);
                        }
                    } else if (personal_document.equals(AppConstant.ATM)) {
                        Log.e("eroor", postShot.getValue() + "");
                        AtmBean atmBean = postShot.getValue(AtmBean.class);
                        if (atmBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            atmBeanList.add(atmBean);
                        }
                    } else if (personal_document.equals(AppConstant.VOTER_ID)) {
                        Log.e("eroor", postShot.getValue() + "");
                        VoteridBean voteridBean = postShot.getValue(VoteridBean.class);
                        if (voteridBean.getVoterMobileNo().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            voteridBeanList.add(voteridBean);
                        }
                    } else if (personal_document.equals(AppConstant.STUDENT_ID)) {
                        Log.e("eroor", postShot.getValue() + "");
                        StudentIdBean studentIdBean = postShot.getValue(StudentIdBean.class);
                        if (studentIdBean.getMobilenumber().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            studentIdBeanList.add(studentIdBean);
                        }
                    }
                }
                if (adhaarBeanList.size() > 0) {
                    AdhaarAdapter adhaarAdapter = new AdhaarAdapter(DocumentList.this, adhaarBeanList, DocumentList.this);
                    rvDoc.setAdapter(adhaarAdapter);
                } else if (panBeanList.size() > 0) {
                    PanAdapter panAdapter = new PanAdapter(DocumentList.this, panBeanList, DocumentList.this);
                    rvDoc.setAdapter(panAdapter);
                } else if (dlicenceBeanList.size() > 0) {
                    DrivingLicenseAdapter drivingLicenseAdapter = new DrivingLicenseAdapter(DocumentList.this, dlicenceBeanList, DocumentList.this);
                    rvDoc.setAdapter(drivingLicenseAdapter);
                } else if (bankBeanList.size() > 0) {
                    BankAdapter bankAdapter = new BankAdapter(DocumentList.this, bankBeanList, DocumentList.this);
                    rvDoc.setAdapter(bankAdapter);
                } else if (atmBeanList.size() > 0) {
                    AtmAdapter atmAdapter = new AtmAdapter(DocumentList.this, atmBeanList, DocumentList.this);
                    rvDoc.setAdapter(atmAdapter);
                } else if (voteridBeanList.size() > 0) {
                    VoterIDAdapter voterIDAdapter = new VoterIDAdapter(DocumentList.this, voteridBeanList, DocumentList.this);
                    rvDoc.setAdapter(voterIDAdapter);
                } else if (studentIdBeanList.size() > 0) {
                    StudentIDAdapter studentIDAdapter = new StudentIDAdapter(DocumentList.this, studentIdBeanList, DocumentList.this);
                    rvDoc.setAdapter(studentIDAdapter);
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