package atoz.protection.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static atoz.protection.utils.Utils.isOnline;

public class NetworkConnectionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!isOnline(context))
            Toast.makeText(context, "Internet Connection error", Toast.LENGTH_SHORT).show();
    }


}
