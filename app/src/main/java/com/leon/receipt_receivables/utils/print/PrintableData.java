package com.leon.receipt_receivables.utils.print;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.leon.receipt_receivables.R;

public class PrintableData implements com.kishcore.sdk.hybrid.api.PrintableData {
    @SuppressLint("InflateParams")
    @Override
    public View toView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.printable_data, null);
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
