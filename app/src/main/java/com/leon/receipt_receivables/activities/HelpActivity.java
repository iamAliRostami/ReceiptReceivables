package com.leon.receipt_receivables.activities;

import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityHelpBinding;

public class HelpActivity extends BaseActivity {
    ActivityHelpBinding binding;

    @Override
    protected void initialize() {
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
//        binding.pdfView.fromAsset("v.pdf")
//                .showMinimap(false).enableSwipe(true).load();
        binding.pdfView.fromAsset("v.pdf").load();
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