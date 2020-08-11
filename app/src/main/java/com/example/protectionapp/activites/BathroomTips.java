package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;

public class BathroomTips extends AppCompatActivity {
   private TextView textView;
    ImageView ivBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_tips);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.bathroom_tipsTV);
        textView.setText("Scan for below devices (For manual scan, scan for lens looking part over below devices)\n" +
                "\n" +
                "1. Water heater - Precaution : Turn off heater and then bath \n" +
                "\n" +
                "2. Mirror -Precaution : Touch the mirror. Do you feel any gap between your finger and its reflection? If NO,there could be camera behind the mirror glass \n" +
                "\n" +
                "3. Ceiling / Smoke detector -Precaution : Apply sticker/sellotape over the viewable part of the detector \n" +
                "\n" +
                "4. Lamps or bulbs -Precaution : Can't do much. Use curtains whenever possible\n" +
                "\n" +
                "If you find anything even if suspicious, complaint to the authority."
        );

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}