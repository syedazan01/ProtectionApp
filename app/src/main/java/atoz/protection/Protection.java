package atoz.protection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;

import com.balram.locker.view.AppLocker;

import atoz.protection.receivers.NetworkConnectionReciever;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Protection extends MultiDexApplication implements LifecycleObserver {
    private static Context mInstance;
    private InterstitialAd mInterstitialAd=null;
    private BroadcastReceiver mNetworkReceiver;
    private int result;
    private Intent intent;
    private MediaProjectionManager mMediaProjectionManager;


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.d("AppController", "Foreground");
        isAppInBackground(false);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.d("AppController", "Background");
        isAppInBackground(true);
    }

    // Adding some callbacks for test and log
    public interface ValueChangeListener {
        void onChanged(Boolean value);
    }
    private ValueChangeListener visibilityChangeListener;
    public void setOnVisibilityChangeListener(ValueChangeListener listener) {
        this.visibilityChangeListener = listener;
    }
    private void isAppInBackground(Boolean isBackground) {

        try {
            if (mNetworkReceiver!=null) {
                if (isBackground) {
                        unregisterReceiver(mNetworkReceiver);
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*if (mInterstitialAd==null) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitalId));
        }
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }*/
        if (null != visibilityChangeListener) {
            visibilityChangeListener.onChanged(isBackground);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        mNetworkReceiver=new NetworkConnectionReciever();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

         AppLocker.getInstance().enableAppLock(this);

        Firebase.setAndroidContext(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                    PrefManager.putString(AppConstant.FCMTOKEN,task.getResult().getToken());
            }
        });
    }
    public static Context getInstance()
    {
        if(mInstance==null)
            mInstance=new Protection();
        return mInstance;
    }
    public int getResult(){
        return result;
    }

    public Intent getIntent(){
        return intent;
    }

    public MediaProjectionManager getMediaProjectionManager(){
        return mMediaProjectionManager;
    }

    public void setResult(int result1){
        this.result = result1;
    }

    public void setIntent(Intent intent1){
        this.intent = intent1;
    }

    public void setMediaProjectionManager(MediaProjectionManager mMediaProjectionManager){
        this.mMediaProjectionManager = mMediaProjectionManager;
    }
}
