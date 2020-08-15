package com.example.protectionapp.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PAN extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnPANscan, btnPANsave, btnPANsend;
    Uri fileUri;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, FatherName, dob, PermanentAccountNumber;
    TextInputEditText dobET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private ImageView ivPAN, pan_imageview;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(PAN.this);
    private String msg, password;
    private List<FileShareBean> fileShareBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_a_n);
        initViews();
        initActions();
        if (ContextCompat.checkSelfPermission(PAN.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PAN.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

    }

    private void initViews() {
        btnPANscan = findViewById(R.id.panscanbt);
        btnPANsave = findViewById(R.id.pansavebt);
        btnPANsend = findViewById(R.id.pan_sendBT);
        btnPANscan = findViewById(R.id.panscanbt);
        FullName = findViewById(R.id.panFullname);
        FatherName = findViewById(R.id.panFathersname);
        pan_imageview = findViewById(R.id.pan_imageview);
        dob = findViewById(R.id.pan_dob);
        PermanentAccountNumber = findViewById(R.id.pan_number);
        dobET = findViewById(R.id.pandob_calender);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("PAN Detail Form");
        ivPAN = findViewById(R.id.ivBack);
        Utils.makeButton(btnPANscan, getResources().getColor(R.color.colorAccent), 40F);
        Utils.makeButton(btnPANsave, getResources().getColor(R.color.colorPrimary), 40F);
        if (getIntent().hasExtra(AppConstant.PAN)) {
            btnPANsave.setText("Update");
            PanBean panBean = (PanBean) getIntent().getSerializableExtra(AppConstant.PAN);
            FullName.getEditText().setText(panBean.getFullName());
            FatherName.getEditText().setText(panBean.getFatherName());
            PermanentAccountNumber.getEditText().setText(panBean.getPermanentAccountNumber());
            dobET.setText(panBean.getDateOfBirth());
            final ProgressDialog pd = Utils.getProgressDialog(PAN.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.PAN + "/" + panBean.getPanimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(PAN.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(pan_imageview);
                    }
                }
            });
        } else {
            btnPANsave.setText("Save");
            btnPANsend.setVisibility(View.GONE);
        }
    }

    private void initActions() {

        //open dialog for send file
        btnPANsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                openDialog();
            }
        });

        ivPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnPANsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (!validateForm())
                    return;
                //get all the values
                String FullNames = FullName.getEditText().getText().toString();
                String fathersname = FatherName.getEditText().getText().toString();
                String pandob = dob.getEditText().getText().toString();
                String pannumber = PermanentAccountNumber.getEditText().getText().toString();

                //progress dialog
                uploadingDialog.startloadingDialog();

                PanBean panBean = new PanBean();
                panBean.setFullName(FullNames);
                panBean.setFatherName(fathersname);
                panBean.setDateOfBirth(pandob);
                panBean.setPermanentAccountNumber(pannumber);
                panBean.setPanimage(fileUri.getLastPathSegment());
                panBean.setPanmobile(PrefManager.getString(AppConstant.USER_MOBILE));
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.PAN + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                Utils.storeDocumentsInRTD(AppConstant.PAN, Utils.toJson(panBean, PanBean.class));
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });

            }
        });


        final Calendar calendar = Calendar.getInstance();
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PAN.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        btnPANscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(PAN.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(FullName.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            FullName.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(FatherName.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            FatherName.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(PermanentAccountNumber.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            PermanentAccountNumber.getEditText().requestFocus();
            return false;
        }
        if (fileUri == null) {
            Utils.showToast(activity, "Scan PAN Card", AppConstant.errorColor);
            return false;
        }
        return true;
    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true);
        sendDailog.show(getSupportFragmentManager(), "Send Dialog");
    }

    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;
        final ProgressDialog pd = Utils.getProgressDialog(activity);
        pd.show();
        final Dialog dialog = Utils.getRegisteredUserList(activity);
        Button btnSend = dialog.findViewById(R.id.btnSend);
        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, PAN.this));
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
                for (FileShareBean fileShareBean : fileShareBeans) {
                    Utils.storeFileShareToRTD(fileShareBean);
                }
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
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
            fileUri = data.getData();
            pan_imageview.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        if (isChecked) {
            FileShareBean fileShareBean = new FileShareBean();
            fileShareBean.setSentTo(userBean.getMobile());
            fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
            fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            fileShareBean.setDocument_type(AppConstant.PAN);
            fileShareBean.setPassword(password);
            fileShareBean.setMsg(msg);
            fileShareBeans.add(fileShareBean);
        } else {
            fileShareBeans.remove(position);
        }
    }
}