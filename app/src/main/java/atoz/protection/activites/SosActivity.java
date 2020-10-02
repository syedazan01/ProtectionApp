package atoz.protection.activites;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.model.FetchNotification;
import atoz.protection.model.NotificationBean;
import atoz.protection.model.SosBean;
import atoz.protection.model.UserBean;
import atoz.protection.network.ApiResonse;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static atoz.protection.utils.Utils.getThemeGradient;

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
    SearchView searchContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        initViews();
        initActions();
    }

    private void initViews() {
        searchContacts = findViewById(R.id.searchContacts);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarText = findViewById(R.id.tvToolbarTitle);
        tvToolbarText.setText("Sos Alert");
        rvSos = findViewById(R.id.rvSos);
        fabSend = findViewById(R.id.fabSend);
        pd = Utils.getProgressDialog(sosActivity);
        msgDialog = Utils.getMsgDialog(this);
        etMsg = msgDialog.findViewById(R.id.etMsg);
        sosAdapter=new SosAdapter(sosActivity, sosBeanList, this);
    }

    private void initActions() {
        searchContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchContacts.setIconifiedByDefault(true);
                searchContacts.setFocusable(true);
                searchContacts.setIconified(false);
                searchContacts.requestFocusFromTouch();
            }
        });
        searchContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String query=s.toLowerCase(Locale.getDefault());
                if(TextUtils.isEmpty(s))
                {
                    sosAdapter.updateList(sosBeanList);
                    return false;
                }
                List<SosBean> sosBeans=new ArrayList<>();
                for(int i=0;i<sosBeanList.size();i++)
                {
                    SosBean sosBean=sosBeanList.get(i);
                    String name=null;
                    if(sosBean.getName() != null)
                    {
                        name=sosBean.getName().toLowerCase(Locale.getDefault());
                    }
                    Log.e("SOS>>>",sosBean.getMobile()+" >>"+sosBean.getName()+" >>"+query);
                    if((sosBean.getMobile() != null && sosBean.getMobile().contains(query)) || (name != null && name.contains(query)))
                    {
                        sosBeans.add(sosBean);
                    }
                }
                sosAdapter.updateList(sosBeans);
                return false;
            }
        });
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
//                    Utils.makeButton(btnProceed, getResources().getColor(R.color.colorAccent), 40F);
                    btnProceed.setBackground(getThemeGradient(50F));
                    btnProceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(etMsg.getText())) {
                                Utils.showToast(sosActivity, "Type Message", AppConstant.errorColor);
                            } else {
                                msgDialog.dismiss();


                                        for (FetchNotification fetchNotification : fetchNotifications) {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            fetchNotification.setCreated_date(dateFormat.format(new Date()));
                                            fetchNotification.setMsg(etMsg.getText().toString());
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
                                                Toast.makeText(SosActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                              finish();
                                                Log.e("vdfbdbedtbher", String.valueOf(response.body().getSuccess()));
                                            }

                                            @Override
                                            public void onFailure(Call<NotificationBean> call, Throwable t) {
                                                finish();
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
                        if(sosBean.isChecked())
                        {
                            FetchNotification fetchNotification = new FetchNotification();
                            fetchNotification.setTo_mobile(PrefManager.getString(AppConstant.USER_MOBILE));
                            fetchNotification.setFrom_mobile(sosBean.getMobile());
                            fetchNotification.setProfile_Pic(sosBean.getProfilePic());
                            fetchNotifications.add(fetchNotification);
                        }
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
                            if (userBean.getMobile()!=null) {
                                if (!userBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {

                                    SosBean sosBean2 = new SosBean();
                                    if (sosBeanList.size() == 0 || sosNumbers.indexOf(userBean.getMobile()) == -1) {

                                        sosBean2.setMobile(userBean.getMobile());
                                        sosBean2.setProfilePic(userBean.getProfilePic());
                                        sosBean2.setFcmToken(userBean.getFcmToken());
                                        if(sosBean2.isChecked())
                                        {
                                            FetchNotification fetchNotification = new FetchNotification();
                                            fetchNotification.setTo_mobile(PrefManager.getString(AppConstant.USER_MOBILE));
                                            fetchNotification.setFrom_mobile(sosBean2.getMobile());
                                            fetchNotification.setProfile_Pic(sosBean2.getProfilePic());
                                            fetchNotifications.add(fetchNotification);
                                        }
                                        sosBeanList.add(sosBean2);
                                    }
                                }
                            }
                        }
                        pd.dismiss();

                        rvSos.setAdapter(sosAdapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        pd.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
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
    @Override
    public void onBackPressed() {
        if (!searchContacts.isIconified()) {
            searchContacts.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}