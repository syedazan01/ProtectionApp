package com.example.protectionapp.activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.model.FetchNotification;
import com.example.protectionapp.model.NotificationBean;
import com.example.protectionapp.model.SosBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.network.ApiResonse;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SosActivity extends AppCompatActivity implements SosAdapter.RecyclerViewHandleOnCheck {
    private ImageView ivBack;
    private TextView tvToolbarText;
    private RecyclerView rvSos;
    private SosAdapter sosAdapter;
    ProgressDialog pd;
    SosActivity sosActivity = this;
    int savedPositionRange = -1;
    private List<SosBean> sosBeanList = new ArrayList<>();
    private List<String> sosNumbers = new ArrayList<>();
    FloatingActionButton fabSend;
    private Dialog msgDialog;
    private List<FetchNotification> fetchNotifications = new ArrayList<>();
    private List<String> tokenList = new ArrayList<>();
    private EditText etMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        initViews();
        initActions();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvToolbarText = findViewById(R.id.tvToolbarTitle);
        tvToolbarText.setText("Sos Alert");
        rvSos = findViewById(R.id.rvSos);
        fabSend = findViewById(R.id.fabSend);
        pd = Utils.getProgressDialog(sosActivity);
        msgDialog = Utils.getMsgDialog(this);
        etMsg = msgDialog.findViewById(R.id.etMsg);
    }

    private void initActions() {
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkFlag = false;
                for (int i = 0; i < sosBeanList.size(); i++) {
                    if (sosBeanList.get(i).isChecked())
                        checkFlag = true;
                }
                if (!checkFlag)
                    Utils.showToast(sosActivity, "Please choose any contact to send", AppConstant.errorColor);
                else {
                    msgDialog.show();
                    Button btnProceed = msgDialog.findViewById(R.id.btnProceed);
                    Utils.makeButton(btnProceed, getResources().getColor(R.color.colorAccent), 40F);
                    btnProceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(etMsg.getText())) {
                                Utils.showToast(sosActivity, "Type Message", AppConstant.errorColor);
                            } else {
                                msgDialog.dismiss();


                                        for (FetchNotification fetchNotification : fetchNotifications) {
                                            Utils.storeNotificationInRTD(fetchNotification);
                                        }
                                msgDialog.dismiss();
                                        Gson gson = new GsonBuilder()
                                                .setLenient()
                                                .create();
                                        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://a2zcreation.000webhostapp.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();
                                        ApiResonse apiResonse = retrofit.create(ApiResonse.class);
                                        String tokens = tokenList.toString();
                                        tokens = tokens.replaceAll("[\\[\\](){}]", "");
                                        tokens = tokens.replace("\"", "");
                                        tokens = tokens.replaceAll(" ", "");
                                        Log.e("dvdfbtrghtbe", tokens + etMsg.getText().toString());
                                        Call<NotificationBean> call = apiResonse.sendMsg(tokens, etMsg.getText().toString());
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
                            }
                        }
                    });
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rvSos.setLayoutManager(new LinearLayoutManager(sosActivity));
        pd.show();
        Utils.getSosReference().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sosBeanList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SosBean sosBean = snapshot.getValue(SosBean.class);
                    if (sosBean.getOwnerMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                        sosBeanList.add(sosBean);
                        sosNumbers.add(sosBean.getMobile());
                    }
                    if (sosBeanList.size() > 0) {
                        savedPositionRange = sosBeanList.size();
                    }
                }
                Utils.getUserReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (savedPositionRange != -1) {
                            List<SosBean> tempList = new ArrayList<>(sosBeanList);
                            for (int i = savedPositionRange; i < tempList.size(); i++) {
                                sosBeanList.remove(i);
                            }
                        }
                        for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                            UserBean userBean = postShot.getValue(UserBean.class);
                            if (!userBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {

                                SosBean sosBean2 = new SosBean();
                                if (sosBeanList.size() == 0 || sosNumbers.indexOf(userBean.getMobile()) == -1) {

                                    sosBean2.setMobile(userBean.getMobile());
                                    sosBean2.setProfilePic(userBean.getProfilePic());
                                    sosBean2.setFcmToken(userBean.getFcmToken());
                                    sosBeanList.add(sosBean2);
                                }
                            }
                        }
                        pd.dismiss();
                        rvSos.setAdapter(new SosAdapter(sosActivity, sosBeanList, sosActivity));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onCheckedChanged(SosBean sosBean, boolean isChecked) {
        FetchNotification fetchNotification = new FetchNotification();
        fetchNotification.setMsg(etMsg.getText().toString());
        fetchNotification.setTo_mobile(PrefManager.getString(AppConstant.USER_MOBILE));
        fetchNotification.setFrom_mobile(sosBean.getMobile());
        fetchNotification.setProfile_Pic(sosBean.getProfilePic());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        fetchNotification.setCreated_date(dateFormat.format(new Date()));
        if (isChecked) {

        } else {

        }
        if (isChecked) {
            showSaveNameDialog(sosBean);
            tokenList.add(sosBean.getFcmToken());
            fetchNotifications.add(fetchNotification);
        } else {
            fetchNotifications.remove(fetchNotification);
            tokenList.remove(sosBean.getFcmToken());
            Utils.removeSosNumbersInRTD(sosBean.getPushKey());
        }
    }

    private void showSaveNameDialog(SosBean sosBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(sosActivity);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_save_number_dailog, null);
        EditText etName = view.findViewById(R.id.etName);
        builder.setView(view)
                .setTitle("Assign a Password and Text message to secure file")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                            Utils.showToast(sosActivity, "Enter Any Name", AppConstant.succeedColor);
                        } else {
                            sosBean.setChecked(true);
                            sosBean.setName(etName.getText().toString().trim());
                            sosBean.setOwnerMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                            Utils.storeSosNumbersInRTD(sosBean);
                        }
                        Utils.hideKeyboardFrom(sosActivity, view);
                    }
                });
        builder.create().show();
    }
}