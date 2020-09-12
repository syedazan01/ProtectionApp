package com.example.protectionapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.Adhaar;
import com.example.protectionapp.RecordsActivites.SendDailog;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.adapters.MediaDocsAdapter;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.MediaDocBean;
import com.example.protectionapp.model.NotificationBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.network.ApiResonse;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaFragment extends Fragment implements MediaDocsAdapter.MediaDocsClickListener, SendDailog.SendDialogListener, AdapterUsers.RecyclerViewListener {
    ProgressDialog pd;
    private RecyclerView rvMedia;
    private MediaDocsAdapter mediaDocsAdapter;
    private List<MediaDocBean> mediaDocBeanList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String msg,password;
    private List<FileShareBean> fileShareBeans=new ArrayList<>();
    private List<String> tokenList=new ArrayList<>();
    private String documentType;
    private MediaDocBean mediaDocBean;

    public MediaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaFragment newInstance(String param1, String param2) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        LayoutAnimationController animation= AnimationUtils.loadLayoutAnimation(getContext(),getContext().getResources().getIdentifier("layout_slide_down","anim",getActivity().getPackageName()));
        rvMedia.setLayoutAnimation(animation);
        rvMedia.setHasFixedSize(true);
        pd=Utils.getProgressDialog(getActivity());
        pd.show();
        Utils.getPersonalDocReference(AppConstant.MEDIA_DOC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mediaDocBeanList.clear();
                pd.dismiss();
                for (DataSnapshot postSnap : dataSnapshot.getChildren())
                {

                    MediaDocBean mediaDocBean=postSnap.getValue(MediaDocBean.class);

                    if (mediaDocBean.getDocMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                        mediaDocBeanList.add(mediaDocBean);
                        Log.e("sffdsvbfaes",mediaDocBeanList.get(0).getFileName());
                    }
                }
                mediaDocsAdapter = new MediaDocsAdapter(getActivity(), mediaDocBeanList, MediaFragment.this);
              rvMedia.setAdapter(mediaDocsAdapter);

                rvMedia.getAdapter().notifyDataSetChanged();
                rvMedia.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });

    }

    private void initViews(View view) {
        rvMedia = view.findViewById(R.id.rvMedia);
    }

    @Override
    public void onClick(MediaDocBean mediaDocBean) {
        this.mediaDocBean=mediaDocBean;
        Utils.showMediaChooseDialog(mediaDocBean.getDocType(),mediaDocBean.getDocUrl(),(AppCompatActivity) getActivity());
    }
public void setOnSendDialog(String documentType, String message, String password)
{
    this.documentType=documentType;
    applyTexts(message,password);
}
    @Override
    public void applyTexts(String message, String password) {
        this.msg = message;
        this.password = password;

        pd.show();
        final BottomSheetDialog dialog = Utils.getRegisteredUserList(getActivity());
        Button btnSend = dialog.findViewById(R.id.btnSend);
//        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
        final RecyclerView rvUser = dialog.findViewById(R.id.rvUser);
        rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUser.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
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

                rvUser.setAdapter(new AdapterUsers(getActivity(), userBeans, MediaFragment.this));
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
                final ProgressDialog pd = Utils.getProgressDialog(getActivity());
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
                Utils.showToast(getActivity(), "File Sent Successfully", AppConstant.succeedColor);
            }
        });
    }

    @Override
    public void onCheck(int position, UserBean userBean, boolean isChecked) {
        FileShareBean fileShareBean = new FileShareBean();
        fileShareBean.setId(mediaDocBean.getId());
        fileShareBean.setSentTo(userBean.getMobile());
        fileShareBean.setSentFrom(PrefManager.getString(AppConstant.USER_MOBILE));
        fileShareBean.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        fileShareBean.setDocument_type(documentType);
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