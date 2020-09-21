package atoz.protection.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import atoz.protection.BuildConfig;
import atoz.protection.R;

import atoz.protection.activites.SettingActivity;
import atoz.protection.activites.SosActivity;
import atoz.protection.activites.SubscriptionPlan;
import atoz.protection.activites.Wallet;
import atoz.protection.adapters.AdapterSubscription;
import atoz.protection.adapters.AdapterUsers;
import atoz.protection.billing.GooglePaySetup;
import atoz.protection.model.FetchNotification;
import atoz.protection.model.PlansBean;
import atoz.protection.model.UserBean;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, AdapterUsers.RecyclerViewListener, AdapterSubscription.RecyclerViewClickListener {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 300;
    private static final String DEEP_LINK_URL = "https://a2zprotection.page.link/Go1D";
    GooglePaySetup googlePaySetup;
    UploadTask mUploadTask;
    List<FetchNotification> fetchNotifications = new ArrayList<>();
    private RelativeLayout rltSubscription, rltInvite, rltLogout, rltSos, rltWallet, rltSetting;
    String deepLink = "";
    private TextView tvMobile;
    UserBean userBean;
    ImageView ivEdit, ivProfile;
    Activity mActivity;
    boolean isImageUpload;
    List<String> tokenList = new ArrayList<>();
    Dialog msgDialog;
    EditText etMsg;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if (PrefManager.getBoolean(ISBLUELIGHT))
            mActivity.setTheme(R.style.AppTheme_Base_Night);
        else*/
            mActivity.setTheme(R.style.AppTheme_Base_Light);
        googlePaySetup = new GooglePaySetup(getContext());
        try {
            Task<Boolean> task = googlePaySetup.readyToPay();
            task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful()) {
//Utils.showToast(getActivity(),"Success",AppConstant.succeedColor);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        deepLink = generateContentLink().toString();
        tvMobile.setText(PrefManager.getString(AppConstant.USER_MOBILE));
        msgDialog = Utils.getMsgDialog(mActivity);
        etMsg = msgDialog.findViewById(R.id.etMsg);
        initActions();
    }

    private void initViews(View view) {
        rltInvite = view.findViewById(R.id.rltreferEarn);
        rltLogout = view.findViewById(R.id.rltLogout);
        rltSos = view.findViewById(R.id.rltSos);
        tvMobile = view.findViewById(R.id.tvMobile);
        ivEdit = view.findViewById(R.id.ivEdit);
        ivProfile = view.findViewById(R.id.ivProfile);
        rltSubscription = view.findViewById(R.id.rltSubscription);
        rltWallet = view.findViewById(R.id.rltWallet);
        rltSetting = view.findViewById(R.id.rltSetting);
        getUser();
    }

    private void initActions() {
        rltSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(), SettingActivity.class);
                startActivity(i);

            }
        });


        rltWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                    Utils.showNoSubsDialog(getContext());
                    return;
                }
                else{
                    Intent intent = new Intent(getActivity(), Wallet.class);
                    startActivity(intent);
                }*/
                Intent intent = new Intent(getActivity(), Wallet.class);
                startActivity(intent);
            }
        });
        rltInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(!PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                    Utils.showNoSubsDialog(getContext());
                    return;
                }*/
                final Uri deepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL + "/?" + AppConstant.INVITED_BY + "=" + PrefManager.getString(AppConstant.USER_MOBILE)), 0);
                shareDeepLink(deepLink.toString());
            }
        });
        rltLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildSignoutDialog();
            }
        });
        rltSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(!PrefManager.getBoolean(AppConstant.IS_SUBSCRIBE)) {
                    Utils.showNoSubsDialog(getContext());
                return;
                }*/
                startActivity(new Intent(mActivity, SosActivity.class));
               /* msgDialog.show();
                Button btnProceed = msgDialog.findViewById(R.id.btnProceed);
                Utils.makeButton(btnProceed, getResources().getColor(R.color.colorAccent), 40F);
                btnProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(etMsg.getText())) {
                            Utils.showToast(mActivity, "Type Message", AppConstant.errorColor);
                        } else {
                            msgDialog.dismiss();

                            final ProgressDialog pd = Utils.getProgressDialog(mActivity);
                            pd.show();
                            final Dialog dialog = Utils.getRegisteredUserList(mActivity);
                            Button btnSend = dialog.findViewById(R.id.btnSend);
                            Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
                            final RecyclerView rvUser = dialog.findViewById(R.id.rvUser);
                            rvUser.setLayoutManager(new LinearLayoutManager(mActivity));
                            rvUser.addItemDecoration(new DividerItemDecoration(mActivity, RecyclerView.VERTICAL));
                            Utils.getUserReference().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (isImageUpload) {
                                        isImageUpload = false;
                                        return;
                                    }
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

                                    rvUser.setAdapter(new AdapterUsers(mActivity, userBeans, AccountFragment.this));
                                    dialog.show();
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                            btnSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    for (FetchNotification fetchNotification : fetchNotifications) {
                                        Utils.storeNotificationInRTD(fetchNotification);
                                    }
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
                            });
                        }
                    }
                });*/


            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMediaChooseBottomSheet(getActivity());
            }
        });
        rltSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, SubscriptionPlan.class);
                startActivity(intent);
//                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {
        ProgressDialog pd = Utils.getProgressDialog(getActivity());
        pd.show();
        final List<PlansBean> plansBeans = new ArrayList<>();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.subscribtion_dialog);
        RecyclerView rvSubscribe = bottomSheetDialog.findViewById(R.id.rvSubscription);
        rvSubscribe.setLayoutManager(new LinearLayoutManager(getActivity()));
        Utils.getPlansReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                for (DataSnapshot postShot : dataSnapshot.getChildren()) {
                    PlansBean plansBean = postShot.getValue(PlansBean.class);
                    plansBeans.add(plansBean);
                }
                rvSubscribe.setAdapter(new AdapterSubscription(getActivity(), plansBeans, AccountFragment.this));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public static Uri generateContentLink() {
        Uri baseUrl = Uri.parse("https://www.protectionapp.com/?currentPage=1");
        String domain = "https://protectionapp.page.link";

        DynamicLink link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID).build())
                .buildDynamicLink();

        return link.getUri();
    }

    public Uri buildDeepLink(@NonNull Uri deepLink, int minVersion) {
        String uriPrefix = "https://a2zprotection.page.link";
        String lnk = mActivity.getPackageName();

        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(Uri.parse(lnk))
                .setDomainUriPrefix(uriPrefix)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setMinimumVersion(minVersion)
                        .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + lnk))
                        .build())
                .setLink(deepLink);

        DynamicLink link = builder.buildDynamicLink();
        return link.getUri();
    }

    private void generateDeepLink() {
        GoogleApiClient googleApiClient = Utils.createGoogleClient((FragmentActivity) mActivity, this);
        AppInvite.AppInviteApi.getInvitation(googleApiClient, mActivity, true)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    Intent intent = result.getInvitationIntent();
                                    deepLink = AppInviteReferral.getDeepLink(intent);
                                    // Handloe the deep link
                                } else {
                                    Log.e("Account", "Oops, looks like there was no deep link found!");
                                }
                            }
                        });

    }

    private void shareDeepLink(String deepLink) {
        //Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + "ic_refer");
/*        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_refer);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Clapio/invite.jpeg";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


//        Uri bmpUri = Uri.parse("android.resource://" + mActivity.getPackageName() + "/drawable/" + "ic_refer.jpeg");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        intent.putExtra(Intent.EXTRA_TEXT, deepLink + "\n\nJoin me on ProtectionApp & Earn Cash, an Amazing Utility App.!");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
        Log.e("DEEPLINK>>>",deepLink);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void buildSignoutDialog() {
        Utils.showSignOutDialog(getActivity());
        /*final Dialog dialog = new Dialog(mActivity, R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnOk, cancelBtn;
        btnOk = dialog.findViewById(R.id.btnOk);
        cancelBtn = dialog.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                PrefManager.putBoolean(AppConstant.ISLOGGEDIN, false);
                mActivity.finishAffinity();
                startActivity(new Intent(mActivity, LogIn.class));
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }

    private void getUser() {
        final ProgressDialog pd = Utils.getProgressDialog(mActivity);
        pd.show();
        Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    userBean = dataSnapshot.getValue(UserBean.class);
                    if (userBean != null) {
                        Utils.getStorageReference()
                                .child(AppConstant.USER_DETAIL + "/" + userBean.getProfilePic())
                                .getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            if (mActivity != null) {
                                                Glide.with(mActivity).load(task.getResult())
                                                        .error(R.drawable.login_logo)
                                                        .placeholder(R.drawable.login_logo)
                                                        .into(ivProfile);
                                            }
                                        }
                                        pd.dismiss();
                                    }
                                });
                    }
                    else
                    {
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showToast(mActivity, resultCode + "", AppConstant.errorColor);
        if (resultCode == Activity.RESULT_OK && requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            PaymentData paymentData = PaymentData.getFromIntent(data);
//                String json = data.toJSon()
            try {
                JSONObject paymentMethodData = new JSONObject(paymentData.toJson())
                        .getJSONObject("paymentMethodData");
                String paymentToken = paymentMethodData
                        .getJSONObject("tokenizationData")
                        .getString("token");
                Log.e("payment??>>", paymentToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        } else if (resultCode == 1 && requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            Log.e("dfvbdfbdfbn", status.getStatusMessage());
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            final Uri fileUri = data.getData();
            ivProfile.setImageURI(fileUri);

            //You can get File object from intent
            final File file = ImagePicker.Companion.getFile(data);
            mUploadTask = Utils.getStorageReference().child(AppConstant.USER_DETAIL + "/" + fileUri.getLastPathSegment()).putFile(fileUri);
            final ProgressDialog pd = Utils.getProgressDialog(mActivity);
            pd.show();
            mUploadTask.addOnCompleteListener(mActivity, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    pd.dismiss();
                    isImageUpload = true;
                    if (task.isSuccessful()) {
                        UserBean userBean = new UserBean();
                        userBean.setMobile(PrefManager.getString(AppConstant.USER_MOBILE));
                        userBean.setProfilePic(fileUri.getLastPathSegment());
                        userBean.setFcmToken(PrefManager.getString(AppConstant.FCMTOKEN));
                        Utils.storeUserDetailsToRTD(userBean);
                    } else {

                    }
                }
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Utils.showToast(mActivity, ImagePicker.Companion.getError(data), AppConstant.errorColor);
        } else {
            Utils.showToast(mActivity, "Task Cancelled", AppConstant.errorColor);
        }

    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FetchNotification fetchNotification = new FetchNotification();
        fetchNotification.setMsg(etMsg.getText().toString());
        fetchNotification.setTo_mobile(PrefManager.getString(AppConstant.USER_MOBILE));
        fetchNotification.setFrom_mobile(userBean.getMobile());
        fetchNotification.setProfile_Pic(userBean.getProfilePic());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        fetchNotification.setCreated_date(dateFormat.format(new Date()));
        if (isChecked) {
            tokenList.add(userBean.getFcmToken());
            fetchNotifications.add(fetchNotification);
        } else {
            fetchNotifications.remove(fetchNotification);
            tokenList.remove(userBean.getFcmToken());
        }

    }

    @Override
    public void onSelectPlan(PlansBean plansBean) {
        try {
            final JSONObject paymentRequestJson = googlePaySetup.baseConfigurationJson();
            paymentRequestJson.put("transactionInfo", new JSONObject()
                    .put("totalPrice", plansBean.getPlanPrice().replace("\u20B9", ""))
                    .put("totalPriceStatus", "FINAL")
                    .put("currencyCode", "INR")
            );
            paymentRequestJson.put("merchantInfo", new JSONObject()
                    .put("merchantId", "01234567890123456789")
                    .put("merchantName", "Example Merchant")

            );
            final PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequestJson.toString());
            AutoResolveHelper.resolveTask(googlePaySetup.paymentsClient.loadPaymentData(request), getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}