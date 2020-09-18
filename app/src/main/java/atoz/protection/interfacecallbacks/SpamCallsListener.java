package atoz.protection.interfacecallbacks;

import atoz.protection.model.SpamBean;

public interface SpamCallsListener {
    void onSaved(SpamBean spamBean);
    void onDeleted(int position);
}
