package com.example.protectionapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.protectionapp.R;
import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.views.RoundView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Utils {
    public static void setShader(int startColor,int endColor,TextView tv)
    {
        Shader textShader=new LinearGradient(7, 2, 4, 10,
                new int[]{startColor, endColor},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }
    public static void makeButton(Button button, int color,float radius){
        button.setBackground(new RoundView(color, Utils.getRadius(radius)));
    }
    public static GradientDrawable getColoredDrawable(int startColor,int endColor){
        int[] colors = {startColor,endColor};
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
    }

    public static GradientDrawable getColoredDrawable(int startColor,int endColor,int shape,float cornerRadius){
        int[] colors = {startColor,endColor};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(shape);
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }

    public static GradientDrawable getColoredDrawable(int startColor, int endColor, int shape, float cornerRadius, int strokeWidth, int strokeColor){
        int[] colors = {startColor,endColor};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(shape);
        gradientDrawable.setStroke(strokeWidth,strokeColor);
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }

    public static Bitmap addGradientToImage(Bitmap originalBitmap, int startColor, int endColor) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }

    public static float[] getRadius(float value) {
        return new float[]{value, value, value, value, value, value, value, value};
    }

    public  static void storeDocumentsInRTD(Context context, String child, String modelString)
    {
        Firebase reference;
        Firebase.setAndroidContext(context);
            reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL+ AppConstant.PERSONAL_DOCUMENT+"/"+child+"/");
            if (child.equals(AppConstant.ADHAAR)) {
                UserHelperClass userHelperClass=fromJson(modelString,UserHelperClass.class);
                Log.e("sfbdfbfsabn",modelString);
                Log.e("sfbdfbfsabn",userHelperClass.getAddress());
                reference.child(AppConstant.USER_MOBILE).setValue(userHelperClass);
            }
            /*else if(child.equals(AppConstant.ADHAAR))
            {
            }*/
    }
    public static <T>String toJson(T value,Class<T> model)
    {
        return new Gson().toJson(value,model);
    }

    public static <T>T fromJson(String json,Class<T> model)
    {
        return new Gson().fromJson(json,model);
    }
    public static GoogleApiClient createGoogleClient(FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener connectionFailedListener)
    {

            return new GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity, connectionFailedListener)
                    .addApi(AppInvite.API)
                    .build();
    }
    public static void showToast(Activity activity, String mes, int  color) {
        String msg = "Something gone wrong.";
        if (mes == null) {
            mes = msg;
        } else if (mes.equalsIgnoreCase("")) {
            mes = msg;
        }
        hideKeyboard(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        LinearLayout root = layout.findViewById(R.id.toast_layout_root);
        root.setBackground(new RoundView(color, Utils.getRadius(100f)));
        TextView toastTextView = layout.findViewById(R.id.toastTextView);
        toastTextView.setText(mes);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    public static void hideKeyboard(Activity _activity) {
        View view = _activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) _activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void storeUserDetailsToRTD(Context context,UserBean userBean) {
        Firebase reference;
        Firebase.setAndroidContext(context);
        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL+ AppConstant.USER_DETAIL+"/");
        reference.child(PrefManager.getString(AppConstant.USER_MOBILE)).setValue(userBean);
    }
    public static Firebase getUserReference(Context context)
    {
        Firebase reference;
        Firebase.setAndroidContext(context);
        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL+ AppConstant.USER_DETAIL+"/");
      return reference;
    }
    public static StorageReference getStorageReference()
    {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        return storage.getReferenceFromUrl(AppConstant.FIREBASE_STORAGE_DATABASE_URL);
    }

}
