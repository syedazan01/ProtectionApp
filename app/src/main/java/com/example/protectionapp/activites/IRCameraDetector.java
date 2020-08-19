package com.example.protectionapp.activites;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protectionapp.R;
import com.obd.infrared.InfraRed;
import com.obd.infrared.log.LogToEditText;
import com.obd.infrared.transmit.TransmitterType;


public class IRCameraDetector extends AppCompatActivity {
    private static final String TAG = "IRCameraDetector";
    EditText tvConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_r_camera_detector);
        // print log messages to EditText
        tvConsole = findViewById(R.id.tvconsole);
        LogToEditText log = new LogToEditText(tvConsole, TAG);
        InfraRed infraRed = new InfraRed(this, log);
        // print Log messages with Log.d(), Log.w(), Log.e() (LogCat)
        // LogToConsole log = new LogToConsole(TAG);
        TransmitterType transmitterType = infraRed.detect();

        // initialize transmitter by type
        infraRed.createTransmitter(transmitterType);
        // Turn off logs
//         LogToAir log = new LogToAir(TAG);
    }
}