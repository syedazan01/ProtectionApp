package atoz.protection.activites;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import atoz.protection.R;


public class IRCameraDetector extends AppCompatActivity {
    private static final String TAG = "IRCameraDetector";
    EditText tvConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_r_camera_detector);
        // print log messages to EditText
        tvConsole = findViewById(R.id.tvconsole);
    }
}