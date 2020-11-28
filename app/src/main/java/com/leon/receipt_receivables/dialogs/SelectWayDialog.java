package com.leon.receipt_receivables.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.kishcore.sdk.sepehr.rahyab.api.DataCallback;
import com.leon.receipt_receivables.databinding.DialogPaymentWayBinding;

public class SelectWayDialog extends Dialog {

    private final DataCallback dataCallback;
    DialogPaymentWayBinding binding;

    public SelectWayDialog(@NonNull Context context, DataCallback dataCallback) {
        super(context);
        this.dataCallback = dataCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogPaymentWayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init() {
        binding.buttonNormalPurchase.setOnClickListener(arg0 -> {
            dataCallback.onDataInserted("normal");
            dismiss();
        });
        binding.buttonBonPurchase.setOnClickListener(arg0 -> {
            dataCallback.onDataInserted("bon");
            dismiss();
        });
    }
}

