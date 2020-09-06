package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.CallRecorder;
import com.example.protectionapp.activites.CameraDetector;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.activites.KillNotification;
import com.example.protectionapp.utils.Utils;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private CardView cardCallRecorder, cardCameraDetector, cardFileShare, cardKillNotification;
    private ImageView ivBluelightFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_intro, container, false);
        cardCallRecorder = view.findViewById(R.id.cardCallRecorder);
        cardCameraDetector = view.findViewById(R.id.cardCameraDetector);
        cardFileShare = view.findViewById(R.id.cardFileShare);
        cardKillNotification = view.findViewById(R.id.cardKillNotification);
        ivBluelightFilter = view.findViewById(R.id.ivBluelightFilter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardCallRecorder.setOnClickListener(this);
        cardCameraDetector.setOnClickListener(this);
        cardFileShare.setOnClickListener(this);
        cardKillNotification.setOnClickListener(this);
        ivBluelightFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ivBluelightFilter) {
            Utils.setBlueLightTheme(getActivity(), ivBluelightFilter);
            return;
        }
        Intent intent = null;
        if (view == cardCallRecorder) {
            intent = new Intent(getActivity(), CallRecorder.class);

        } else if (view == cardCameraDetector) {
            intent = new Intent(getActivity(), CameraDetector.class);
        } else if (view == cardFileShare) {
            intent = new Intent(getActivity(), FileShare.class);
        } else if (view == cardKillNotification) {
            intent = new Intent(getActivity(), KillNotification.class);
        }
        startActivity(intent);
    }
}