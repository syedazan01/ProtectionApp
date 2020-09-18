package atoz.protection.cameraDetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import atoz.protection.R;

public class CamDetector_tips extends AppCompatActivity {
    RadioGroup radioGroupRooms;
    RadioButton bedRoom, bathRoom, changingRoom, outside;
    Button next;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_detector_tips);
        initViews();
        initAction();
    }

    private void initAction() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bedRoom.isChecked()){
                    Intent intent = new Intent(CamDetector_tips.this,BedroomTips.class);
                    startActivity(intent);
                } else if (bathRoom.isChecked()){
                    Intent intent = new Intent(CamDetector_tips.this,BathroomTips.class);
                    startActivity(intent);

                } else if (changingRoom.isChecked()){
                    Intent intent = new Intent(CamDetector_tips.this,Changingroom.class);
                    startActivity(intent);

                } else if (outside.isChecked()) {
                    Intent intent = new Intent(CamDetector_tips.this, OutsideTips.class);
                    startActivity(intent);
                } else
                    Toast.makeText(CamDetector_tips.this, "Select any option", Toast.LENGTH_SHORT).show();

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        next = findViewById(R.id.next_tipsBT);
        radioGroupRooms = findViewById(R.id.Rgroup_rooms);
        bedRoom = findViewById(R.id.bedroom_tips);
        bathRoom =findViewById(R.id.bathroom_tips);
        changingRoom = findViewById(R.id.changingroom_tips);
        outside = findViewById(R.id.outside_tips);
        ivBack = findViewById(R.id.ivBack);
    }
}