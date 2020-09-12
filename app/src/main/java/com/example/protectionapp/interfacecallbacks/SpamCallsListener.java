package com.example.protectionapp.interfacecallbacks;

import com.example.protectionapp.model.SpamBean;

public interface SpamCallsListener {
    void onSaved(SpamBean spamBean);
    void onDeleted(int position);
}
