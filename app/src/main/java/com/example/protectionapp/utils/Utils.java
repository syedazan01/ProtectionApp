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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.protectionapp.UserHelperClass;
import com.example.protectionapp.utils.views.RoundView;
import com.firebase.client.Firebase;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.api.GoogleApiClient;
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

    public  static <T>void storeInRTD(Context context, String child, String modelString)
    {
        Firebase reference;
        Firebase.setAndroidContext(context);
        try {

            reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL+ URLEncoder.encode("Personal Document","UTF-8")+"/"+child+"/");
            if (child.equals(AppConstant.ADHAAR)) {
                UserHelperClass userHelperClass=fromJson(modelString,UserHelperClass.class);
                Log.e("sfbdfbfsabn",modelString);
                Log.e("sfbdfbfsabn",userHelperClass.getAddress());
                reference.setValue(userHelperClass);
            }
            /*else if(child.equals(AppConstant.ADHAAR))
            {
            }*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
