package com.example.protectionapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;


public class HomeFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.transparentStatusBar(this@Home)
        setContentView(R.layout.activity_intro)
        llNext.background= RoundView(resources.getColor(R.color.colorPrimary), Utils.getRadius(100f))
        val introAdapter = IntroPagerAdapter(this@IntroActivity)
        introPager.adapter = introAdapter

        tvSkip.setOnClickListener {
            startActivity(Intent(this@IntroActivity,LoginActivity::class.java))
            finish()
        }

        tvNext.setOnClickListener {
            if (tvNext.text=="Next"){
                introPager.currentItem = introPager.currentItem+1
            }else {
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }


        introPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        tvTitle.text=getString(R.string.intoTitle1)
                        tvNext.text = "Next"
                        tvSkip.visibility= View.VISIBLE
                    }
                    1 -> {
                        tvTitle.text=getString(R.string.introTitle2)
                        tvNext.text = "Next"
                        tvSkip.visibility= View.VISIBLE
                    }
                    2 -> {
                        tvTitle.text=getString(R.string.introTitle3)
                        tvNext.text = "Next"
                        tvSkip.visibility= View.VISIBLE
                    }
                    3 -> {
                        tvTitle.text=getString(R.string.introTitle4)
                        tvNext.text = "Get Started"
                        tvSkip.visibility= View.GONE
                    }
                }

                setIndicator(position)
            }
        })

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);
    }
}