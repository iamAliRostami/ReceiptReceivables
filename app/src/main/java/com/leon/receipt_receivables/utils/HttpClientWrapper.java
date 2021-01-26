package com.leon.receipt_receivables.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.leon.receipt_receivables.MyApplication;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.enums.ProgressType;
import com.leon.receipt_receivables.infrastructure.ICallback;
import com.leon.receipt_receivables.infrastructure.ICallbackError;
import com.leon.receipt_receivables.infrastructure.ICallbackIncomplete;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HttpClientWrapper {
    public static Call call;

    public static <T> void callHttpAsync(Call<T> call, int dialogType,
                                         final Context context,
                                         final ICallback<T> callback,
                                         final ICallbackIncomplete<T> callbackIncomplete,
                                         final ICallbackError callbackError) {
        HttpClientWrapper.call = call;
        CustomProgressBar progressBar = new CustomProgressBar();
        if (dialogType == ProgressType.SHOW.getValue()) {
            progressBar.show(context, context.getString(R.string.waiting));
        } else if (dialogType == ProgressType.SHOW_CANCELABLE.getValue()) {
            progressBar.show(context, context.getString(R.string.waiting), true);
        } else if (dialogType == ProgressType.SHOW_CANCELABLE_REDIRECT.getValue()) {
            progressBar.show(context, context.getString(R.string.waiting), true);
        }
        if (MyApplication.isNetworkAvailable(context)) {
            HttpClientWrapper.call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (response.isSuccessful()) {
                        callback.execute(response);
                    } else {
                        ((Activity) context).runOnUiThread(() -> callbackIncomplete.executeIncomplete(response));
                    }
                    if (progressBar.getDialog() != null)
                        progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    ((Activity) context).runOnUiThread(() -> callbackError.executeError(t));
                    if (progressBar.getDialog() != null)
                        progressBar.getDialog().dismiss();
                }
            });
        } else {
            if (progressBar.getDialog() != null)
                progressBar.getDialog().dismiss();
            CustomToast customToast = new CustomToast();
            customToast.warning(context.getString(R.string.turn_internet_on));
        }
    }
}
