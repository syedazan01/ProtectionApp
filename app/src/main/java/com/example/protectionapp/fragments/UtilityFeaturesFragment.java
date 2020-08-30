package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.HomePage;
import com.example.protectionapp.activites.KillNotification;
import com.example.protectionapp.services.FloatingWindowService;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.MIUIUtils;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UtilityFeaturesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UtilityFeaturesFragment extends Fragment implements FloatingWindowService.OnFabClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static FloatingWindowService.OnFabClick onFabClick = null;
    private Switch swEnableLauncher, ssEnableVolume;
    private RelativeLayout rltKillNotification;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UtilityFeaturesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UtilityFeaturesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UtilityFeaturesFragment newInstance(String param1, String param2) {
        UtilityFeaturesFragment fragment = new UtilityFeaturesFragment();
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
        View view = inflater.inflate(R.layout.fragment_utility_features, container, false);
        swEnableLauncher = view.findViewById(R.id.swEnableLauncher);
        ssEnableVolume = view.findViewById(R.id.ss_Enable);
        rltKillNotification = view.findViewById(R.id.rltKillNotification);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swEnableLauncher.setChecked(PrefManager.getBoolean(AppConstant.OVERLAY));
        onFabClick = this;
        rltKillNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KillNotification.class));
            }
        });
        ssEnableVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PrefManager.putBoolean(AppConstant.SCREENSHOT_ONVOLUME,b);
            }
        });
        swEnableLauncher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Build.VERSION.SDK_INT >= 19 && MIUIUtils.isMIUI() && !MIUIUtils.isFloatWindowOptionAllowed(getContext())) {
                        Utils.showToast(getActivity(), "Draw over other app permission not enable.", AppConstant.errorColor);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                        Utils.showToast(getActivity(), "Draw over other app permission not enable.", AppConstant.errorColor);

                    } else {
                        PrefManager.putBoolean(AppConstant.OVERLAY, b);
                        if(Utils.isMyFloatingServiceRunning(getActivity()))
                            getActivity().stopService(new Intent(getActivity(),FloatingWindowService.class));
                        getActivity().startService(new Intent(getActivity(), FloatingWindowService.class));
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