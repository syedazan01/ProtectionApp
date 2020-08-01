package com.example.protectionapp.receivers;

import android.content.Context;
import android.widget.Toast;

import com.aykuttasil.callrecord.CallRecord;

import java.util.Date;

public class CallRecordReceiver extends com.aykuttasil.callrecord.receiver.CallRecordReceiver {

    public CallRecordReceiver(CallRecord callRecord) {
        super(callRecord);
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {
        super.onIncomingCallReceived(context, number, start);
        Toast.makeText(context, number, Toast.LENGTH_SHORT).show();
    }
}
