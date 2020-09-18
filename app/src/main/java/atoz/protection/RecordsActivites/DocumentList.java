package atoz.protection.RecordsActivites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.adapters.AdapterUsers;
import atoz.protection.adapters.AdhaarAdapter;
import atoz.protection.adapters.AtmAdapter;
import atoz.protection.adapters.BankAdapter;
import atoz.protection.adapters.BirthCertificateAdapter;
import atoz.protection.adapters.DrivingLicenseAdapter;
import atoz.protection.adapters.MediaDocsAdapter;
import atoz.protection.adapters.PanAdapter;
import atoz.protection.adapters.PassportAdapter;
import atoz.protection.adapters.StudentIDAdapter;
import atoz.protection.adapters.VoterIDAdapter;
import atoz.protection.interfacecallbacks.DocumentClickListener;
import atoz.protection.model.AdhaarBean;
import atoz.protection.model.AtmBean;
import atoz.protection.model.BankBean;
import atoz.protection.model.BirthCertificateBean;
import atoz.protection.model.DlicenceBean;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.MediaDocBean;
import atoz.protection.model.NotificationBean;
import atoz.protection.model.PanBean;
import atoz.protection.model.PassportBean;
import atoz.protection.model.StudentIdBean;
import atoz.protection.model.UserBean;
import atoz.protection.model.VoteridBean;
import atoz.protection.network.ApiResonse;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DocumentList extends AppCompatActivity implements DocumentClickListener, MediaDocsAdapter.MediaDocsClickListener, SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private List<FileShareBean> fileShareBeans=new ArrayList<>();
    private List<String> tokenList=new ArrayList<>();
    
    private UploadingDialog uploadingDialog;
    ProgressDialog pd;
    TextView tvToolbarTitle;
    ImageView ivBack;
    RecyclerView rvDoc;
    ImageView ivInsertDoc;
    List<AdhaarBean> adhaarBeanList = new ArrayList<>();
    List<PanBean> panBeanList = new ArrayList<>();
    List<DlicenceBean> dlicenceBeanList = new ArrayList<>();
    List<BankBean> bankBeanList = new ArrayList<>();
    List<AtmBean> atmBeanList = new ArrayList<>();
    List<VoteridBean> voteridBeanList = new ArrayList<>();
    List<StudentIdBean> studentIdBeanList = new ArrayList<>();
    List<PassportBean> passportBeansList = new ArrayList<>();
    List<BirthCertificateBean> birthCertificateBeanList = new ArrayList<>();
    private List<MediaDocBean> mediaDocBeanList = new ArrayList<>();
    String personal_document;
    LinearLayout llNoData;
    ImageView ivDocType;
    TextView tvDocName;
    private Activity activity = this;
    private MediaDocsAdapter mediaDocsAdapter;
    private MediaDocBean mediaDocBean;
    private String msg,password;
    private String documentType;

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
                passportBeansList.clear();
                birthCertificateBeanList.clear();
                mediaDocBeanList.clear();

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
                    } else if (personal_document.equals(AppConstant.PASSPORT)) {
                        Log.e("eroor", postShot.getValue() + "");
                        PassportBean passportBean = postShot.getValue(PassportBean.class);
                        if (passportBean.getMobilenumber().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            passportBeansList.add(passportBean);
                        }
                    } else if (personal_document.equals(AppConstant.BIRTH_CERTIFICATE)) {
                        Log.e("eroor", postShot.getValue() + "");
                        BirthCertificateBean birthCertificateBean = postShot.getValue(BirthCertificateBean.class);
                        if (birthCertificateBean.getMoblilenumber().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            birthCertificateBeanList.add(birthCertificateBean);
                        }
                    } else {
                        MediaDocBean mediaDocBean = postShot.getValue(MediaDocBean.class);

                        if (mediaDocBean.getDocMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            mediaDocBeanList.add(mediaDocBean);
                        }
                    }
                }


                if (adhaarBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    AdhaarAdapter adhaarAdapter = new AdhaarAdapter(DocumentList.this, adhaarBeanList, DocumentList.this);
                    rvDoc.setAdapter(adhaarAdapter);
                } else if (panBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    PanAdapter panAdapter = new PanAdapter(DocumentList.this, panBeanList, DocumentList.this);
                    rvDoc.setAdapter(panAdapter);
                } else if (dlicenceBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    DrivingLicenseAdapter drivingLicenseAdapter = new DrivingLicenseAdapter(DocumentList.this, dlicenceBeanList, DocumentList.this);
                    rvDoc.setAdapter(drivingLicenseAdapter);
                } else if (bankBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    BankAdapter bankAdapter = new BankAdapter(DocumentList.this, bankBeanList, DocumentList.this);
                    rvDoc.setAdapter(bankAdapter);
                } else if (atmBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    AtmAdapter atmAdapter = new AtmAdapter(DocumentList.this, atmBeanList, DocumentList.this);
                    rvDoc.setAdapter(atmAdapter);
                } else if (voteridBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    VoterIDAdapter voterIDAdapter = new VoterIDAdapter(DocumentList.this, voteridBeanList, DocumentList.this);
                    rvDoc.setAdapter(voterIDAdapter);
                } else if (studentIdBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    StudentIDAdapter studentIDAdapter = new StudentIDAdapter(DocumentList.this, studentIdBeanList, DocumentList.this);
                    rvDoc.setAdapter(studentIDAdapter);
                } else if (passportBeansList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    PassportAdapter passportAdapter = new PassportAdapter(DocumentList.this, passportBeansList, DocumentList.this);
                    rvDoc.setAdapter(passportAdapter);
                } else if (birthCertificateBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    BirthCertificateAdapter birthCertificateAdapter = new BirthCertificateAdapter(DocumentList.this, birthCertificateBeanList, DocumentList.this);
                    rvDoc.setAdapter(birthCertificateAdapter);
                } else if (mediaDocBeanList.size() > 0) {
                    rvDoc.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    mediaDocsAdapter = new MediaDocsAdapter(activity, mediaDocBeanList, DocumentList.this);
                    rvDoc.setAdapter(mediaDocsAdapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
        ivInsertDoc.setOnClickListener(new View.OnClickListener() {
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
                } else if (personal_document.equals(AppConstant.PASSPORT)) {
                    startActivity(new Intent(DocumentList.this, Passport.class));
                } else if (personal_document.equals(AppConstant.BIRTH_CERTIFICATE)) {
                    startActivity(new Intent(DocumentList.this, DateOfBirth.class));
                } else {
                    Utils.showDocsDialog(DocumentList.this);
                }
            }
        });
    }

    private void initViews() {
        personal_document = getIntent().getStringExtra(AppConstant.PERSONAL_DOCUMENT);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBack = findViewById(R.id.ivBack);
        rvDoc = findViewById(R.id.rvDocument);
        ivInsertDoc = findViewById(R.id.ivInsertDoc);
        llNoData = findViewById(R.id.llNoData);
        ivDocType = findViewById(R.id.ivDocType);
        tvDocName = findViewById(R.id.tvDocName);
        uploadingDialog = new UploadingDialog(this);
        pd=Utils.getProgressDialog(activity);
        tvToolbarTitle.setText(getIntent().getStringExtra(AppConstant.PERSONAL_DOCUMENT));
        int resId = 0;
        String docName = "";
        if (personal_document.equals(AppConstant.ADHAAR)) {
            resId = R.drawable.aadharlogo;
            docName = "Add your Aadhaar Card";
        } else if (personal_document.equals(AppConstant.PAN)) {
            resId = R.drawable.panlogo;
            docName = "Add your Pan Card";
        } else if (personal_document.equals(AppConstant.DRIVING_LICENSE)) {
            resId = R.drawable.drivinglogo;
            docName = "Add your Driving Licence";
        } else if (personal_document.equals(AppConstant.BANK)) {
            resId = R.drawable.banknew;
            docName = "Add your Bank Account";
        } else if (personal_document.equals(AppConstant.ATM)) {
            resId = R.drawable.atmlogo;
            docName = "Add your Debit Card";
        } else if (personal_document.equals(AppConstant.VOTER_ID)) {
            resId = R.drawable.voteridlogo;
            docName = "Add your VOTER ID";
        } else if (personal_document.equals(AppConstant.STUDENT_ID)) {
            resId = R.drawable.student_id;
            docName = "Add your Student ID";
        } else if (personal_document.equals(AppConstant.BIRTH_CERTIFICATE)) {
            resId = R.drawable.birthlogo;
            docName = "Add your Birth Certificate";
        } else if (personal_document.equals(AppConstant.PASSPORT)) {
            resId = R.drawable.passportlogo;
            docName = "Add your Passport";
        } else {
            resId = R.drawable.file;
            docName = "Add your PDF and Camera";
        }
        ivDocType.setImageResource(resId);
        tvDocName.setText(docName);
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
        Intent intent = new Intent(this, DrivingLicence.class);
        intent.putExtra(AppConstant.DRIVING_LICENSE, dlicenceBean);
        startActivity(intent);
    }

    @Override
    public void onSelectBank(BankBean bankBean) {
        Intent intent = new Intent(this, Bank.class);
        intent.putExtra(AppConstant.BANK, bankBean);
        startActivity(intent);
    }

    @Override
    public void onSelectAtm(AtmBean atmBean) {
        Intent intent = new Intent(this, ATM.class);
        intent.putExtra(AppConstant.ATM, atmBean);
        startActivity(intent);
    }

    @Override
    public void onSelectVoterID(VoteridBean voteridBean) {
        Intent intent = new Intent(this, VoterID.class);
        intent.putExtra(AppConstant.VOTER_ID, voteridBean);
        startActivity(intent);
    }

    @Override
    public void onSelectStudentID(StudentIdBean studentIdBean) {
        Intent intent = new Intent(this, StudentID.class);
        intent.putExtra(AppConstant.STUDENT_ID, studentIdBean);
        startActivity(intent);
    }

    @Override
    public void onSelectPassport(PassportBean passportBean) {
        Intent intent = new Intent(this, Passport.class);
        intent.putExtra(AppConstant.PASSPORT, passportBean);
        startActivity(intent);

    }

    @Override
    public void onSelectBirthCertificate(BirthCertificateBean birthCertificateBean) {
        Intent intent = new Intent(this, DateOfBirth.class);
        intent.putExtra(AppConstant.BIRTH_CERTIFICATE, birthCertificateBean);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to image  view
            imageView.setImageBitmap(captureImage);
        }*/
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            if (requestCode == AppConstant.CHOOSE_PDF_REQUESTCODE) {
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                if (files != null && files.size() > 0) {
                    String fileUrl = files.get(0).getUri().getLastPathSegment().substring(files.get(0).getUri().getLastPathSegment().lastIndexOf("/") + 1);
                    MediaDocBean mediaDocBean = new MediaDocBean();
                    mediaDocBean.setId((int) System.currentTimeMillis());
                    mediaDocBean.setFileName(fileUrl.substring(0, fileUrl.lastIndexOf(".")));
                    mediaDocBean.setDocUrl(fileUrl);
                    mediaDocBean.setDocType(AppConstant.PDF_DOC);
                    mediaDocBean.setDocMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                    uploadingDialog.startloadingDialog();
                    Utils.storeDocumentsInRTD(AppConstant.MEDIA_DOC, Utils.toJson(mediaDocBean, MediaDocBean.class));
                    UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.MEDIA_DOC + "/" + files.get(0).getUri().getLastPathSegment().substring(files.get(0).getUri().getLastPathSegment().lastIndexOf("/") + 1)).putFile(files.get(0).getUri());
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            uploadingDialog.dismissdialog();
                            Utils.showToast(activity, "Pdf File save successfully", AppConstant.succeedColor);
                        }
                    });
                }
//                    else
//                        Utils.showToast(activity,"No file selected",AppConstant.errorColor);
            } else {
                MediaDocBean mediaDocBean = new MediaDocBean();
                mediaDocBean.setId((int) System.currentTimeMillis());
                mediaDocBean.setFileName(fileUri.getLastPathSegment().substring(0, fileUri.getLastPathSegment().lastIndexOf(".")));
                mediaDocBean.setDocUrl(fileUri.getLastPathSegment());
                mediaDocBean.setDocType(AppConstant.DOC_IMAGE);
                mediaDocBean.setDocMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                uploadingDialog.startloadingDialog();
                Utils.storeDocumentsInRTD(AppConstant.MEDIA_DOC, Utils.toJson(mediaDocBean, MediaDocBean.class));
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.MEDIA_DOC + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        Utils.showToast(activity, "Image File save successfully", AppConstant.succeedColor);
                    }
                });
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(MediaDocBean mediaDocBean) {
        this.mediaDocBean = mediaDocBean;
        this.documentType = mediaDocBean.getDocType();
        Utils.showMediaChooseDialog(mediaDocBean.getDocType(), mediaDocBean.getDocUrl(), (AppCompatActivity) activity);
    }

    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;

        pd.show();
        final BottomSheetDialog dialog = Utils.getRegisteredUserList(activity);
        Button btnSend = dialog.findViewById(R.id.btnSend);
//        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
        final RecyclerView rvUser = dialog.findViewById(R.id.rvUser);
        rvUser.setLayoutManager(new LinearLayoutManager(activity));
        rvUser.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
        Utils.getUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                List<UserBean> userBeans = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserBean userBean = postSnapshot.getValue(UserBean.class);
                    if (userBean.getMobile() != null) {
                        if (!userBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                            userBeans.add(userBean);
                        }
                    }
                }

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, DocumentList.this));
                dialog.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final ProgressDialog pd = Utils.getProgressDialog(activity);
                pd.show();
                dialog.dismiss();
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://a2zcreation.000webhostapp.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();
                ApiResonse apiResonse = retrofit.create(ApiResonse.class);
                String tokens = tokenList.toString();
                tokens = tokens.replaceAll("[\\[\\](){}]", "");
                tokens = tokens.replace("\"", "");
                tokens = tokens.replaceAll(" ", "");
                Log.e("dvdfbtrghtbe", tokens + message);
                Call<NotificationBean> call = apiResonse.fileSendMsg(tokens, message);
                tokenList.clear();
                call.enqueue(new Callback<NotificationBean>() {
                    @Override
                    public void onResponse(Call<NotificationBean> call, Response<NotificationBean> response) {
                        Log.e("vdfbdbedtbher", String.valueOf(response.body().getSuccess()));
                    }

                    @Override
                    public void onFailure(Call<NotificationBean> call, Throwable t) {
                        Log.e("vdfbdbedtbher", t.getMessage());
                    }
                });
                for (FileShareBean fileShareBean : fileShareBeans) {
                    Utils.storeFileShareToRTD(fileShareBean);
                }
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
            }
        });
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId(mediaDocBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(documentType);
        fileShareBean.setPassword(password);
        fileShareBean.setMsg(msg);
        if (isChecked) {

            fileShareBeans.add(fileShareBean);
            tokenList.add(userBean.getFcmToken());
        } else {
            fileShareBeans.remove(fileShareBean);
            tokenList.remove(userBean.getFcmToken());
        }
    }
}