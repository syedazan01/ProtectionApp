package com.example.protectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.protectionapp.adapters.RecordingFileAdapter;
import com.example.protectionapp.fragments.PlayerFragment;
import com.example.protectionapp.fragments.Recording_fragment;
import com.example.protectionapp.fragments.SettingFragment;
import com.example.protectionapp.interfacecallbacks.onPlay;
import com.example.protectionapp.model.RecordingFileData;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;

public class CallRecorder extends AppCompatActivity implements onPlay,SeekBar.OnSeekBarChangeListener {

    private Toolbar toolbar_recorder;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CardView cardPlayer;
    private SeekBar sbPlayer;
    private TextView tvMusicName;
    private ImageView ivPlayPause;
    private MediaPlayer mMediaPlayer;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recorder);
        initViews();
        initActions();
        setSupportActionBar(toolbar_recorder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


    }
private void initActions()
{
    ivPlayPause.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mMediaPlayer!=null)
            {
                if(mMediaPlayer.isPlaying())
                {
                    ivPlayPause.setImageResource(R.drawable.ic_round_play);
                    mMediaPlayer.pause();
                    handler.removeCallbacks(run);
                }
                    else
                {
                    ivPlayPause.setImageResource(R.drawable.ic_media_pause);
                    int total=mMediaPlayer.getDuration();
                    int current=mMediaPlayer.getCurrentPosition();
                    mMediaPlayer.start();
                    mMediaPlayer.seekTo(current);
                    handler.post(run);
                }
                    }
        }
    });
}
    private void initViews() {
        toolbar_recorder = findViewById(R.id.toolbar_recorder);
        tabLayout = findViewById(R.id.tablayout_recorder);
        viewPager = findViewById(R.id.viewpager_recorder);
        cardPlayer = findViewById(R.id.cardPlayer);
        tvMusicName = findViewById(R.id.tvMusicName);
        ivPlayPause = findViewById(R.id.ivPlayPause);
        sbPlayer = findViewById(R.id.sbPlayer);
        sbPlayer.setOnSeekBarChangeListener(this);
        handler=new Handler();
        RecordingFileAdapter.onPlay=this;
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),2);
        viewPageAdapter.addFragment(new Recording_fragment(),"Recording");
        viewPageAdapter.addFragment(new PlayerFragment(),"Player");
//        viewPageAdapter.addFragment(new SettingFragment(),"Setting");
        viewPager.setAdapter(viewPageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(android.R.id.home==item.getItemId())
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void playMusic(RecordingFileData recordingFileData) {
        if(cardPlayer.getVisibility()==View.GONE)
        {
            cardPlayer.setVisibility(View.VISIBLE);
            cardPlayer.startAnimation(AnimationUtils.loadAnimation(this,R.anim.in_from_bottom));
        }
        tvMusicName.setText(recordingFileData.getFileName());

        if(mMediaPlayer!=null)
        {
            if(mMediaPlayer.isPlaying())
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer.reset();
                handler.removeCallbacks(run);
            }

        }
        else
           mMediaPlayer=new MediaPlayer();
        try {
            Log.e("sfvdfbgvfd",new File(recordingFileData.getFilePath()).getAbsolutePath());
            mMediaPlayer.setDataSource(this, Uri.fromFile(new File(recordingFileData.getFilePath())));
            mMediaPlayer.prepare();
            sbPlayer.setMax(mMediaPlayer.getDuration());
            sbPlayer.setProgress(0);
            mMediaPlayer.start();
            ivPlayPause.setImageResource(R.drawable.ic_media_pause);
            handler.post(run);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer!=null)
        {
            if(mMediaPlayer.isPlaying())
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer.reset();
                handler.removeCallbacks(run);
            }
        }
    }
    Runnable run = new Runnable() { @Override public void run() { seekUpdation(); } };

    public void seekUpdation() {
        sbPlayer.setProgress(mMediaPlayer.getCurrentPosition());
        handler.postDelayed(run, 1000);
    }

}