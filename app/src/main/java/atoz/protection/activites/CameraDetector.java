package atoz.protection.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import atoz.protection.R;
import atoz.protection.cameraDetector.BathroomTips;
import atoz.protection.cameraDetector.BedroomTips;
import atoz.protection.cameraDetector.CameraDectectorByRM;
import atoz.protection.cameraDetector.Changingroom;
import atoz.protection.cameraDetector.OutsideTips;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CameraDetector extends AppCompatActivity {
    Toolbar toolbar;
    Button camDetect, tipsBt, btnInfraredCamera;
    ImageView ivBack;
    BottomSheetDialog bottomSheetDialog;
    RadioGroup Rgroup_rooms;
    Button next_tipsBT;
    RadioButton radio_bedroom,radio_bathroom,radio_changingRoom,radio_outside;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detector);
        setSupportActionBar(toolbar);
        findview();
        initActions();


    }

    private void findview() {
        camDetect = findViewById(R.id.btnCameraByRM);
        tipsBt = findViewById(R.id.tips_camBT);
        ivBack = findViewById(R.id.ivBack);
        btnInfraredCamera = findViewById(R.id.btnInfraredCamera);
        bottomSheetDialog = new BottomSheetDialog(CameraDetector.this, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.activity_cam_detector_tips);
        next_tipsBT=bottomSheetDialog.findViewById(R.id.next_tipsBT);
        Rgroup_rooms=bottomSheetDialog.findViewById(R.id.Rgroup_rooms);
        radio_bedroom=bottomSheetDialog.findViewById(R.id.bedroom_tips);
        radio_bathroom=bottomSheetDialog.findViewById(R.id.bathroom_tips);
        radio_changingRoom=bottomSheetDialog.findViewById(R.id.changingroom_tips);
        radio_outside=bottomSheetDialog.findViewById(R.id.outside_tips);
    }


    private void initActions() {
        next_tipsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                if(radio_bedroom.isChecked())
                {
                    intent=new Intent(CameraDetector.this, BedroomTips.class);
                }
                else if(radio_bathroom.isChecked())
                {
                    intent=new Intent(CameraDetector.this, BathroomTips.class);
                }
                else if(radio_changingRoom.isChecked())
                {
                    intent=new Intent(CameraDetector.this, Changingroom.class);
                }
                else if(radio_outside.isChecked())
                {
                    intent=new Intent(CameraDetector.this, OutsideTips.class);
                }
                else
                {
                    Utils.showToast(CameraDetector.this,"Please Select Any", AppConstant.errorColor);
               return;
                }
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tipsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.show();
              /*  Intent intent= new Intent(CameraDetector.this, CamDetector_tips.class);
                startActivity(intent);
*/
            }
        });
        camDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraDetector.this, CameraDectectorByRM.class);
                startActivity(intent);

            }
        });
        btnInfraredCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraDetector.this, IRCameraDetector.class);
                startActivity(intent);
            }
        });
    }


}