package com.ioki.key;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserDefinedSharedPreference {
    private Context context;

    UserDefinedSharedPreference(Context ctx){
        context = ctx;
    }

    public void saveData(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public String getPreferences(String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = "DEFAULT";
        return sharedPreferences.getString(keyValue, defaultValue);
    }

    public void removeAllSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}
