package atoz.protection.RecordsActivites;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import atoz.protection.model.AdhaarBean;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.NotificationBean;
import atoz.protection.model.UserBean;
import atoz.protection.network.ApiResonse;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URL;
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

public class Adhaar extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private TextInputEditText dobinput;
    public URL imgaeurl;
    TextInputLayout adhharfullname, adharnumber, adharaddres, dob;
    Button addharsavebt;
    int yearofdob, monthofdob, dayofdob;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale, radioOther;
    ImageView imageView, imageView2;
    Button opencam, sendBT;
    String msg = "", password = "";
    AdhaarBean adhaarBean=null;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(Adhaar.this);
    String Gender;
    //firebase realtime database
    FirebaseDatabase rootNode;
    //    DatabaseReference reference;
    //firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;
    Firebase reference;
    ImageView ivBack;
    TextView tvToolbarTitle;
    Activity activity = this;
    Boolean imagepick = true;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    List<String> tokenList = new ArrayList<>();

    private Uri fileUri, fileUri2;

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
            if (imagepick == true) {
                fileUri = data.getData();
                imageView.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepick = false;
            } else if (imagepick == false) {
                fileUri2 = data.getData();
                imageView2.setImageURI(fileUri2);
                //You can get File object from intent
                File file2 = ImagePicker.Companion.getFile(data);
                imagepick = true;

            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar);
        initViews();
        initActions();
        //firebase storgae
        storageReference = FirebaseStorage.getInstance().getReference();
        //progress dialog

    }

    private void initActions() {

        //open dialog for send file
        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;
                /*BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Adhaar.this, R.style.AppBottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.senddailog_bottomsheet);
                bottomSheetDialog.show();*/
                openDialog();
            }
        });


        //save data on SAVE button click firebase
        addharsavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation
                if (!validateForm())
                    return;
                String Fullname = adhharfullname.getEditText().getText().toString();
                String aadharNumber = adharnumber.getEditText().getText().toString();
                String Address = adharaddres.getEditText().getText().toString();
                String dateofbirth = dob.getEditText().getText().toString();

                String gender;
                if (radioMale.isChecked())
                    gender = "Male";
                else if (radioFemale.isChecked())
                    gender = "Female";
                else gender = "Other";

                //progress dialog
                uploadingDialog.startloadingDialog();

               adhaarBean = new AdhaarBean();
                adhaarBean.setId((int)System.currentTimeMillis());
                adhaarBean.setFullname(Fullname);
                adhaarBean.setDateofbirth(dateofbirth);
                adhaarBean.setGender(gender);
                adhaarBean.setAadharNumber(aadharNumber);
                adhaarBean.setAddress(Address);
                adhaarBean.setAdhaarimage(fileUri.getLastPathSegment());
                adhaarBean.setAdhaarimage2(fileUri2.getLastPathSegment());
                adhaarBean.setMobileNo(PrefManager.getString(AppConstant.USER_MOBILE));
                 Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                Utils.storeDocumentsInRTD(AppConstant.ADHAAR, Utils.toJson(adhaarBean, AdhaarBean.class));
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });


            }
        });


        //Request for camera permission
        if (ContextCompat.checkSelfPermission(Adhaar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Adhaar.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        //open camera to capture image
        opencam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Utils.showMediaChooseBottomSheet(Adhaar.this);

            }
        });


        final Calendar calendar = Calendar.getInstance();
        dobinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Adhaar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        dobinput.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
/*
    @Override
    public void onBackPressed() {
        //dismiss progress dialog
        progressDialog.dismiss();
    }*/

    private void openDialog() {
        SendDailog sendDailog = new SendDailog(this, true, R.style.AppBottomSheetDialogTheme);
        LayoutInflater layoutInflater = getLayoutInflater();
        View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
        sendDailog.setContentView(bootomSheetView);
        sendDailog.show();
        //sendDailog.show(getSupportFragmentManager(), "Send Dialog");
    }

    private void initViews() {
        adhharfullname = findViewById(R.id.adhhar_fullname);
        adharnumber = findViewById(R.id.adhar_number);
        adharaddres = findViewById(R.id.adhar_addres);
        addharsavebt = findViewById(R.id.addhr_savebt);
        dob = findViewById(R.id.dob);
        imageView = findViewById(R.id.adhar_imageView1);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        imageView2 = findViewById(R.id.adhar_imageView2);
        opencam = findViewById(R.id.cam_openbt);
        sendBT = findViewById(R.id.adhaar_sendBT);
        dobinput = findViewById(R.id.dob_calender);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Adhaar Form");
//        Utils.makeButton(opencam, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(addharsavebt, getResources().getColor(R.color.colorPrimary), 40F);
        opencam.setBackground(getThemeGradient(50F));
        addharsavebt.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.ADHAAR)) {
            addharsavebt.setText("Update");
            adhaarBean = (AdhaarBean) getIntent().getSerializableExtra(AppConstant.ADHAAR);
            adhharfullname.getEditText().setText(adhaarBean.getFullname());
            adharaddres.getEditText().setText(adhaarBean.getAddress());
            adharnumber.getEditText().setText(adhaarBean.getAadharNumber());
            dobinput.setText(adhaarBean.getDateofbirth());
            if (adhaarBean.getGender().equals("Male"))
                radioMale.setChecked(true);
            else if (adhaarBean.getGender().equals("Female"))
                radioFemale.setChecked(true);
            else
                radioOther.setChecked(true);
            final ProgressDialog pd = Utils.getProgressDialog(Adhaar.this);
            pd.show();

            Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + adhaarBean.getAdhaarimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(Adhaar.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView);
                    }
                }

            });
            Utils.getStorageReference().child(AppConstant.ADHAAR + "/" + adhaarBean.getAdhaarimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(Adhaar.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(imageView2);
                    }
                }

            });
        } else {
            addharsavebt.setText("Save");
            sendBT.setVisibility(View.GONE);
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, Adhaar.this));
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

    private Boolean validateForm() {
        if (TextUtils.isEmpty(adhharfullname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            adhharfullname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(adhharfullname.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            adhharfullname.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dob.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            return false;
        }
        if (adharnumber.getEditText().getText().toString().length() < 12) {
            Utils.showToast(activity, getResources().getString(R.string.adhaar_error), AppConstant.errorColor);
            adharnumber.getEditText().requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(adharaddres.getEditText().getText().toString())) {
            Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
            adharaddres.getEditText().requestFocus();
            return false;
        }
        if (fileUri == null) {
            Utils.showToast(activity, getResources().getString(R.string.adhaar_scan_error), AppConstant.errorColor);
            return false;
        }
        if (fileUri2 == null) {
            Utils.showToast(activity, getResources().getString(R.string.adhaar_scan_error), AppConstant.errorColor);
            return false;
        }
        return true;
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId((int)adhaarBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.ADHAAR);
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