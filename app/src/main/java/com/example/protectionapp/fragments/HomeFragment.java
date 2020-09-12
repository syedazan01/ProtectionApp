package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.balram.locker.utils.Locker;
import com.example.protectionapp.R;
import com.example.protectionapp.activites.CallRecorder;
import com.example.protectionapp.activites.CameraDetector;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.activites.KillNotification;
import com.example.protectionapp.activites.SpamActivity;
import com.example.protectionapp.adapters.ServiceAdapter;
import com.example.protectionapp.model.ServiceBean;
import com.example.protectionapp.services.FloatingWindowService;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.MIUIUtils;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment implements View.OnClickListener, ServiceAdapter.RecyclerViewOnClick, CompoundButton.OnCheckedChangeListener {
    //    private CardView cardLauncherWidget;
    private Switch swEnableLauncher;
    private ImageView ivBluelightFilter;
    private ServiceAdapter serviceAdapter;
    private RecyclerView rvService;
    private CardView cardAppLock;
    private List<ServiceBean> serviceBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_intro, container, false);
        /*cardLauncherWidget = view.findViewById(R.id.cardLauncherWidget);*/
       /* cardCameraDetector = view.findViewById(R.id.cardCameraDetector);
        cardFileShare = view.findViewById(R.id.cardFileShare);
        cardKillNotification = view.findViewById(R.id.cardKillNotification);*/
        cardAppLock = view.findViewById(R.id.cardAppLock);
        cardAppLock.setVisibility(View.GONE);
        swEnableLauncher = view.findViewById(R.id.swEnableLauncher);
        ivBluelightFilter = view.findViewById(R.id.ivBluelightFilter);
        rvService = view.findViewById(R.id.rvService);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swEnableLauncher.setChecked(PrefManager.getBoolean(AppConstant.OVERLAY));
        initData();
        setupRv();
       /* cardCallRecorder.setOnClickListener(this);
        cardCameraDetector.setOnClickListener(this);
        cardFileShare.setOnClickListener(this);
        cardKillNotification.setOnClickListener(this);*/
        swEnableLauncher.setOnCheckedChangeListener(this);
        ivBluelightFilter.setOnClickListener(this);
        cardAppLock.setOnClickListener(this);
    }

    private void initData() {
        serviceBeanList.add(new ServiceBean("Call \nrecorder", R.drawable.ic_call_recorder));
        serviceBeanList.add(new ServiceBean("Camera \nDetector", R.drawable.cameradetector_logo));
        serviceBeanList.add(new ServiceBean("File \nShare", R.drawable.filesshare));
        serviceBeanList.add(new ServiceBean("Kill \nNotification", R.drawable.ic_bell_icon));
        serviceBeanList.add(new ServiceBean("App Lock", R.drawable.ic_applock));
        serviceBeanList.add(new ServiceBean("Spam Call", R.drawable.spam));
    }

    private void setupRv() {
        serviceAdapter = new ServiceAdapter(serviceBeanList);
        serviceAdapter.setRecyclerViewOnClick(this);
        rvService.setAdapter(serviceAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == ivBluelightFilter) {
            Utils.setBlueLightTheme(getActivity(), ivBluelightFilter);
//            return;
        }
        else if(view==cardAppLock)
        {
            Utils.showAppLockDialog(getActivity());
        }
        /*Intent intent = null;
        if (view == cardCallRecorder) {
            intent = new Intent(getActivity(), CallRecorder.class);

        } else if (view == cardCameraDetector) {
            intent = new Intent(getActivity(), CameraDetector.class);
        } else if (view == cardFileShare) {
            intent = new Intent(getActivity(), FileShare.class);
        } else if (view == cardKillNotification) {
            intent = new Intent(getActivity(), KillNotification.class);
        }
        startActivity(intent);*/
    }

    @Override
    public void onServiceClick(int position) {
        Intent intent = null;
        if (position == 0) {
            intent = new Intent(getActivity(), CallRecorder.class);
        } else if (position == 1) {
            intent = new Intent(getActivity(), CameraDetector.class);
        } else if (position == 2) {
            intent = new Intent(getActivity(), FileShare.class);
        } else if (position == 3) {
            intent = new Intent(getActivity(), KillNotification.class);
        }
        else if(position==4)
        {
            Utils.showAppLockDialog(getActivity());
            return;
        }
        else if(position==5)
        {
            intent=new Intent(getActivity(), SpamActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Locker.DISABLE_PASSLOCK:
                return;
            case Locker.ENABLE_PASSLOCK:
            case Locker.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity(), getString(R.string.setup_passcode),
                            Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (Build.VERSION.SDK_INT >= 19 && MIUIUtils.isMIUI() && !MIUIUtils.isFloatWindowOptionAllowed(getContext())) {
                Utils.showToast(getActivity(), "Draw over other app permission not enable.", AppConstant.errorColor);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                Utils.showToast(getActivity(), "Draw over other app permission not enable.", AppConstant.errorColor);

            } else {
                if (Utils.isMyFloatingServiceRunning(getActivity()))
                    getActivity().stopService(new Intent(getActivity(), FloatingWindowService.class));
                getActivity().startService(new Intent(getActivity(), FloatingWindowService.class).setAction(FloatingWindowService.LAUNCHER_WIDGET));
            }
        } else {
            if (getActivity() != null) {
                getActivity().stopService(new Intent(getActivity(), FloatingWindowService.class));
            }

        }
        PrefManager.putBoolean(AppConstant.OVERLAY, b);
    }
}