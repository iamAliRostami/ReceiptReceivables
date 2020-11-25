package com.leon.receipt_receivables.utils;

import android.content.Context;

import com.leon.receipt_receivables.R;

import java.io.IOException;

import retrofit2.Response;

public class CustomErrorHandling {
    Context context;

    public CustomErrorHandling(Context context) {
        this.context = context;
    }

    public String getErrorMessageTotal(Throwable throwable) {
        String errorMessage;
        if (throwable instanceof IOException) {
            errorMessage = context.getString(R.string.error_connection);
        } else {
            errorMessage = context.getString(R.string.error_other);
        }
        return errorMessage;
    }

    public <T> String getErrorMessageDefault(Response<T> response) {
        String errorMessage;
        int code = response.code();
        if (code >= 500 && code < 600) {
            errorMessage = context.getString(R.string.error_internal);
        } else if (code == 404) {
            errorMessage = context.getString(R.string.error_change_server);
        } else if (code >= 400 && code < 500) {
            errorMessage = context.getString(R.string.error_not_auth);
        } else {
            errorMessage = context.getString(R.string.error_other);
        }
        return errorMessage;
    }
}