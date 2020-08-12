package com.example.protectionapp.cameraDetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class CameraDectectorByRM extends AppCompatActivity implements SensorEventListener {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    SensorManager sensorManager;
    Sensor magnetometerSensor;
    ProgressBar meterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_camera_dectector_by_r_m);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetometerSensor==null)
            Toast.makeText(this, "No Magnetometer sensor detector", Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        addViews();
        initActions();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void addViews() {
        toolbar=findViewById(R.id.toolbar);
        ivBack=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        meterView=findViewById(R.id.meterView);
        meterView.setMax(500);
        tvToolbarTitle.setText("Hidden Camera Detector");


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() ==Sensor.TYPE_MAGNETIC_FIELD)
        {
            String sensorValue=sensorEvent.values[0]+" "+ sensorEvent.values[1]+ " "+sensorEvent.values[2];
            meterView.setProgress((int)sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}