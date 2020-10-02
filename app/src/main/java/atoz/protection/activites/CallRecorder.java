package atoz.protection.activites;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import atoz.protection.R;
import atoz.protection.adapters.RecordingFileAdapter;
import atoz.protection.adapters.ViewPageAdapter;
import atoz.protection.fragments.PlayerFragment;
import atoz.protection.fragments.Recording_fragment;
import atoz.protection.interfacecallbacks.onPlay;
import atoz.protection.model.RecordingFileData;
import atoz.protection.services.CallRecorderService;
import atoz.protection.services.FloatingWindowService;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

public class CallRecorder extends AppCompatActivity implements onPlay {

    private Toolbar toolbar_recorder;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private CardView cardPlayer;
    private boolean isSetPlayer;
//    private SeekBar sbPlayer;
//    private TextView tvMusicName;
//    private ImageView ivPlayPause;
//    private MediaPlayer mMediaPlayer;
    private JcPlayerView jcPlayerView;
    private Handler handler;
    private Switch callToggle;
    private RelativeLayout rltCallRecording;

    //    private CallRecord callRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
            setTheme(R.style.AppTheme_Base_Light);
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
    callToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Intent intent = new Intent(CallRecorder.this, CallRecorderService.class);
            if (b) {
                if(isMyServiceRunning(CallRecorderService.class))
                    stopService(new Intent(CallRecorder.this, FloatingWindowService.class));
                startService(intent);
//                PrefManager.putBoolean(AppConstant.IS_CALL_RECORDING_ON,true);
//                callRecord.startCallRecordService();
                Toast.makeText(getApplicationContext(), "Call Recording is set ON", Toast.LENGTH_SHORT).show();
            } else {
                stopService(intent);
//                callRecord.stopCallReceiver();
                Toast.makeText(getApplicationContext(), "Call Recording is set OFF", Toast.LENGTH_SHORT).show();
//                PrefManager.putBoolean(AppConstant.IS_CALL_RECORDING_ON,false);
            }
                }
    });
    /*ivPlayPause.setOnClickListener(new View.OnClickListener() {
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
    });*/
}

    @Override
    protected void onResume() {
        super.onResume();
        // Runtime permission
        try {

            boolean permissionGranted_recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_WriteExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_ReadExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


                    if (permissionGranted_recordAudio) {
                        if (permissionGranted_WriteExternal) {
                            if (permissionGranted_ReadExternal) {
                                try {
//                                    rltCallRecording.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                            }
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        }
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 400);
                    }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
    }

    private void initViews() {
        jcPlayerView=findViewById(R.id.jcPlayerView);
        toolbar_recorder = findViewById(R.id.toolbar_recorder);
        tabLayout = findViewById(R.id.tablayout_recorder);
        viewPager = findViewById(R.id.viewpager_recorder);
        cardPlayer = findViewById(R.id.cardPlayer);
       /* tvMusicName = findViewById(R.id.tvMusicName);
        ivPlayPause = findViewById(R.id.ivPlayPause);
        sbPlayer = findViewById(R.id.sbPlayer);*/
        rltCallRecording = findViewById(R.id.rltCallRecording);
        callToggle = findViewById(R.id.callToggle);
//        sbPlayer.setOnSeekBarChangeListener(this);
        handler = new Handler();
        RecordingFileAdapter.onPlay = this;

        if (isMyServiceRunning(CallRecorderService.class))
            callToggle.setChecked(true);
        else
            callToggle.setChecked(false);
       /* callRecord = new CallRecord.Builder(this)
                .setLogEnable(true)
                .setRecordFileName("call_"+System.currentTimeMillis())
                .setRecordDirName("Protection Call Records")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
                .setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // optional & default value
                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // optional & default value
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) // optional & default value
                .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
//                .setLogEnable(true)
//                .setShowPhoneNumber(true)
                .build();*/
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
        JcAudio jcAudio=JcAudio.createFromFilePath(recordingFileData.getFilePath());
        jcAudio.setTitle(recordingFileData.getFileName());
        if (!jcAudios.contains(jcAudio)) {
            jcAudios.add(jcAudio);
        }
            jcPlayerView.initPlaylist(jcAudios, null);
        /*tvMusicName.setText(recordingFileData.getFileName());

        if(mMediaPlayer!=null)
        {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer=null;
//                mMediaPlayer.reset();
                handler.removeCallbacks(run);
        }
        else
           mMediaPlayer=new MediaPlayer();
        try {
            Log.e("sdvcfsbvrwe",recordingFileData.getFilePath());
            mMediaPlayer.setDataSource(this, Uri.fromFile(new File(recordingFileData.getFilePath())));
            mMediaPlayer.prepare();
            sbPlayer.setMax(mMediaPlayer.getDuration());
            sbPlayer.setProgress(0);
            mMediaPlayer.start();
            ivPlayPause.setImageResource(R.drawable.ic_media_pause);
            handler.post(run);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToast(this,"IncorrectFile", AppConstant.errorColor);
        }*/
    }

   /* @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(jcPlayerView.isPlaying())
        {
            jcPlayerView.pause();
        }
        /*if(mMediaPlayer!=null)
        {
            if(mMediaPlayer.isPlaying())
            {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer.reset();
                handler.removeCallbacks(run);
            }
        }*/
    }
   /* Runnable run = new Runnable() { @Override public void run() { seekUpdation(); } };

    public void seekUpdation() {
        sbPlayer.setProgress(mMediaPlayer.getCurrentPosition());
        handler.postDelayed(run, 1000);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200 || requestCode == 300 || requestCode == 400 || requestCode == 500 || requestCode == 600) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
//                    rltCallRecording.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (jcPlayerView.isPlaying()) {
            jcPlayerView.pause();
        }
    }
}