package com.example.protectionapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.protectionapp.RecordsActivites.DrivingLicenseFile;
import com.example.protectionapp.RecordsActivites.PanFile;
import com.example.protectionapp.RecordsActivites.SendDailog;
import com.example.protectionapp.RecordsActivites.StudentIDFile;
import com.example.protectionapp.RecordsActivites.VoterIDFile;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.adapters.AdapterFileShare;
import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.model.AtmBean;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.VoteridBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
        SendDailog sendDailog = new SendDailog(getActivity(), false);
        sendDailog.show(this.getChildFragmentManager(), "Send Dialog");
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

            Utils.getPersonalDocReference(fileShareBean.getDocument_type()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                        if (fileShareBean.getDocument_type().equals(AppConstant.ADHAAR)) {
                            AdhaarBean adhaarBean = postShot.getValue(AdhaarBean.class);
                            if (adhaarBean.getMobileNo().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), AdhaarFile.class).putExtra(AppConstant.ADHAAR, adhaarBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.PAN)) {
                            PanBean panBean = postShot.getValue(PanBean.class);
                            if (panBean.getPanmobile().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), PanFile.class).putExtra(AppConstant.PAN, panBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.DRIVING_LICENSE)) {
                            DlicenceBean dlicenceBean = postShot.getValue(DlicenceBean.class);
                            if (dlicenceBean.getMobileno().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), DrivingLicenseFile.class).putExtra(AppConstant.DRIVING_LICENSE, dlicenceBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.BANK)) {
                            BankBean bankBean = postShot.getValue(BankBean.class);
                            if (bankBean.getMobile().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), BankFile.class).putExtra(AppConstant.BANK, bankBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.ATM)) {
                            AtmBean atmBean = postShot.getValue(AtmBean.class);
                            if (atmBean.getMobile().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), ATMFile.class).putExtra(AppConstant.ATM, atmBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.VOTER_ID)) {
                            VoteridBean voteridBean = postShot.getValue(VoteridBean.class);
                            if (voteridBean.getVoterMobileNo().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), VoterIDFile.class).putExtra(AppConstant.VOTER_ID, voteridBean));
                                break;
                            }
                        } else if (fileShareBean.getDocument_type().equals(AppConstant.STUDENT_ID)) {
                            StudentIdBean studentIdBean = postShot.getValue(StudentIdBean.class);
                            if (studentIdBean.getMobilenumber().equals(fileShareBean.getSentFrom())) {
                                Log.e("dvbsfbdf", postShot.getValue() + "");
                                startActivity(new Intent(getActivity(), StudentIDFile.class).putExtra(AppConstant.STUDENT_ID, studentIdBean));
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