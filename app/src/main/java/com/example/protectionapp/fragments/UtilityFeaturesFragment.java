package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.protectionapp.R;
import com.example.protectionapp.services.FloatingWindowService;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;


public class UtilityFeaturesFragment extends Fragment implements FloatingWindowService.OnFabClick {

    public static FloatingWindowService.OnFabClick onFabClick = null;
    private Switch swEnableLauncher;
    private Switch ssEnableVolume;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_utility_features, container, false);
        swEnableLauncher = view.findViewById(R.id.swEnableLauncher);
        ssEnableVolume = view.findViewById(R.id.ss_Enable);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ssEnableVolume.setChecked(PrefManager.getBoolean(AppConstant.SCREENSHOT_ONVOLUME));
        swEnableLauncher.setChecked(PrefManager.getBoolean(AppConstant.OVERLAY));
        onFabClick = this;
        swEnableLauncher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Settings.canDrawOverlays(getContext())) {
                        PrefManager.putBoolean(AppConstant.OVERLAY, b);
                        getActivity().startService(new Intent(getActivity(), FloatingWindowService.class));
                    } else {
                        Utils.showToast(getActivity(), "Draw over other app permission not enable.", AppConstant.errorColor);
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().stopService(new Intent(getActivity(), FloatingWindowService.class));
                    }
                    PrefManager.putBoolean(AppConstant.OVERLAY, b);
                }
            }
        });
    }

    @Override
    public void onClose() {
        swEnableLauncher.setChecked(PrefManager.getBoolean(AppConstant.OVERLAY));
    }
}