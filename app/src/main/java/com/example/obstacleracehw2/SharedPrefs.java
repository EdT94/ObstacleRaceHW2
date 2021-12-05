package com.example.obstacleracehw2;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private static final String SP_FILE = "SharedPrefsFile";
    private SharedPreferences preferences;
    private static SharedPrefs sharedPrefs;


    public static SharedPrefs getInstance(Context context){
        if(sharedPrefs==null){
            sharedPrefs=new SharedPrefs(context);
        }
        return sharedPrefs;
    }

    public SharedPrefs(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public int getIntSP(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putIntSP(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getStringSP(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putStringSP(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    




}
