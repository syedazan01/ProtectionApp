package atoz.protection.activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BlockedNumberContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import atoz.protection.R;
import atoz.protection.adapters.SpamCallAdapter;
import atoz.protection.interfacecallbacks.SpamCallsListener;
import atoz.protection.model.SpamBean;
import atoz.protection.room.AppDatabase;
import atoz.protection.room.dao.SpamCallDao;
import atoz.protection.spam.ITelephony;
import atoz.protection.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SpamActivity extends AppCompatActivity implements SpamCallsListener, SpamCallAdapter.SpamCallRecyclerViewListener {
    private ImageView ivBack;
    private RecyclerView rvSpamCall;
    LinearLayout llNoSpam;
    SpamCallAdapter spamCallAdapter;
    private ImageView ivAdd;
    SpamCallDao spamCallDao;
    List<SpamBean> spamBeanList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);
        initViews();
        initActions();
        TelephonyManager telecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Class c = null;
        try {
            c = Class.forName(telecomManager.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(telecomManager);
            telephonyService.silenceRinger();
            telephonyService.endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        Context.startActivity(telecomManager.cr, null);
       /* Cursor c = getContentResolver().query(BlockedNumberContract.BlockedNumbers.CONTENT_URI,
                new String[]{BlockedNumberContract.BlockedNumbers.COLUMN_ID,
                        BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
                        BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER}, null, null, null);
        ContentValues values = new ContentValues();
        values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, "8278668446");
        Uri uri = getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values);*/

    }

    private void initViews() {
        rvSpamCall = findViewById(R.id.rvSpamCall);
        llNoSpam = findViewById(R.id.llNoSpam);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Spam Calls");
        spamCallAdapter = new SpamCallAdapter(this);
        spamCallDao = AppDatabase.getAppDataBase(this).getSpamCallDao();
    rvSpamCall.setLayoutManager(new LinearLayoutManager(this));
    rvSpamCall.setAdapter(spamCallAdapter);
    }

    private void initActions() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                spamBeanList=spamCallDao.getSpamCalls();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (spamBeanList.size()>0) {
                            spamCallAdapter.updateList(spamBeanList);
                        rvSpamCall.setVisibility(View.VISIBLE);
                        llNoSpam.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showSpamCallDialog(SpamActivity.this);
            }
        });
    }

    @Override
    public void onSaved(SpamBean spamBean) {
        spamBeanList.add(spamBean);
        rvSpamCall.setVisibility(View.VISIBLE);
        llNoSpam.setVisibility(View.GONE);
        spamCallAdapter.updateList(spamBeanList);
    }

    @Override
    public void onDeleted(int position) {
        spamBeanList.remove(position);
        if (spamBeanList.size()>0) {
            spamCallAdapter.notifyItemRemoved(position);
        }
        else
        {
            rvSpamCall.setVisibility(View.GONE);
            llNoSpam.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDelete(int id, int position) {
        Utils.showSpamCallDeleteDialog(this, id, position);
    }

    @Override
    public void onChecked(boolean b, int id) {
        spamCallDao.updateCheck(id, b);
    }
}