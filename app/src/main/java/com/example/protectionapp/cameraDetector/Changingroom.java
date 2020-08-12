package com.example.protectionapp.cameraDetector;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protectionapp.R;

public class Changingroom extends AppCompatActivity {
    private TextView textView;
    ImageView ivBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changingroom);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.changingroom_tipsTV);
        textView.setText("Scan for below devices (For manual scan, scan for lens looking part over below devices)\n" +
                "\n" +
                "1. Mirror -Touch the mirror. Do you feel any gap between your finger and its reflection? If NO, there could be camera behind the mirror glass.Report immediately or change the room. \n" +
                "\n" +
                "2. Hanger - Check for lens looking aperture for hanger especially screw looking objects. Put cloths covering all the screws \n" +
                "\n" +
                "3. Ceiling / Smoke detector - Apply sticker/sellotape over the viewable part of the detector. \n" +
                "\n" +
                "\n" +
                "\n" +
                "If you find anything suspicious, leave immediately and complaint to the authority.");
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}