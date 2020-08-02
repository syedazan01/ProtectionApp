package com.example.protectionapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.protectionapp.BuildConfig;
import com.example.protectionapp.R;
import com.example.protectionapp.activites.LogIn;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
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
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
private CardView cardInvite,cardLogout;
String deepLink="";


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        cardInvite=view.findViewById(R.id.cardreferEarn);
        cardLogout=view.findViewById(R.id.cardLogout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);
        deepLink=generateContentLink().toString();
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
    private void generateDeepLink()
    {
        GoogleApiClient googleApiClient=Utils.createGoogleClient(getActivity(),this);
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
    private void shareLink()
    {
        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.app_name));
            String shareMessage= "\nRefer & Earn Link\n\n";
            shareMessage = shareMessage + deepLink;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void buildSignoutDialog()
    {
        final Dialog dialog=new Dialog(getActivity(),R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnOk,cancelBtn;
        btnOk=dialog.findViewById(R.id.btnOk);
        cancelBtn=dialog.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}