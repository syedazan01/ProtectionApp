package atoz.protection.interfacecallbacks;


import atoz.protection.model.AdhaarBean;
import atoz.protection.model.AtmBean;
import atoz.protection.model.BankBean;
import atoz.protection.model.BirthCertificateBean;
import atoz.protection.model.DlicenceBean;
import atoz.protection.model.PanBean;
import atoz.protection.model.PassportBean;
import atoz.protection.model.StudentIdBean;
import atoz.protection.model.VoteridBean;

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
