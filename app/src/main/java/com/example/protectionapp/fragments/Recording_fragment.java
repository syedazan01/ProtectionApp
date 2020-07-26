package com.example.protectionapp.fragments;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.example.protectionapp.interfacecallbacks.onRecordFileSave;
import com.example.protectionapp.services.RecordingService;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.views.VisualizerView;

import java.io.File;
import java.util.Random;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;


public class Recording_fragment extends Fragment {
    public static MediaRecorder mRecorder = null;
   public static onRecordFileSave onRecordFileSave=null;
    public  Handler handler =new Handler();
    Toast toast;
    Chronometer mChronometer;
    ImageView mRecordButton;
    CardView cardRecord;
    private int mRecordPromptCount = 0;
    private int timeWhenPaused = 0;
    private TextView mRecordingPrompt;
    public  boolean mStartRecording=true;
    public  VisualizerView visualizer;

    public Recording_fragment() {
        // Required empty public constructor
    }
    // updates the visualizer every 50 milliseconds
   public  Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
                // get the current amplitude


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecorder!=null) {
                            int x = new Random().nextInt(10000);
                            visualizer.addAmplitude(x); // update the VisualizeView
                            visualizer.invalidate(); // refresh the VisualizerView
                        }
                    }
                });

                // update in 40 milliseconds
                handler.postDelayed(this, 40);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recording_fragment, container, false);
        mChronometer = view.findViewById(R.id.chronometer);
        mRecordButton = view.findViewById(R.id.recording_button);
        mRecordingPrompt = view.findViewById(R.id.mRecordingPrompt);
        cardRecord = view.findViewById(R.id.cardRecord);
        visualizer = view.findViewById(R.id.visualizer);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);
        cardRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

    }

    // Recording Start/Stop
    //TODO: recording pause
    private void onRecord(boolean start) {

        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (start) {
            // start recording
            handler.post(updateVisualizer);
            mRecordButton.setImageResource(R.drawable.ic_baseline_stop_24);
            //mPauseButton.setVisibility(View.VISIBLE);
            if(toast!=null)
                toast.cancel();
            toast=Toast.makeText(getActivity(), R.string.toast_recording_start, Toast.LENGTH_SHORT);
            toast.show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            //start RecordingService
            getActivity().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;

        } else {
            //stop recording
            handler.removeCallbacks(updateVisualizer);
            visualizer.clear();
            mRecordButton.setImageResource(R.drawable.recording_button_icon);
            //mPauseButton.setVisibility(View.GONE);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
//            mRecordingPrompt.setText(getString(R.string.record_prompt));

            getActivity().stopService(intent);
            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }

    //TODO: implement pause recording
    private void onPauseRecord(boolean pause) {
        if (pause) {
            mChronometer.stop();
            mRecordButton.setImageResource(R.drawable.recording_button_icon);
        }
        else
        {
            mChronometer.start();
        }
        /*if (pause) {
            //pause recording
            mRecordButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.ic_media_play, 0, 0, 0);
            mRecordingPrompt.setText((String) getString(R.string.resume_recording_button).toUpperCase());
            timeWhenPaused = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
        } else {
            //resume recording
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.ic_media_pause, 0, 0, 0);
            mRecordingPrompt.setText((String) getString(R.string.pause_recording_button).toUpperCase());
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            mChronometer.start();
        }*/
    }
}