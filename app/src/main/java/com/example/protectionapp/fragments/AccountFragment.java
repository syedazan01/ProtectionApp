package com.example.protectionapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DateIntervalInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.protectionapp.BuildConfig;
import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.Adhaar;
import com.example.protectionapp.activites.LogIn;
import com.example.protectionapp.activites.Payment_premiumUser;
import com.example.protectionapp.model.PInfo;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    UploadTask mUploadTask;
    private CardView cardSubscription,cardInvite, cardLogout, cardSos;
    String deepLink = "";
    private TextView tvMobile;
    UserBean userBean;
    ImageView ivEdit, ivProfile;
    TextView tvPremiumUser;

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
        if (PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);

        deepLink = generateContentLink().toString();
        tvMobile.setText(PrefManager.getString(AppConstant.USER_MOBILE));
        initActions();
    }

    private void initViews(View view) {
        cardInvite = view.findViewById(R.id.cardreferEarn);
        cardLogout = view.findViewById(R.id.cardLogout);
        cardSos = view.findViewById(R.id.cardSos);
        tvMobile = view.findViewById(R.id.tvMobile);
        ivEdit = view.findViewById(R.id.ivEdit);
        ivProfile = view.findViewById(R.id.ivProfile);
        cardSubscription = view.findViewById(R.id.cardSubscription);
        tvPremiumUser = view.findViewById(R.id.paymentTV);
        getUser();
    }

    private void initActions() {
        cardInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareLink();
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildSignoutDialog();
            }
        });
        cardSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(AccountFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        cardSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Payment_premiumUser.class);
                startActivity(intent);

            }
        });
        tvPremiumUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Payment_premiumUser.class);
                startActivity(intent);
            }
        });
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

    private void generateDeepLink() {
        GoogleApiClient googleApiClient = Utils.createGoogleClient(getActivity(), this);
        AppInvite.AppInviteApi.getInvitation(googleApiClient, getActivity(), true)
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

    private void shareLink() {
        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.app_name));
            String shareMessage = "\nRefer & Earn Link\n\n";
            shareMessage = shareMessage + deepLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void buildSignoutDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogFragmentTheme);
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
                getActivity().finishAffinity();
                startActivity(new Intent(getActivity(), LogIn.class));
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getUser() {
        Utils.getUserReference(getContext()).child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    userBean = dataSnapshot.getValue(UserBean.class);
                    if (userBean != null) {
                        Utils.getStorageReference()
                                .child(AppConstant.USER_DETAIL+"/"+userBean.getProfilePic())
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.e("dvsvsfbrwrb",uri.getLastPathSegment());
                                        Glide.with(getActivity()).load(uri)
                                                .error(R.drawable.login_logo)
                                                .placeholder(R.drawable.login_logo)
                                                .into(ivProfile);
                                    }
                                });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            final Uri fileUri = data.getData();
            ivProfile.setImageURI(fileUri);

            //You can get File object from intent
            final File file = ImagePicker.Companion.getFile(data);
            mUploadTask = Utils.getStorageReference().child(AppConstant.USER_DETAIL+"/"+fileUri.getLastPathSegment()).putFile(fileUri);
            mUploadTask.addOnCompleteListener(getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        UserBean userBean = new UserBean();
                        userBean.setProfilePic(fileUri.getLastPathSegment());
                        Utils.storeUserDetailsToRTD(getActivity(), userBean);
                    } else {

                    }
                }
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Utils.showToast(getActivity(), ImagePicker.Companion.getError(data), AppConstant.errorColor);
        } else {
            Utils.showToast(getActivity(), "Task Cancelled", AppConstant.errorColor);
        }
    }
}