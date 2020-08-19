package com.example.protectionapp.interfacecallbacks;


import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.model.AtmBean;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.BirthCertificateBean;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.PassportBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.VoteridBean;

public interface DocumentClickListener {
    void onSelectAdhaar(AdhaarBean adhaarBean);

    void onSelectPan(PanBean panBean);

    void onSelectDL(DlicenceBean dlicenceBean);

    void onSelectBank(BankBean bankBean);

    void onSelectAtm(AtmBean atmBean);

    void onSelectVoterID(VoteridBean voteridBean);

    void onSelectStudentID(StudentIdBean studentIdBean);


    void onSelectPassport(PassportBean passportBean);

    void onSelectBirthCertificate(BirthCertificateBean birthCertificateBean);
}
