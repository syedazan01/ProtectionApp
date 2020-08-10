package com.example.protectionapp.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Payment_premiumUser extends AppCompatActivity {
    TextInputLayout amount, note, name, upivirtualid;
    Button paybt;
    String TAG ="main";
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_premium_user);
        paybt = (Button) findViewById(R.id.paynowBT);
        amount = findViewById(R.id.paymentAmount_TL);
        note = findViewById(R.id.payment_note_TL);
        name =  findViewById(R.id.payment_nameTL);
        upivirtualid = findViewById(R.id.payment_upiID_TL);

        paybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
                if (TextUtils.isEmpty(name.getEditText().getText().toString())) {
                    Toast.makeText(Payment_premiumUser.this, " Name is invalid", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(upivirtualid.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment_premiumUser.this, " UPI ID is invalid", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(note.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment_premiumUser.this, " Note is invalid", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(amount.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment_premiumUser.this, " Amount is invalid", Toast.LENGTH_SHORT).show();
                } else {

                    payUsingUpi(name.getEditText().getText().toString(), upivirtualid.getEditText().getText().toString(),
                            note.getEditText().getText().toString(), amount.getEditText().getText().toString());
                }
            }
        });
    }

    private void payUsingUpi(String name, String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)  // virtual ID
                .appendQueryParameter("pn", name)    // name
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)           // any note about payment
                .appendQueryParameter("am", amount)         // amount
                .appendQueryParameter("cu", "INR")          // currency
                //.appendQueryParameter("refUrl", "blueapp")         // optional
                .build();
        //for only google pay
        /*String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        activity.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);*/


        // for all payment app accept google
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Payment_premiumUser.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
    }
}

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Payment_premiumUser.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Payment_premiumUser.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Payment_premiumUser.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);

            } else {
                Toast.makeText(Payment_premiumUser.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(Payment_premiumUser.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isConnectionAvailable(Payment_premiumUser payment_premiumUser) {
        ConnectivityManager connectivityManager = (ConnectivityManager) payment_premiumUser.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
