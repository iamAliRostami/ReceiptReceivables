package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityReportBinding;
import com.leon.receipt_receivables.tables.MyDatabase;
import com.leon.receipt_receivables.tables.MyDatabaseClient;

public class ReportActivity extends BaseActivity {
    ActivityReportBinding binding;
    int payed, reading, total,repetitive;
    Activity activity;

    @Override
    protected void initialize() {
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        activity = this;
        setupTextViews();
    }

    void setupTextViews() {
        MyDatabase myDatabase = MyDatabaseClient.getInstance(activity).getMyDatabase();
        total = myDatabase.vosoolLoadDao().countTotalVossolLoad();
        payed = myDatabase.vosoolLoadDao().countPayedVossolLoad(true);
        reading = myDatabase.vosoolLoadDao().countSentVossolLoad(true);
        repetitive = myDatabase.vosoolOffloadDao().getRepetitiveBillId();
//        repetitive = new ArrayList<>(myDatabase.vosoolOffloadDao().getRepetitiveBillId());
//        Log.e("size", String.valueOf(repetitive.size()));
//        for (int i = 0; i < repetitive.size(); i++)
//            Log.e("index", String.valueOf(repetitive.get(i)));
        binding.textViewPayed.setText(String.valueOf(payed));
        binding.textViewTotal.setText(String.valueOf(total));
        binding.textViewRead.setText(String.valueOf(reading));
        binding.textViewRepetitiveRead.setText(String.valueOf(repetitive));
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
        binding.imageViewRead.setImageDrawable(null);
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }
}