package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityUploadBinding;
import com.leon.receipt_receivables.enums.DialogType;
import com.leon.receipt_receivables.enums.ProgressType;
import com.leon.receipt_receivables.enums.SharedReferenceKeys;
import com.leon.receipt_receivables.enums.SharedReferenceNames;
import com.leon.receipt_receivables.infrastructure.IAbfaService;
import com.leon.receipt_receivables.infrastructure.ICallback;
import com.leon.receipt_receivables.infrastructure.ICallbackError;
import com.leon.receipt_receivables.infrastructure.ICallbackIncomplete;
import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;
import com.leon.receipt_receivables.tables.MyDatabase;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.VosoolOffload;
import com.leon.receipt_receivables.tables.VosoolOffloadDto;
import com.leon.receipt_receivables.tables.VosoolOffloadResponse;
import com.leon.receipt_receivables.utils.CustomDialog;
import com.leon.receipt_receivables.utils.CustomErrorHandling;
import com.leon.receipt_receivables.utils.CustomToast;
import com.leon.receipt_receivables.utils.HttpClientWrapper;
import com.leon.receipt_receivables.utils.NetworkHelper;
import com.leon.receipt_receivables.utils.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadActivity extends BaseActivity {
    ActivityUploadBinding binding;
    Activity activity;
    ArrayList<VosoolOffloadDto> vosoolOffloadDtos = new ArrayList<>();
    MyDatabase myDatabase;

    @Override
    protected void initialize() {
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        activity = this;
        binding.imageViewUpload.setImageDrawable(ContextCompat.getDrawable(activity,
                R.drawable.img_upload_on));
        myDatabase = MyDatabaseClient.getInstance(activity).getMyDatabase();
        setOnButtonDownloadClickListener();
    }

    void setOnButtonDownloadClickListener() {
        binding.buttonUpload.setOnClickListener(v -> {
            vosoolOffloadDtos.clear();
            vosoolOffloadDtos.addAll(myDatabase.vosoolOffloadDao().getVosoolOffloadDtoBySent(false));
            if (vosoolOffloadDtos.size() > 0) {
                upload();
                return;
            }
            new CustomDialog(DialogType.Green, activity,
                    "موردی برای بارگذاری وجود ندارد.",
                    activity.getString(R.string.dear_user),
                    activity.getString(R.string.upload),
                    activity.getString(R.string.accepted));
        });
    }

    void upload() {
        ISharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity,
                SharedReferenceNames.ACCOUNT.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(sharedPreferenceManager.getStringData(
                SharedReferenceKeys.TOKEN.getValue()), false);
        IAbfaService iAbfaService = retrofit.create(IAbfaService.class);
        Call<VosoolOffloadResponse> call = iAbfaService.upload(new VosoolOffload(vosoolOffloadDtos));
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), activity,
                new Upload(), new GetErrorIncomplete(), new GetError());
    }

    class Upload implements ICallback<VosoolOffloadResponse> {
        @Override
        public void execute(Response<VosoolOffloadResponse> response) {
            myDatabase.vosoolOffloadDao().updateVosoolOffloadDtoBySent(true);
            for (VosoolOffloadDto vosoolOffloadDto : vosoolOffloadDtos)
                myDatabase.vosoolLoadDao().updateVosoolBySent(true, vosoolOffloadDto.posBillId);
            if (response.body() != null) {
                new CustomDialog(DialogType.Green, activity,
                        response.body().message,
                        activity.getString(R.string.dear_user),
                        activity.getString(R.string.upload),
                        activity.getString(R.string.accepted));
            }
        }
    }

    class GetErrorIncomplete implements ICallbackIncomplete<VosoolOffloadResponse> {
        @Override
        public void executeIncomplete(Response<VosoolOffloadResponse> response) {
            CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(activity);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            new CustomToast().warning(error);
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(activity);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);

            new CustomDialog(DialogType.YellowRedirect, UploadActivity.this, error,
                    UploadActivity.this.getString(R.string.dear_user),
                    UploadActivity.this.getString(R.string.upload),
                    UploadActivity.this.getString(R.string.accepted));
        }
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
        binding.imageViewUpload.setImageDrawable(null);
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