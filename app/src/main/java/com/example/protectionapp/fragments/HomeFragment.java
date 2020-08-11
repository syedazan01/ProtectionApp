package com.example.protectionapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.IntroPagerAdapter;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.example.protectionapp.utils.views.RoundView;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;


public class HomeFragment extends Fragment {
    private LinearLayout llNext,indicators;
    TextView tvSkip,tvNext,tvTitle;
    ViewPager introPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_intro, container, false);
        llNext = view.findViewById(R.id.llNext);
        tvSkip = view.findViewById(R.id.tvSkip);
        introPager = view.findViewById(R.id.introPager);
        tvNext = view.findViewById(R.id.tvNext);
        tvTitle = view.findViewById(R.id.tvTitle);
        indicators = view.findViewById(R.id.indicators);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);

        llNext.setBackground(new RoundView(getActivity().getResources().getColor(R.color.colorPrimary), Utils.getRadius(100f)));
        IntroPagerAdapter introAdapter = new IntroPagerAdapter(getActivity());
        introPager.setAdapter(introAdapter);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(Intent(this @IntroActivity,LoginActivity:: class.java))

            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvNext.getText().toString().equals("Next")) {
                    introPager.setCurrentItem(introPager.getCurrentItem() + 1);
                } else {
//                    startActivity(Intent(this @IntroActivity,LoginActivity:: class.java))
//                    finish()
                }
            }
        });


        introPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
              /*      case 0:{
                        tvTitle.setText(getActivity().getResources().getString(R.string.intoTitle1));
                        tvNext.setText("Next");
                        tvSkip.setVisibility(View.VISIBLE);
                    }
                    case 1:{
                        tvTitle.setText(getActivity().getResources().getString(R.string.introTitle2));
                        tvNext.setText("Next");
                        tvSkip.setVisibility(View.VISIBLE);
                    }
                    case 2:{
                        tvTitle.setText(getActivity().getResources().getString(R.string.introTitle3));
                        tvNext.setText("Next");
                        tvSkip.setVisibility(View.VISIBLE);
                    }
                    case 3:{
                        tvTitle.setText(getString(R.string.introTitle4));
                        tvNext.setText("Get Started");
                        tvSkip.setVisibility(View.GONE);
                    }*/
                }

                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void setIndicator(int position) {
        for(int i=0;i< indicators.getChildCount();i++){
            View view = indicators.getChildAt(i);
            if (i==position){
                view.setBackgroundResource(R.drawable.capsule_primary);
            }else{
                view.setBackgroundResource(R.drawable.capsule_primary_lite);
            }
        }
    }
}