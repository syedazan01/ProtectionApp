package atoz.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Objects;

import atoz.protection.model.UserBean;

import static atoz.protection.utils.PrefManager.PREF_NAME;

public class SubscriptionCheckService extends Worker {
    private boolean isChecked=false;
    private SharedPreferences prefManager;
    public SubscriptionCheckService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        prefManager=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Result doWork() {
        if(prefManager.getBoolean(AppConstant.ISLOGGEDIN,false))
        {
            isChecked=false;
            Utils.getUserReference().child(Objects.requireNonNull(prefManager.getString(AppConstant.USER_MOBILE, ""))).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(!isChecked)
                    {
                        UserBean userBean=dataSnapshot.getValue(UserBean.class);
                        if(userBean!=null)
                        {
                            String dateTime=userBean.getPlanPurchaseDate();
                            if (dateTime!=null && dateTime.isEmpty()) {
                                String dayzLeft=Utils.getDaysLeft(dateTime);
                                if(!dayzLeft.equalsIgnoreCase(""))
                                {
                                    userBean.setSubscribe(true);

                                }
                                else
                                {
                                    userBean.setSubscribe(false);
                                }
                                prefManager.edit().putBoolean(AppConstant.IS_SUBSCRIBE,userBean.isSubscribe()).apply();
                                Utils.storeUserDetailsToRTD(userBean);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        return Result.success();
    }
}
