package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityDownloadBinding;
import com.leon.receipt_receivables.enums.DialogType;
import com.leon.receipt_receivables.enums.ProgressType;
import com.leon.receipt_receivables.enums.SharedReferenceKeys;
import com.leon.receipt_receivables.enums.SharedReferenceNames;
import com.leon.receipt_receivables.infrastructure.IAbfaService;
import com.leon.receipt_receivables.infrastructure.ICallback;
import com.leon.receipt_receivables.infrastructure.ICallbackError;
import com.leon.receipt_receivables.infrastructure.ICallbackIncomplete;
import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;
import com.leon.receipt_receivables.tables.KarbariDictionary;
import com.leon.receipt_receivables.tables.MyDatabase;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.ReceiptReceivablesFeedback;
import com.leon.receipt_receivables.tables.ResultDictionary;
import com.leon.receipt_receivables.tables.VosoolBill;
import com.leon.receipt_receivables.tables.VosoolLoad;
import com.leon.receipt_receivables.utils.CalendarTool;
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

public class DownloadActivity extends BaseActivity {
    ActivityDownloadBinding binding;
    Activity activity;
    ISharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void initialize() {
        binding = ActivityDownloadBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        activity = this;
        sharedPreferenceManager = new SharedPreferenceManager(activity, SharedReferenceNames.ACCOUNT.getValue());
        binding.imageViewDownload.setImageDrawable(ContextCompat.getDrawable(activity,
                R.drawable.img_download));
        setOnButtonDownloadClickListener();
    }

    void setOnButtonDownloadClickListener() {
        binding.buttonDownload.setOnClickListener(v -> {
            Retrofit retrofit = NetworkHelper.getInstance(sharedPreferenceManager.getStringData(
                    SharedReferenceKeys.TOKEN.getValue()), false);
            IAbfaService iAbfaService = retrofit.create(IAbfaService.class);
            Call<ReceiptReceivablesFeedback> call = iAbfaService.download();
            HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), activity,
                    new Download(), new GetErrorIncomplete(), new GetError());
        });
    }

    class Download implements ICallback<ReceiptReceivablesFeedback> {
        @Override
        public void execute(Response<ReceiptReceivablesFeedback> response) {
            Log.e("here","0");
            ISharedPreferenceManager sharedPreferenceManager =
                    new SharedPreferenceManager(activity, SharedReferenceNames.ACCOUNT.getValue());
            CalendarTool calendarTool = new CalendarTool();
            String date = String.valueOf(calendarTool.getIranianYear()).concat("/").
                    concat(calendarTool.getIranianMonth() < 10 ?
                            "0".concat(String.valueOf(calendarTool.getIranianMonth())) :
                            String.valueOf(calendarTool.getIranianMonth())).concat("/").
                    concat(calendarTool.getIranianDay() < 10 ?
                            "0".concat(String.valueOf(calendarTool.getIranianDay())) :
                            String.valueOf(calendarTool.getIranianDay()));
            sharedPreferenceManager.putData(SharedReferenceKeys.DATE.getValue(), date);
            final ReceiptReceivablesFeedback receiptReceivablesFeedback = response.body();
            final ReceiptReceivablesFeedback receiptReceivablesFeedbackTemp = response.body();
            if (receiptReceivablesFeedback != null && receiptReceivablesFeedbackTemp != null) {
                MyDatabase myDatabase = MyDatabaseClient.getInstance(activity).getMyDatabase();
                ArrayList<KarbariDictionary> karbariDictionaries = new ArrayList<>(
                        myDatabase.karbariDictionaryDao().getAllKarbariDictionary());
                ArrayList<ResultDictionary> resultDictionaries = new ArrayList<>(
                        myDatabase.resultDictionaryDao().getAllResultDictionary());
                ArrayList<VosoolLoad> vosoolLoads = new ArrayList<>(
                        myDatabase.vosoolLoadDao().getSentVosoolLoad(false));
                ArrayList<VosoolBill> vosoolBills = new ArrayList<>(
                        myDatabase.vosoolBillDao().getAllVosoolBill());
                Log.e("here","1");
                for (int i = 0; i < receiptReceivablesFeedbackTemp.karbariDictionary.size(); i++) {
                    for (KarbariDictionary karbariDictionary : karbariDictionaries)
                        if (receiptReceivablesFeedbackTemp.karbariDictionary.get(i).id == karbariDictionary.id)
                            receiptReceivablesFeedback.karbariDictionary.remove(receiptReceivablesFeedback.karbariDictionary.get(i));
                }
                Log.e("here","2");
                for (int i = 0; i < receiptReceivablesFeedbackTemp.resultDictionary.size(); i++) {
                    for (ResultDictionary resultDictionary : resultDictionaries)
                        if (receiptReceivablesFeedbackTemp.resultDictionary.get(i).id == resultDictionary.id)
                            receiptReceivablesFeedback.resultDictionary.remove(receiptReceivablesFeedback.resultDictionary.get(i));
                }

                Log.e("here","3");
                for (VosoolLoad vosoolLoad : vosoolLoads) {
                    for (int i = 0; i < receiptReceivablesFeedbackTemp.vosoolLoads.size(); i++) {
                        if (receiptReceivablesFeedbackTemp.vosoolLoads.get(i).billId.equals(vosoolLoad.billId)) {
                            myDatabase.vosoolLoadDao().updateVosoolByArchive(false, vosoolLoad.billId);
                            receiptReceivablesFeedback.vosoolLoads.remove(receiptReceivablesFeedbackTemp.vosoolLoads.get(i));
                        }
                    }
                }
                Log.e("here","4");
                for (VosoolBill vosoolBill : vosoolBills)
                    for (int i = 0; i < receiptReceivablesFeedbackTemp.vosoolLoads.size(); i++) {
                        for (int j = 0; j < receiptReceivablesFeedbackTemp.vosoolLoads.get(i).vosoolBills.size(); j++)
                            if (vosoolBill.id.equals(receiptReceivablesFeedbackTemp.vosoolLoads.get(i).vosoolBills.get(j).id))
                                receiptReceivablesFeedback.vosoolLoads.get(i).vosoolBills.remove(receiptReceivablesFeedbackTemp.vosoolLoads.get(i).vosoolBills.get(j));
                    }

                Log.e("here","5");
                for (int i = 0; i < receiptReceivablesFeedback.vosoolLoads.size(); i++) {
                    myDatabase.vosoolBillDao().insertAllVosoolBill(receiptReceivablesFeedback.vosoolLoads.get(i).vosoolBills);
                }
                Log.e("here","6");
                myDatabase.karbariDictionaryDao().insertKarbariDictionary(
                        receiptReceivablesFeedback.karbariDictionary);
                myDatabase.resultDictionaryDao().insertAllResultDictionary(
                        receiptReceivablesFeedback.resultDictionary);
                myDatabase.vosoolLoadDao().insertAllVosoolLoad(
                        receiptReceivablesFeedback.vosoolLoads);

                new CustomDialog(DialogType.Green, activity,
                        "تعداد ".concat(String.valueOf(receiptReceivablesFeedback.vosoolLoads.size())).concat(" اشتراک بارگیری شد."),
                        activity.getString(R.string.dear_user),
                        activity.getString(R.string.download),
                        activity.getString(R.string.accepted));
            }
        }
    }

    class GetErrorIncomplete implements ICallbackIncomplete<ReceiptReceivablesFeedback> {
        @Override
        public void executeIncomplete(Response<ReceiptReceivablesFeedback> response) {
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
            new CustomDialog(DialogType.YellowRedirect, DownloadActivity.this, error,
                    DownloadActivity.this.getString(R.string.dear_user),
                    DownloadActivity.this.getString(R.string.download),
                    DownloadActivity.this.getString(R.string.accepted));
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
        binding.imageViewDownload.setImageDrawable(null);
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