package atoz.protection.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import atoz.protection.R;
import atoz.protection.adapters.IntroPagerAdapter;
import atoz.protection.utils.Utils;

public class App_intro extends AppCompatActivity implements View.OnClickListener {
    ViewPager introViewPager;
    TextView tvIntro, tvDesc;
    CardView cardArrow, cardGetStarted;
    LinearLayout llIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.transparentStatusBar(this);
        setContentView(R.layout.activity_app_intro);
        initViews();
        initActions();
    }

    private void initViews() {
        introViewPager = findViewById(R.id.introViewPager);
        tvIntro = findViewById(R.id.tvIntro);
        tvDesc = findViewById(R.id.tvDesc);
        cardArrow = findViewById(R.id.cardArrow);
        cardGetStarted = findViewById(R.id.cardGetStarted);
        llIntro = findViewById(R.id.llIntro);

        cardArrow.setBackground(Utils.getThemeGradient(50F));
        cardGetStarted.setBackground(Utils.getThemeGradient(50F));
    }

    private void initActions() {
        IntroPagerAdapter introAdapter = new IntroPagerAdapter(this);
        introViewPager.setAdapter(introAdapter);
        cardArrow.setOnClickListener(this);
        cardGetStarted.setOnClickListener(this);
        introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvIntro.setText(getResources().getString(R.string.welcome));
                        tvDesc.setText(getResources().getString(R.string.app_intro1));
                        cardArrow.setVisibility(View.VISIBLE);
                        cardGetStarted.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvIntro.setText(getResources().getString(R.string.protect_doc));
                        tvDesc.setText(getResources().getString(R.string.app_intro2));
                        cardArrow.setVisibility(View.GONE);
                        cardGetStarted.setVisibility(View.VISIBLE);
                        break;
                }
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setIndicator(int position) {
        for (int i = 0; i < llIntro.getChildCount(); i++) {
            View view = llIntro.getChildAt(i);
            if (i == position) {
                view.setBackgroundColor(Color.parseColor("#0077FF"));
            } else {
                view.setBackgroundColor(Color.parseColor("#DFDFDF"));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == cardArrow) {
            introViewPager.setCurrentItem(1);
        } else if (view == cardGetStarted) {
            startActivity(new Intent(this, LogIn.class));
        }
    }
}