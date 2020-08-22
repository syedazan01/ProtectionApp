package com.example.protectionapp.RecordsActivites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.DocsPagerAdapter;
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

import java.io.File;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class PersonalRecords extends AppCompatActivity {
    Activity activity=this;
    private TextView tvToolbarTitle;
    private ImageView ivBack;
    private FloatingActionButton fabDocs;
    private TabLayout tabDocs;
    private ViewPager docsViewPager;
    private UploadingDialog uploadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
          setTheme(R.style.AppTheme_Base_Night);
        else
          setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_personal_records);
        initViews();
        initActions();
    }
    private void initViews()
    {
        docsViewPager=findViewById(R.id.docsViewPager);
        tabDocs=findViewById(R.id.tabDocs);
        fabDocs=findViewById(R.id.fabDocs);
        ivBack=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Personal Documents");
        uploadingDialog=new UploadingDialog(this);
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

                    String uriString = data.getData().toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Uri _uri = data.getData();
                        Log.d("","URI = "+ _uri);
                        if (_uri != null && "content".equals(_uri.getScheme())) {
                            Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                            cursor.moveToFirst();
                            path = cursor.getString(0);
                            cursor.close();
                        } else {
                            path = _uri.getPath();
                        }
                        myFile=new File(uriString.substring(uriString.lastIndexOf("raw:")+1));
                       /* Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(data.getData(), null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            myFile=null;
                        }finally {
                            cursor.close();
                        }*/
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    if (myFile!=null) {
                        MediaDocBean mediaDocBean=new MediaDocBean();
                        String fileName=myFile.getAbsolutePath().substring(myFile.getAbsolutePath().lastIndexOf("/")+1);
                        try {
                            Log.e("vsdxVBsfdb",fileName);
                            mediaDocBean.setFileName(fileName.substring(0,fileName.lastIndexOf(".")));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.showToast(activity,"File may be unsupported",AppConstant.errorColor);
                            return;
                        }
                        mediaDocBean.setDocUrl(fileName);
                        mediaDocBean.setDocType(AppConstant.PDF_DOC);
                        mediaDocBean.setDocMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                        uploadingDialog.startloadingDialog();
                        Utils.storeDocumentsInRTD(AppConstant.MEDIA_DOC,Utils.toJson(mediaDocBean,MediaDocBean.class));
                        UploadTask uploadTask=Utils.getStorageReference().child(AppConstant.MEDIA_DOC+"/"+fileName).putFile(Uri.fromFile(myFile));
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                uploadingDialog.dismissdialog();
                                Utils.showToast(activity,"Pdf File save successfully",AppConstant.succeedColor);
                            }
                        });
                    }
                    else
                        Utils.showToast(activity,"File may be unsupported",AppConstant.errorColor);
                }
                else
                {
                    MediaDocBean mediaDocBean=new MediaDocBean();
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
}