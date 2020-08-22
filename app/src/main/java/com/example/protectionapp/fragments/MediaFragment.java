package com.example.protectionapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.MediaDocsAdapter;
import com.example.protectionapp.model.MediaDocBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaFragment extends Fragment implements MediaDocsAdapter.MediaDocsClickListener {
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
        rvMedia.setHasFixedSize(true);
        Utils.getPersonalDocReference(AppConstant.MEDIA_DOC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mediaDocBeanList.clear();
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
                LayoutAnimationController animation= AnimationUtils.loadLayoutAnimation(getContext(),getActivity().getResources().getIdentifier("layout_slide_down","anim",getActivity().getPackageName()));
                rvMedia.setLayoutAnimation(animation);
                rvMedia.getAdapter().notifyDataSetChanged();
                rvMedia.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void initViews(View view) {
        rvMedia = view.findViewById(R.id.rvMedia);
    }

    @Override
    public void onClick(MediaDocBean mediaDocBean) {
        Utils.showMediaChooseDialog(getActivity());
    }
}