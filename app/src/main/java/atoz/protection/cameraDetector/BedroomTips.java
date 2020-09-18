package atoz.protection.cameraDetector;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import atoz.protection.R;

public class BedroomTips extends AppCompatActivity {
   private TextView textView;
    ImageView ivBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom_tips);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.bedroom_tipsTV);
        textView.setText("Scan for below devices (For manual scan, scan for lens looking part over below devices)\n" +
                "\n" +
                "1. Smoke Detector - Precaution : Apply sticker/sellotape over the viewable part of the detector \n" +
                "\n" +
                "2. Air Conditioner - Precaution : If possible use fans and turn off the AC. Turn rotating blades OFF. \n" +
                "\n" +
                "3. Television - Precaution : Turn off main supply. Keep objects front of the lens looking pat of the TV. \n" +
                "\n" +
                "4. Night lamp - Precaution : Keep it off or change its direction \n" +
                "\n" +
                "5. Flower pot - Change position to least viewable area. \n" +
                "\n" +
                "6. Coffee maker - Precaution : Unplug it and put it inside cupboard\n" +
                "\n" +
                "Finally do not forget to turn lights off.\n" +
                "\n" +
                "\n" +
                "More info\n" +
                "\n" +
                "Some easy steps can save you from uneasy situations");
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}