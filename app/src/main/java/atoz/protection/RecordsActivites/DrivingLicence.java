package atoz.protection.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import atoz.protection.R;
import atoz.protection.adapters.AdapterUsers;
import atoz.protection.model.DlicenceBean;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.NotificationBean;
import atoz.protection.model.UserBean;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static atoz.protection.utils.Utils.getThemeGradient;

public class DrivingLicence extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnDLscan, btnDLsave, btnDLSend;
    private ImageView ivDL, ivDLscan, ivDlscan2;
    private TextView tvToolbarTitle;
    TextInputLayout FullName, sonOf, LicenceNumber, BloodGroup, dob, dateofissue, validity;
    TextInputEditText dobET, dateOfIssueET, ValidityET;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private Uri fileUri, fileUri2;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(DrivingLicence.this);
    private List<FileShareBean> fileShareBeans = new ArrayList<>();
    private String password, msg;
    List<String> tokenList = new ArrayList<>();
    private boolean imagepicker;
    private DlicenceBean dlicenceBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_licence);
        initViews();
        initActions();
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(DrivingLicence.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DrivingLicence.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        btnDLscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMediaChooseBottomSheet(DrivingLicence.this);
            }
        });
    }

    private void initActions() {
        //open dialog for send file
        btnDLSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        ivDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnDLsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validations
                if (TextUtils.isEmpty(FullName.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    FullName.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sonOf.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    sonOf.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(LicenceNumber.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    LicenceNumber.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(BloodGroup.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    BloodGroup.getEditText().requestFocus();
                    return;
                }
                if (fileUri == null) {
                    Utils.showToast(activity, getResources().getString(R.string.dl_scan_error), AppConstant.errorColor);
                    return;
                }
                if (fileUri2 == null) {
                    Utils.showToast(activity, getResources().getString(R.string.dl_scan_error), AppConstant.errorColor);
                    return;
                }
                // get all the values
                String FullNames = FullName.getEditText().getText().toString();
                String sonOfName = sonOf.getEditText().getText().toString();
                String licenceNumber = LicenceNumber.getEditText().getText().toString();
                String bloddgroups = BloodGroup.getEditText().getText().toString();
                String DLdob = dob.getEditText().getText().toString();
                String dateofIssue = dateofissue.getEditText().getText().toString();
                String DLvaliditys = validity.getEditText().getText().toString();

                //progress dialog
                uploadingDialog.startloadingDialog();

                dlicenceBean = new DlicenceBean();
                dlicenceBean.setId((int)System.currentTimeMillis());
                dlicenceBean.setFullname(FullNames);
                dlicenceBean.setSon_of(sonOfName);
                dlicenceBean.setLicenceNumber(licenceNumber);
                dlicenceBean.setBloodGroup(bloddgroups);
                dlicenceBean.setDateOfBirth(DLdob);
                dlicenceBean.setDateOfIssue(dateofIssue);
                dlicenceBean.setValidity(DLvaliditys);
                dlicenceBean.setMobileno(PrefManager.getString(AppConstant.USER_MOBILE));
                dlicenceBean.setDLimage(fileUri.getLastPathSegment());
                dlicenceBean.setDLimage2(fileUri2.getLastPathSegment());
               Utils.getStorageReference().child(AppConstant.DRIVING_LICENSE + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.DRIVING_LICENSE + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
                Utils.storeDocumentsInRTD(AppConstant.DRIVING_LICENSE, Utils.toJson(dlicenceBean, DlicenceBean.class));
                //progress dialog
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadingDialog.dismissdialog();

                    }
                }, 4000);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        dobET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        //date of issue
        final Calendar calendar2 = Calendar.getInstance();
        dateOfIssueET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar2.get(Calendar.YEAR);
                monthofdob = calendar2.get(Calendar.MONTH);
                dayofdob = calendar2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateOfIssueET.setText(SimpleDateFormat.getDateInstance().format(calendar2.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });
        //validity
        final Calendar calendar3 = Calendar.getInstance();
        ValidityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar3.get(Calendar.YEAR);
                monthofdob = calendar3.get(Calendar.MONTH);
                dayofdob = calendar3.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicence.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        ValidityET.setText(SimpleDateFormat.getDateInstance().format(calendar3.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();

            }
        });

    }

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //sendDailog.show(getSupportFragmentManager(),"Send Dialog");
    }

    private void initViews() {
        btnDLscan = findViewById(R.id.btdl_scan);
        btnDLsave = findViewById(R.id.DL_savebt);
        btnDLSend = findViewById(R.id.driving_sendBT);
        FullName = findViewById(R.id.drivingFullname);
        sonOf = findViewById(R.id.drivingFathersname);
        LicenceNumber = findViewById(R.id.LicenceNumber);
        BloodGroup = findViewById(R.id.bloodgroupDriving);
        dob = findViewById(R.id.Driving_dob);
        dateofissue = findViewById(R.id.Drivingdofissue);
        validity = findViewById(R.id.licence_validity);
        ivDLscan = findViewById(R.id.dl_imageview1);
        ivDlscan2 = findViewById(R.id.dl_imageview2);


        dobET = findViewById(R.id.Drivingdob_calender);
        dateOfIssueET = findViewById(R.id.dofissue_calender);
        ValidityET = findViewById(R.id.Licence_datevalidity);


        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Driving License Form");
        ivDL = findViewById(R.id.ivBack);
//        Utils.makeButton(btnDLscan, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(btnDLsave, getResources().getColor(R.color.colorPrimary), 40F);
        btnDLsave.setBackground(getThemeGradient(50F));
        btnDLscan.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.DRIVING_LICENSE)) {
            btnDLsave.setText("Update");
             dlicenceBean = (DlicenceBean) getIntent().getSerializableExtra(AppConstant.DRIVING_LICENSE);
            FullName.getEditText().setText(dlicenceBean.getFullname());
            sonOf.getEditText().setText(dlicenceBean.getSon_of());
            LicenceNumber.getEditText().setText(dlicenceBean.getLicenceNumber());
            BloodGroup.getEditText().setText(dlicenceBean.getBloodGroup());
            dob.getEditText().setText(dlicenceBean.getDateOfBirth());
            dateofissue.getEditText().setText(dlicenceBean.getDateOfBirth());
            validity.getEditText().setText(dlicenceBean.getDateOfBirth());

            final ProgressDialog pd = Utils.getProgressDialog(DrivingLicence.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.DRIVING_LICENSE + "/" + dlicenceBean.getDLimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(DrivingLicence.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivDLscan);
                    }
                }
            });
            Utils.getStorageReference().child(AppConstant.DRIVING_LICENSE + "/" + dlicenceBean.getDLimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(DrivingLicence.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivDlscan2);
                    }
                }
            });
        } else {
            btnDLsave.setText("Save");
            btnDLSend.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (!imagepicker) {
                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();
                ivDLscan.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker) {
                fileUri2 = data.getData();
                ivDlscan2.setImageURI(fileUri2);
                //You can get File object from intent
                File file2 = ImagePicker.Companion.getFile(data);
                imagepicker = false;
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;
        final ProgressDialog pd = Utils.getProgressDialog(activity);
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, DrivingLicence.this));
                dialog.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
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
                pd.dismiss();
                Utils.showToast(activity, "File Sent Successfully", AppConstant.succeedColor);
            }
        });
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId((int)dlicenceBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.DRIVING_LICENSE);
        fileShareBean.setPassword(password);
        fileShareBean.setMsg(msg);
        if (isChecked) {

            tokenList.add(userBean.getFcmToken());
            fileShareBeans.add(fileShareBean);
        } else {
            fileShareBeans.remove(fileShareBean);
            tokenList.remove(userBean.getFcmToken());
        }
    }
}
