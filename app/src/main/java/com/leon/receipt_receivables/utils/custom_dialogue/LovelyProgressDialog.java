package com.leon.receipt_receivables.utils.custom_dialogue;

import android.content.Context;

import com.leon.receipt_receivables.R;


/**
 * Created by yarolegovich on 16.04.2016.
 */
public class LovelyProgressDialog extends AbsLovelyDialog<LovelyProgressDialog> {

    {
        setCancelable(false);
    }

    public LovelyProgressDialog(Context context) {
        super(context);
    }

    public LovelyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_progress;
    }
}
