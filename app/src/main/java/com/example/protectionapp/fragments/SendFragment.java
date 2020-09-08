package com.example.protectionapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.ATMFile;
import com.example.protectionapp.RecordsActivites.AdhaarFile;
import com.example.protectionapp.RecordsActivites.BankFile;
import com.example.protectionapp.RecordsActivites.DateOfBirth_File;
import com.example.protectionapp.RecordsActivites.DrivingLicenseFile;
import com.example.protectionapp.RecordsActivites.PanFile;
import com.example.protectionapp.RecordsActivites.PassportFile;
import com.example.protectionapp.RecordsActivites.SendDailog;
import com.example.protectionapp.RecordsActivites.StudentIDFile;
import com.example.protectionapp.RecordsActivites.VoterIDFile;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.adapters.AdapterFileShare;
import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.model.AtmBean;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.BirthCertificateBean;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.MediaDocBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.PassportBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.VoteridBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class SendFragment extends Fragment implements AdapterFileShare.FileShareClickListener, FileShare.SendClickListener, SendDailog.SendDialogListener {
    public static FileShare.SendClickListener sendClickListener = null;
    public static SendDailog.SendDialogListener sendDialogListener = null;
    private RecyclerView rvSentList;
    private AdapterFileShare adapterFileShare;
    private List<FileShareBean> fileShareBeans = new ArrayList<>();
    private FileShareBean fileShareBean;

    public SendFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        rvSentList = view.findViewById(R.id.rvSentList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendDialogListener = this;
        rvSentList.setLayoutManager(new LinearLayoutManager(getContext()));
        sendClickListener = this;
        ProgressDialog pd = Utils.getProgressDialog(getActivity());
        pd.show();
        Utils.getFileShareReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    FileShareBean fileShareBean = postSnap.getValue(FileShareBean.class);
                    if (fileShareBean.getSentFrom().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                        fileShareBeans.add(fileShareBean);
                    }
                    adapterFileShare = new AdapterFileShare(getActivity(), fileShareBeans, SendFragment.this, "Send");
                    rvSentList.setAdapter(adapterFileShare);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onSelect(FileShareBean fileShareBean) {
        this.fileShareBean = fileShareBean;
        SendDailog sendDailog = new SendDailog(getActivity(), false, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //        //sendDailog.show(this.getChildFragmentManager(), "Send Dialog");
    }

    @Override
    public void onSent() {
        fileShareBeans.clear();
        ProgressDialog pd = Utils.getProgressDialog(getActivity());
        pd.show();
        Utils.getFileShareReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                fileShareBeans.clear();
                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    FileShareBean fileShareBean = postSnap.getValue(FileShareBean.class);
                    if (fileShareBean.getSentFrom().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                        fileShareBeans.add(fileShareBean);
                    }
                    adapterFileShare = new AdapterFileShare(getActivity(), fileShareBeans, SendFragment.this, "Send");
                    rvSentList.setAdapter(adapterFileShare);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void applyTexts(String message, String password) {
        if (password.equals(fileShareBean.getPassword())) {
            String documentType=fileShareBean.getDocument_type();
            if(fileShareBean.getDocument_type().equals(AppConstant.PDF_DOC) || fileShareBean.getDocument_type().equals(AppConstant.DOC_IMAGE))
            {
                documentType=AppConstant.MEDIA_DOC;
            }
            Utils.getPersonalDocReference(documentType).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                        Log.e("dvbsfbdf", postShot.getValue() + "");
                        if (fileShareBean.getDocument_type().equals(AppConstant.ADHAAR)) {
                            AdhaarBean adhaarBean = postShot.getValue(AdhaarBean.class);
                            if (adhaarBean.getId()==fileShareBean.getId() && adhaarBean.getMobileNo().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), AdhaarFile.class).putExtra(AppConstant.ADHAAR, adhaarBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.PAN)) {
                            PanBean panBean = postShot.getValue(PanBean.class);
                            if (panBean.getId()==fileShareBean.getId() && panBean.getPanmobile().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), PanFile.class).putExtra(AppConstant.PAN, panBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.DRIVING_LICENSE)) {
                            DlicenceBean dlicenceBean = postShot.getValue(DlicenceBean.class);
                            if (dlicenceBean.getId()==fileShareBean.getId() && dlicenceBean.getMobileno().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), DrivingLicenseFile.class).putExtra(AppConstant.DRIVING_LICENSE, dlicenceBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.BANK)) {
                            BankBean bankBean = postShot.getValue(BankBean.class);
                            if (bankBean.getId()==fileShareBean.getId() && bankBean.getMobile().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), BankFile.class).putExtra(AppConstant.BANK, bankBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.ATM)) {
                            AtmBean atmBean = postShot.getValue(AtmBean.class);
                            if (atmBean.getId()==fileShareBean.getId() && atmBean.getMobile().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), ATMFile.class).putExtra(AppConstant.ATM, atmBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.VOTER_ID)) {
                            VoteridBean voteridBean = postShot.getValue(VoteridBean.class);
                            if (voteridBean.getId()==fileShareBean.getId() && voteridBean.getVoterMobileNo().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), VoterIDFile.class).putExtra(AppConstant.VOTER_ID, voteridBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.STUDENT_ID)) {
                            StudentIdBean studentIdBean = postShot.getValue(StudentIdBean.class);
                            if (studentIdBean.getId()==fileShareBean.getId() && studentIdBean.getMobilenumber().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), StudentIDFile.class).putExtra(AppConstant.STUDENT_ID, studentIdBean));
                                break;
                            }
                        }
                        else if(fileShareBean.getDocument_type().equals(AppConstant.PASSPORT))
                        {
                            PassportBean passportBean = postShot.getValue(PassportBean.class);
                            if (passportBean.getId()==fileShareBean.getId() && passportBean.getMobilenumber().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), PassportFile.class).putExtra(AppConstant.PASSPORT, passportBean));
                                break;
                            }
                        }
                        else if(fileShareBean.getDocument_type().equals(AppConstant.BIRTH_CERTIFICATE))
                        {
                            BirthCertificateBean birthCertificateBean = postShot.getValue(BirthCertificateBean.class);
                            if (birthCertificateBean.getId()==fileShareBean.getId() && birthCertificateBean.getMoblilenumber().equals(fileShareBean.getSentFrom())) {
                                
                                startActivity(new Intent(getActivity(), DateOfBirth_File.class).putExtra(AppConstant.BIRTH_CERTIFICATE, birthCertificateBean));
                                break;
                            }
                        }
                        else if(fileShareBean.getDocument_type().equals(AppConstant.PDF_DOC) || fileShareBean.getDocument_type().equals(AppConstant.DOC_IMAGE))
                        {
                            MediaDocBean mediaDocBean = postShot.getValue(MediaDocBean.class);
                            Log.e("fdfdfgdfg",mediaDocBean.getId()+" "+fileShareBean.getId());
                            if (mediaDocBean.getId()==fileShareBean.getId() && mediaDocBean.getDocMobile().equals(fileShareBean.getSentFrom())) {
                                
                                ProgressDialog pd = Utils.getProgressDialog(getActivity());
                                pd.show();
                                Utils.getStorageReference()
                                        .child(AppConstant.MEDIA_DOC + "/" + mediaDocBean.getDocUrl())
                                        .getDownloadUrl()
                                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                pd.dismiss();
                                                Intent it = new Intent(Intent.ACTION_VIEW);
                                                String mediaType;
                                                if (fileShareBean.getDocument_type().equals(AppConstant.DOC_IMAGE))
                                                    mediaType = "image/*";
                                                else
                                                    mediaType = "application/pdf";
                                                it.setDataAndType(task.getResult(), mediaType);
                                                startActivity(it);
                                            }
                                        });
                                break;
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else
            Utils.showToast(getActivity(), "Password is incorrect", AppConstant.errorColor);
    }
}