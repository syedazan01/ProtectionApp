package com.example.protectionapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.Log;
import android.widget.TextView;

import com.example.protectionapp.UserHelperClass;
import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    public  static <T>void storeInRTD(Context context, String child, String modelString)
    {
        Firebase reference;
        Firebase.setAndroidContext(context);
        try {

            reference = new Firebase("https://protectionapp-3baf6.firebaseio.com/"+ URLEncoder.encode("Personal Document","UTF-8")+"/"+child+"/");
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
}
