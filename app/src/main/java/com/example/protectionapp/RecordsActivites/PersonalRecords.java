package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.DocsPagerAdapter;
import com.example.protectionapp.fragments.MediaFragment;
import com.example.protectionapp.model.MediaDocBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class PersonalRecords extends AppCompatActivity implements SendDailog.SendDialogListener {
    Activity activity=this;
    private TextView tvToolbarTitle;
    private ImageView ivBack;
    private FloatingActionButton fabDocs;
    private TabLayout tabDocs;
    private ViewPager docsViewPager;
    private UploadingDialog uploadingDialog;
    private String documentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_records);
        initViews();
        initActions();
    }
    private void initViews()
    {
//        docsViewPager=findViewById(R.id.docsViewPager);
//        tabDocs=findViewById(R.id.tabDocs);
//        fabDocs=findViewById(R.id.fabDocs);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Personal Documents");
        uploadingDialog = new UploadingDialog(this);
    }
    private void initActions() {
        docsViewPager.setAdapter(new DocsPagerAdapter(getSupportFragmentManager(),2));
        tabDocs.setupWithViewPager(docsViewPager,true);
        fabDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showDocsDialog(activity);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                if(requestCode==AppConstant.CHOOSE_PDF_REQUESTCODE)
                {
                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    if (files!=null && files.size()>0) {
                        String fileUrl=files.get(0).getUri().getLastPathSegment().substring(files.get(0).getUri().getLastPathSegment().lastIndexOf("/")+1);
                        MediaDocBean mediaDocBean=new MediaDocBean();
                        mediaDocBean.setId((int)System.currentTimeMillis());
                        mediaDocBean.setFileName(fileUrl.substring(0,fileUrl.lastIndexOf(".")));
                        mediaDocBean.setDocUrl(fileUrl);
                        mediaDocBean.setDocType(AppConstant.PDF_DOC);
                        mediaDocBean.setDocMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                        uploadingDialog.startloadingDialog();
                        Utils.storeDocumentsInRTD(AppConstant.MEDIA_DOC,Utils.toJson(mediaDocBean,MediaDocBean.class));
                        UploadTask uploadTask=Utils.getStorageReference().child(AppConstant.MEDIA_DOC+"/"+files.get(0).getUri().getLastPathSegment().substring(files.get(0).getUri().getLastPathSegment().lastIndexOf("/")+1)).putFile(files.get(0).getUri());
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                uploadingDialog.dismissdialog();
                                Utils.showToast(activity,"Pdf File save successfully",AppConstant.succeedColor);
                            }
                        });
                    }
//                    else
//                        Utils.showToast(activity,"No file selected",AppConstant.errorColor);
                }


                else
                {
                    MediaDocBean mediaDocBean=new MediaDocBean();
                    mediaDocBean.setId((int)System.currentTimeMillis());
                    mediaDocBean.setFileName(fileUri.getLastPathSegment().substring(0,fileUri.getLastPathSegment().lastIndexOf(".")));
                    mediaDocBean.setDocUrl(fileUri.getLastPathSegment());
                    mediaDocBean.setDocType(AppConstant.DOC_IMAGE);
                    mediaDocBean.setDocMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                    uploadingDialog.startloadingDialog();
                    Utils.storeDocumentsInRTD(AppConstant.MEDIA_DOC,Utils.toJson(mediaDocBean,MediaDocBean.class));
                    UploadTask uploadTask=Utils.getStorageReference().child(AppConstant.MEDIA_DOC+"/"+fileUri.getLastPathSegment()).putFile(fileUri);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            uploadingDialog.dismissdialog();
                            Utils.showToast(activity,"Image File save successfully",AppConstant.succeedColor);
                        }
                    });
                }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public void setDocumentType(String documentType)
    {
        this.documentType=documentType;
    }
    @Override
    public void applyTexts(String message, String password) {
        MediaFragment mediaFragment=(MediaFragment) ((DocsPagerAdapter)docsViewPager.getAdapter()).fragmentList.get(1);
    mediaFragment.setOnSendDialog(documentType, message, password);
    }
}