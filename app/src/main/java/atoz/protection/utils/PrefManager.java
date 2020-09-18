package atoz.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;

import atoz.protection.Protection;


public class PrefManager {
    private static SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "protectionApp";
    private final String ISLOGGEDIN = "isloggedin";

    static {
        sharedPreferences = Protection.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putBoolean(String key,Boolean value)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }


    public static String getString(String key)
    {
        return sharedPreferences.getString(key,"");
    }

    public static Boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key,false);
    }

public static void clear()
{
    sharedPreferences.edit().clear().apply();
}
}