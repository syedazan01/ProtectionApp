package atoz.protection.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import atoz.protection.R;
import atoz.protection.RecordsActivites.SendDailog;
import atoz.protection.adapters.ViewPageAdapter;
import atoz.protection.fragments.ReceivedFragment;
import atoz.protection.fragments.SendFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class FileShare extends AppCompatActivity implements SendDailog.SendDialogListener {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;

    @Override
    public void applyTexts(String message, String password) {
        (( SendDailog.SendDialogListener)((ViewPageAdapter) Objects.requireNonNull(viewPager.getAdapter())).getItem(viewPager.getCurrentItem())).applyTexts(message,password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
            //setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_file_share);
        addViews();
        initActions();
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new SendFragment(),"SEND");
        viewPageAdapter.addFragment(new ReceivedFragment(),"RECEIVED");
//        viewPageAdapter.addFragment(new SettingFragment(),"Setting");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              /*  if (viewPager.getCurrentItem() == 0) {
                    if (SendFragment.sendClickListener != null) {
                        SendFragment.sendClickListener.onSent();
                    }
                } else {
                    if (ReceivedFragment.recievedClickListener != null) {
                        ReceivedFragment.recievedClickListener.onRecieved();
                    }
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public interface SendClickListener {
        void onSent();
    }

    public interface RecievedClickListener {
        void onRecieved();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addViews() {
        tabLayout = findViewById(R.id.fileshareTL);
        viewPager=findViewById(R.id.viewpager_fileshare);

        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("File Sharer");
    }
}