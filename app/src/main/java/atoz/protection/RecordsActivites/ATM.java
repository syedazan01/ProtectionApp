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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.adapters.AdapterUsers;
import atoz.protection.model.AtmBean;
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

public class ATM extends AppCompatActivity implements SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    private Button btnAtmScan, btnAtmSave, btnatmsend;
    List<FileShareBean> fileShareBeans = new ArrayList<>();
    private TextView tvToolbarTitle;
    TextInputEditText cardvaliET;
    TextInputLayout bankname, atmnumber, nameoncard, cardVailidity, cvvcode;
    int yearofdob, monthofdob, dayofdob;
    Activity activity = this;
    private Uri fileUri, fileUri2;
    //initilizing progress dialog
    UploadingDialog uploadingDialog = new UploadingDialog(ATM.this);
    private ImageView ivATM, ivatmscan, ivatmscan2;
    private String password, msg;
    private List<String> tokenList = new ArrayList<>();
    private boolean imagepicker;
    private AtmBean atmBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_m);
        initViews();
        initActions();


        //Request for camera permission
        if (ContextCompat.checkSelfPermission(ATM.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ATM.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        btnAtmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                Utils.showMediaChooseBottomSheet(ATM.this);
            }
        });


    }

    private void initActions() {

        //open dialog for send file
        btnatmsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        ivATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAtmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validations
                if (TextUtils.isEmpty(bankname.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    bankname.getEditText().requestFocus();
                    return;
                }
                if (atmnumber.getEditText().getText().toString().length() < 16) {
                    Utils.showToast(activity, "Invalid ATM No.", AppConstant.errorColor);
                    atmnumber.getEditText().requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(nameoncard.getEditText().getText().toString())) {
                    Utils.showToast(activity, getResources().getString(R.string.empty_error), AppConstant.errorColor);
                    nameoncard.getEditText().requestFocus();
                    return;
                }
                if (fileUri == null) {
                    Utils.showToast(activity, getResources().getString(R.string.atm_scan_error), AppConstant.errorColor);
                    return;
                }
                if (fileUri2 == null) {
                    Utils.showToast(activity, getResources().getString(R.string.atm_scan_error), AppConstant.errorColor);
                    return;
                }
                if (cvvcode.getEditText().getText().toString().length()!=3) {
                    Utils.showToast(activity, "Invalid Cvv No.", AppConstant.errorColor);
                    cvvcode.getEditText().requestFocus();
                    return;
                }

                // get all the values
                String banknames = bankname.getEditText().getText().toString();
                String atmnumbers = atmnumber.getEditText().getText().toString();
                String nameoncards = nameoncard.getEditText().getText().toString();
                String cardVailiditys = cardVailidity.getEditText().getText().toString();
                String cvvcodes = cvvcode.getEditText().getText().toString();

                //progress dialog
                uploadingDialog.startloadingDialog();

                atmBean = new AtmBean();
                atmBean.setId((int)System.currentTimeMillis());
                atmBean.setBankname(banknames);
                atmBean.setAtmnumber(atmnumbers);
                atmBean.setNameoncard(nameoncards);
                atmBean.setCardVailidity(cardVailiditys);
                atmBean.setCvvcode(cvvcodes);
                atmBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                atmBean.setAtmimage(fileUri.getLastPathSegment());
                atmBean.setAtmimage2(fileUri2.getLastPathSegment());
                Utils.getStorageReference().child(AppConstant.ATM + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
                UploadTask uploadTask = Utils.getStorageReference().child(AppConstant.ATM + "/" + fileUri2.getLastPathSegment()).putFile(fileUri2);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadingDialog.dismissdialog();
                        finish();
                    }
                });
                Utils.storeDocumentsInRTD(AppConstant.ATM, Utils.toJson(atmBean, AtmBean.class));

            }
        });
        final Calendar calendar = Calendar.getInstance();
        cardvaliET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ATM.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i,i1,i2);
                        cardvaliET.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

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
        bankname = findViewById(R.id.branchET);
        atmnumber = findViewById(R.id.atmnumberET);
        nameoncard = findViewById(R.id.cardnameET);
        cardVailidity = findViewById(R.id.cardvaliTxtIL);
        cardvaliET = findViewById(R.id.cardvaliET);
        cvvcode = findViewById(R.id.cvvET);
        btnAtmScan = findViewById(R.id.btnAtmScan);
        btnAtmSave = findViewById(R.id.btnAtmSave);
        btnatmsend = findViewById(R.id.atm_sendBT);
        ivATM = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("ATM Detail Form");
        ivatmscan = findViewById(R.id.atm_imageView1);
        ivatmscan2 = findViewById(R.id.atm_imageView2);
//        Utils.makeButton(btnAtmScan, getResources().getColor(R.color.colorAccent), 40F);
//        Utils.makeButton(btnAtmSave, getResources().getColor(R.color.colorPrimary), 40F);
        btnAtmSave.setBackground(getThemeGradient(50F));
        btnAtmScan.setBackground(getThemeGradient(50F));
        if (getIntent().hasExtra(AppConstant.ATM)) {
            btnAtmSave.setText("Update");
            atmBean = (AtmBean) getIntent().getSerializableExtra(AppConstant.ATM);
            bankname.getEditText().setText(atmBean.getBankname());
            atmnumber.getEditText().setText(atmBean.getAtmnumber());
            nameoncard.getEditText().setText(atmBean.getNameoncard());
            cardVailidity.getEditText().setText(atmBean.getCardVailidity());
            cvvcode.getEditText().setText(atmBean.getCvvcode());

            final ProgressDialog pd = Utils.getProgressDialog(ATM.this);
            pd.show();
            Utils.getStorageReference().child(AppConstant.ATM + "/" + atmBean.getAtmimage()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        fileUri = task.getResult();
                        Glide.with(ATM.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivatmscan);
                    }
                }
            });
            Utils.getStorageReference().child(AppConstant.ATM + "/" + atmBean.getAtmimage2()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        fileUri2 = task.getResult();
                        Glide.with(ATM.this).load(task.getResult())
                                .error(R.drawable.login_logo)
                                .placeholder(R.drawable.login_logo)
                                .into(ivatmscan2);
                    }
                }
            });
        } else {
            btnAtmSave.setText("Save");
            btnatmsend.setVisibility(View.GONE);
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
//        rvUser.addItemDecoration(new DividerItemDecoration(activity, RecyclerView.VERTICAL));
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

                rvUser.setAdapter(new AdapterUsers(activity, userBeans, ATM.this));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to image  view
            imageView.setImageBitmap(captureImage);
        }*/
        if (resultCode == Activity.RESULT_OK) {
            if (!imagepicker) {
                //Image Uri will not be null for RESULT_OK
                fileUri = data.getData();
                ivatmscan.setImageURI(fileUri);
                //You can get File object from intent
                File file = ImagePicker.Companion.getFile(data);
                imagepicker = true;
            } else if (imagepicker) {
                fileUri2 = data.getData();
                ivatmscan2.setImageURI(fileUri2);
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
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId((int)atmBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(AppConstant.ATM);
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




