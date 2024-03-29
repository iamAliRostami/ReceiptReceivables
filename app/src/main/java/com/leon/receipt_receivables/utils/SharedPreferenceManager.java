package com.leon.receipt_receivables.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceManager implements ISharedPreferenceManager {
    Context context;
    SharedPreferences appPrefs;

    public SharedPreferenceManager(Context context, String xml) {
        this.context = context;
        appPrefs = this.context.getSharedPreferences(xml, MODE_PRIVATE);
    }

    public boolean checkIsNotEmpty(String key) {
        if (appPrefs == null) {
            return false;
        } else if (appPrefs.getString(key, "").length() < 1) {
            return false;
        } else return !appPrefs.getString(key, "").isEmpty();
    }

    @Override
    public void putData(String key, int value) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();

    }

//    public void putTheme(Boolean theme) {
//        SharedPreferences.Editor prefsEditor = appPrefs.edit();
//        prefsEditor.putBoolean(SharedReferenceKeys.THEME.getValue(), theme);
//        prefsEditor.apply();
//    }

    @Override
    public void putData(String key, String data) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putString(key, data);
        prefsEditor.apply();
    }

    @Override
    public void putData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = appPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    @Override
    public String getStringData(String key) {
        return appPrefs.getString(key, "");
    }

    @Override
    public int getIntData(String key) {
        return appPrefs.getInt(key, 1);
    }

    @Override
    public boolean getBoolData(String key) {
        return appPrefs.getBoolean(key, false);
    }

//    public boolean getTheme() {
//        return appPrefs.getBoolean(SharedReferenceKeys.THEME.getValue(), false);
//    }
}
