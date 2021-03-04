package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.SpinnerCustomAdapter;
import com.leon.receipt_receivables.databinding.ActivityResultBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.enums.ProgressType;
import com.leon.receipt_receivables.enums.SharedReferenceKeys;
import com.leon.receipt_receivables.enums.SharedReferenceNames;
import com.leon.receipt_receivables.infrastructure.IAbfaService;
import com.leon.receipt_receivables.infrastructure.ICallback;
import com.leon.receipt_receivables.infrastructure.ICallbackError;
import com.leon.receipt_receivables.infrastructure.ICallbackIncomplete;
import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.PrintModel;
import com.leon.receipt_receivables.tables.PrintableDataList;
import com.leon.receipt_receivables.tables.ResultDictionary;
import com.leon.receipt_receivables.tables.VosoolOffload;
import com.leon.receipt_receivables.tables.VosoolOffloadDto;
import com.leon.receipt_receivables.tables.VosoolOffloadResponse;
import com.leon.receipt_receivables.utils.CustomToast;
import com.leon.receipt_receivables.utils.GPSTracker;
import com.leon.receipt_receivables.utils.HttpClientWrapper;
import com.leon.receipt_receivables.utils.NetworkHelper;
import com.leon.receipt_receivables.utils.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResultCustomActivity extends AppCompatActivity {
    ActivityResultBinding binding;
    ArrayList<String> resultReturns;
    ArrayList<ResultDictionary> resultDictionaries = new ArrayList<>();
    Activity activity = this;
    VosoolOffloadDto vosoolOffloadDto;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    void initialize() {
        getExtra();
        gpsTracker = new GPSTracker(activity);
        if (vosoolOffloadDto.isPaySuccess) {
            binding.spinner.setVisibility(View.GONE);
            vosoolOffloadDto.x2 = gpsTracker.getLongitude();
            vosoolOffloadDto.y2 = gpsTracker.getLatitude();
            vosoolOffloadDto.accuracy = gpsTracker.getAccuracy();
            vosoolOffloadDto.resultId =
                    resultDictionaries.get(binding.spinner.getSelectedItemPosition()).id;
            MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolOffloadDao().
                    insertVosoolOffloadDto(vosoolOffloadDto);
            MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolLoadDao().
                    updateVosoolByPayed(vosoolOffloadDto.isPaySuccess, vosoolOffloadDto.posBillId);
        }
        setOnButtonPrintClickListener();
        setOnButtonSubmitClickListener();
        setupSpinner();
    }

    void setupSpinner() {
        resultDictionaries.addAll(MyDatabaseClient.getInstance(activity).getMyDatabase().
                resultDictionaryDao().getAllResultDictionary());

        ArrayList<String> items = new ArrayList<>();
        for (ResultDictionary resultDictionary : resultDictionaries)
            items.add(resultDictionary.title);
        SpinnerCustomAdapter adapter = new SpinnerCustomAdapter(activity, items);
        binding.spinner.setAdapter(adapter);
    }

    void setOnButtonPrintClickListener() {
        binding.buttonPrint.setOnClickListener(v -> print());
    }

    void setOnButtonSubmitClickListener() {
        binding.buttonSubmit.setOnClickListener(v -> {
            vosoolOffloadDto.description = binding.editTextDescription.getText().toString();
            if (!vosoolOffloadDto.isPaySuccess) {
                vosoolOffloadDto.x2 = gpsTracker.getLongitude();
                vosoolOffloadDto.y2 = gpsTracker.getLatitude();
                vosoolOffloadDto.accuracy = gpsTracker.getAccuracy();
                vosoolOffloadDto.resultId =
                        resultDictionaries.get(binding.spinner.getSelectedItemPosition()).id;
                MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolLoadDao().
                        updateVosoolByPayed(vosoolOffloadDto.isPaySuccess, vosoolOffloadDto.posBillId);
                MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolOffloadDao().
                        insertVosoolOffloadDto(vosoolOffloadDto);
            }
            upload();
        });
    }

    void upload() {
        ISharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity,
                SharedReferenceNames.ACCOUNT.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(sharedPreferenceManager.getStringData(
                SharedReferenceKeys.TOKEN.getValue()), false);
        IAbfaService iAbfaService = retrofit.create(IAbfaService.class);
        ArrayList<VosoolOffloadDto> vosoolOffloadDtos = new ArrayList<>();
        vosoolOffloadDtos.add(vosoolOffloadDto);
        Call<VosoolOffloadResponse> call = iAbfaService.upload(new VosoolOffload(vosoolOffloadDtos));
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), activity,
                new Upload(), new GetErrorIncomplete(), new GetError());
    }

    class Upload implements ICallback<VosoolOffloadResponse> {
        @Override
        public void execute(Response<VosoolOffloadResponse> response) {
            if (response.body() != null && response.body().errorCode == 200) {
                MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolLoadDao().
                        updateVosoolBySent(true, vosoolOffloadDto.posBillId);
                MyDatabaseClient.getInstance(activity).getMyDatabase().vosoolOffloadDao().
                        updateVosoolOffloadDtoBySent(true, vosoolOffloadDto.posBillId);
            }
            finish();
        }
    }

    class GetErrorIncomplete implements ICallbackIncomplete<VosoolOffloadResponse> {
        @Override
        public void executeIncomplete(Response<VosoolOffloadResponse> response) {
            finish();
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            finish();
        }
    }

    void print() {
        if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
            ArrayList<PrintModel> printModels = new ArrayList<>();
            for (int i = 0; i < resultReturns.size(); i++) {
                printModels.add(new PrintModel(resultReturns.get(i)));
            }
            SDKManager.print(ResultCustomActivity.this, new PrintableDataList(printModels), 1, null);

        } else {
            new CustomToast().warning(getString(R.string.printer_has_problem), Toast.LENGTH_LONG);
        }
    }

    public static Intent putIntentResult(Context context, String id, String billId, String paymentId,
                                         String name, long amount, double x, double y) {
        Intent intent = new Intent(context, ResultCustomActivity.class);
        intent.putExtra(BundleEnum.BILL_ID.getValue(), billId);
        intent.putExtra(BundleEnum.ID.getValue(), id);
        intent.putExtra(BundleEnum.PAYMENT_ID.getValue(), paymentId);
        intent.putExtra(BundleEnum.NAME.getValue(), name);
        intent.putExtra(BundleEnum.AMOUNT.getValue(), amount);
        intent.putExtra(BundleEnum.X.getValue(), x);
        intent.putExtra(BundleEnum.Y.getValue(), y);
        return intent;
    }

    void getExtra() {
        getResultExtra();
        resultReturns = new ArrayList<>();

        resultReturns.add("مشترک گرامی ".concat(vosoolOffloadDto.name));
        resultReturns.add("مبلغ قبض ".concat(String.valueOf(vosoolOffloadDto.amount)));
        resultReturns.add("ش. قبض ".concat(vosoolOffloadDto.posBillId));
        resultReturns.add("ش. پرداخت ".concat(vosoolOffloadDto.posPayId));

        StringBuilder resultDescription = new StringBuilder();
        for (String resultReturn : resultReturns) {
            resultDescription.append(resultReturn).append("\n");
        }
        ISharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity, SharedReferenceNames.ACCOUNT.getValue());
        resultReturns.add("کد مامور: ".concat(sharedPreferenceManager.getStringData(SharedReferenceKeys.USER_CODE.getValue())));
        resultReturns.add("شرکت آب و فاضلاب استان اصفهان");
        binding.textViewPrint.setText(String.valueOf(resultDescription));
    }

    void getResultExtra() {
        vosoolOffloadDto = new VosoolOffloadDto();
        if (getIntent() != null) {
            vosoolOffloadDto.posBillId = getIntent().getExtras().getString(BundleEnum.BILL_ID.getValue());
            vosoolOffloadDto.name = getIntent().getExtras().getString(BundleEnum.NAME.getValue());
            vosoolOffloadDto.amount = getIntent().getExtras().getLong(BundleEnum.AMOUNT.getValue());
            vosoolOffloadDto.id = getIntent().getExtras().getString(BundleEnum.ID.getValue());
            vosoolOffloadDto.posPayId = getIntent().getExtras().getString(BundleEnum.PAYMENT_ID.getValue());
            vosoolOffloadDto.x1 = getIntent().getExtras().getDouble(BundleEnum.X.getValue());
            vosoolOffloadDto.y1 = getIntent().getExtras().getDouble(BundleEnum.Y.getValue());
        }
//        binding.textViewPrint.setVisibility(View.GONE);
//        binding.buttonPrint.setVisibility(View.GONE);
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