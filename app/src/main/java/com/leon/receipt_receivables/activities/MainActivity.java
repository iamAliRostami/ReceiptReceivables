package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.MyApplication;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityMainBinding;
import com.leon.receipt_receivables.enums.SharedReferenceKeys;
import com.leon.receipt_receivables.enums.SharedReferenceNames;
import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.utils.CalendarTool;
import com.leon.receipt_receivables.utils.SharedPreferenceManager;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    boolean exit = false;
    Activity activity;
    View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        if (id == R.id.image_view_exit) {
            exit = true;
            MyApplication.POSITION = -1;
            finishAffinity();
        } else {
            Intent intent = new Intent();
            if (id == R.id.image_view_download) {
                MyApplication.POSITION = 0;
                intent = new Intent(getApplicationContext(), DownloadActivity.class);
            } else if (id == R.id.image_view_reading) {
                MyApplication.POSITION = 1;
                intent = new Intent(getApplicationContext(), ReadingActivity.class);
            } else if (id == R.id.image_view_upload) {
                MyApplication.POSITION = 2;
                intent = new Intent(getApplicationContext(), UploadActivity.class);
            } else if (id == R.id.image_view_report) {
                MyApplication.POSITION = 3;
                intent = new Intent(getApplicationContext(), ReportActivity.class);
            } else if (id == R.id.image_view_help) {
                MyApplication.POSITION = 4;
                intent = new Intent(getApplicationContext(), HelpActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void initialize() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        activity = this;
        MyApplication.hostApp = SDKManager.init(this);
        setupImageViews();
        checkExpireDate();
    }

    void checkExpireDate() {
        ISharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity,
                SharedReferenceNames.ACCOUNT.getValue());
        if (sharedPreferenceManager.checkIsNotEmpty(SharedReferenceKeys.DATE.getValue()) &&
                CalendarTool.findDifferent(sharedPreferenceManager.getStringData(
                        SharedReferenceKeys.DATE.getValue()).substring(2)) > 0) {
            MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolLoadDao().updateVosoolByArchive(true);
        }
    }

    void setupImageViews() {
        binding.imageViewExit.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_exit));
        binding.imageViewDownload.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_download));
        binding.imageViewUpload.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_upload_on));
        binding.imageViewReading.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_reading));
        binding.imageViewReport.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_report));
        binding.imageViewHelp.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.img_help));
        setOnImageViewClickListener();
    }

    void setOnImageViewClickListener() {
        binding.imageViewDownload.setOnClickListener(onClickListener);
        binding.imageViewUpload.setOnClickListener(onClickListener);
        binding.imageViewReading.setOnClickListener(onClickListener);
        binding.imageViewReport.setOnClickListener(onClickListener);
        binding.imageViewHelp.setOnClickListener(onClickListener);
        binding.imageViewExit.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStop() {
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        binding.imageViewDownload.setImageDrawable(null);
        binding.imageViewUpload.setImageDrawable(null);
        binding.imageViewHelp.setImageDrawable(null);
        binding.imageViewExit.setImageDrawable(null);
        binding.imageViewReading.setImageDrawable(null);
        binding.imageViewReport.setImageDrawable(null);
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
        if (exit)
            android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}