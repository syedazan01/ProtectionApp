package atoz.protection.cameraDetector;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import atoz.protection.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CameraDectectorByRM extends AppCompatActivity implements SensorEventListener {
    public static DecimalFormat DECIMAL_FORMATTER;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    SensorManager sensorManager;
    Sensor magnetometerSensor;
    MediaPlayer mp;
    HalfGauge meterView;
private ImageView ivObject;
private TextView tvObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_dectector_by_r_m);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometerSensor == null)
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
        mp = MediaPlayer.create(this, R.raw.camera_detector);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void addViews() {
        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivObject = findViewById(R.id.ivObject);
        tvObject = findViewById(R.id.tvObject);
        meterView = findViewById(R.id.meterView);

        tvToolbarTitle.setText("Hidden Camera Detector");

        setupMeter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = sensorEvent.values[0];
            float magY = sensorEvent.values[1];
            float magZ = sensorEvent.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            // set value on the screen
            meterView.setValue(magnitude);
            if (magnitude >= 50.0)
            {
                if(magnitude<100.0)
                {
                    ivObject.setImageResource(R.drawable.ic_photo_camera_black_48dp);
                    tvObject.setText("Camera or similar object is detected");
                }
                else
                {

                    ivObject.setImageResource(R.drawable.ic_baseline_headset_24);
                    tvObject.setText("Microphone or similar object is detected");
                }
                play();
            }
            else
            {
                ivObject.setImageBitmap(null);
                tvObject.setText(null);
                stop();
            }

//            value.setText(DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
        }
      /*  if(sensorEvent.sensor.getType() ==Sensor.TYPE_MAGNETIC_FIELD)
        {
            String sensorValue=sensorEvent.values[0]+" "+ sensorEvent.values[1]+ " "+sensorEvent.values[2];

        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }

    private void setupMeter() {
        Range range3 = new Range();
        range3.setColor(Color.parseColor("#FF1F1F"));
        range3.setFrom(100.0);
        range3.setTo(150.0);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#FFB60C"));
        range2.setFrom(50.0);
        range2.setTo(100.0);

        Range range = new Range();
        range.setColor(Color.parseColor("#28A30F"));

        range.setFrom(0.0);
        range.setTo(50.0);

        //add color ranges to gauge
        meterView.addRange(range);
        meterView.addRange(range2);
        meterView.addRange(range3);

        //set min max and current value
        meterView.setMinValue(0.0);
        meterView.setMaxValue(150.0);
        meterView.setValue(0.0);
    }

    public void stop() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    public void play() {
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.start();
            }
        } else {
            mp = MediaPlayer.create(this, R.raw.camera_detector);
            mp.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

}