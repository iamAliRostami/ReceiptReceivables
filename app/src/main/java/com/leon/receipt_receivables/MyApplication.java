package com.leon.receipt_receivables;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.multidex.MultiDex;

import com.kishcore.sdk.hybrid.api.HostApp;

import es.dmoral.toasty.Toasty;

public class MyApplication extends Application {
    public static final String FONT_NAME = "font/font_1.ttf";
    public static final int TOAST_TEXT_SIZE = 20;
    public static HostApp hostApp;
    static Context appContext;
    public static int POSITION = -1;

    public static Context getContext() {
        return appContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        appContext = getApplicationContext();
        Toasty.Config.getInstance()
                .tintIcon(true)
                .setToastTypeface(Typeface.createFromAsset(appContext.getAssets(), MyApplication.FONT_NAME))
                .setTextSize(TOAST_TEXT_SIZE)
                .allowQueue(true).apply();
        super.onCreate();
    }
}
