package com.example.protectionapp.RecordsActivites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.Firebase;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Adhaar extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private TextInputEditText dobinput;
    public URL imgaeurl;
    TextInputLayout adhharfullname, adharnumber, adharaddres, dob;
    Button addharsavebt;
    int yearofdob, monthofdob, dayofdob;
    RadioGroup radioGender;
    RadioButton radioMale,radioFemale,radioOther;
    ImageView imageView;
    Button opencam;
    String Gender;
    //firebase realtime database
    FirebaseDatabase rootNode;
//    DatabaseReference reference;
    //firebase Storage
    FirebaseStorage storage;
    StorageReference storageReference;
    Firebase reference;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to image  view
            imageView.setImageBitmap(captureImage);
        }*/
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            imageView.setImageURI(fileUri);

            //You can get File object from intent
            File file= ImagePicker.Companion.getFile(data);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar);
        radioGender = findViewById(R.id.Gradio);
        radioMale = findViewById(R.id.Gen_male);
        radioFemale = findViewById(R.id.Gen_female);
        radioOther = findViewById(R.id.Gen_other);
        imageView = findViewById(R.id.adhar_imageView);
        opencam = findViewById(R.id.cam_openbt);
//        radioGroup.setOnCheckedChangeListener(this);
        Firebase.setAndroidContext(this);
        try {
            reference = new Firebase("https://protectionapp-3baf6.firebaseio.com/"+ URLEncoder.encode("Personal Document","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //set the id of all fields
        adhharfullname = findViewById(R.id.adhhar_fullname);
        adharnumber = findViewById(R.id.adhar_number);
        adharaddres = findViewById(R.id.adhar_addres);
        addharsavebt = findViewById(R.id.addhr_savebt);
        dob = findViewById(R.id.dob);
        imageView = findViewById(R.id.adhar_imageView);
        //firebase storgae
        storageReference = FirebaseStorage.getInstance().getReference();



        //save data on SAVE button click firebase
        addharsavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the values
                String Fullname = adhharfullname.getEditText().getText().toString();
                String aadharNumber = adharnumber.getEditText().getText().toString();
                String Address = adharaddres.getEditText().getText().toString();
                String dateofbirth = dob.getEditText().getText().toString();

                String gender;
                if(radioMale.isChecked())
                   gender= "Male";
                else if(radioFemale.isChecked())
                    gender="Female";
                else gender="Other";

                UserHelperClass userHelperClass = new UserHelperClass(Fullname, dateofbirth,gender,aadharNumber,Address);
                rootNode=FirebaseDatabase.getInstance();
//                reference=rootNode.getReference();
//                reference.child("user").push().setValue(userHelperClass);
                Utils.storeInRTD(Adhaar.this, AppConstant.ADHAAR,Utils.toJson(userHelperClass,UserHelperClass.class));
            }
        });


        //Request for camera permission
        if (ContextCompat.checkSelfPermission(Adhaar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Adhaar.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        opencam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code
               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);*/

                ImagePicker.Companion.with(Adhaar.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        dobinput = findViewById(R.id.dob_calender);
        final Calendar calendar = Calendar.getInstance();
        dobinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearofdob = calendar.get(Calendar.YEAR);
                monthofdob = calendar.get(Calendar.MONTH);
                dayofdob = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Adhaar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dobinput.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

                    }
                }, yearofdob, monthofdob, dayofdob);
                datePickerDialog.show();
            }
        });


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.Gen_female:
                Gender = "female";
                break;
            case R.id.Gen_male:
                Gender = "Male";
                break;
            case R.id.Gen_other:
                Gender = "other";
                break;
        }

    }
}