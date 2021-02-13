package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.SpinnerCustomAdapter;
import com.leon.receipt_receivables.databinding.ActivityResultBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.PrintModel;
import com.leon.receipt_receivables.tables.PrintableDataList;
import com.leon.receipt_receivables.tables.ResultDictionary;
import com.leon.receipt_receivables.tables.VosoolOffloadDto;
import com.leon.receipt_receivables.utils.CustomToast;
import com.leon.receipt_receivables.utils.GPSTracker;

import java.util.ArrayList;

public class  ResultCustomActivity extends AppCompatActivity {
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
        getResultExtra();
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
            if (!vosoolOffloadDto.isPaySuccess) {
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
            finish();
        });
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

    public static Intent putIntentResult(Context context, String billId, String paymentId,
                                         double x, double y) {
        Intent intent = new Intent(context, ResultCustomActivity.class);
        intent.putExtra(BundleEnum.BILL_ID.getValue(), billId);
        intent.putExtra(BundleEnum.PAYMENT_ID.getValue(), paymentId);
        intent.putExtra(BundleEnum.X.getValue(), x);
        intent.putExtra(BundleEnum.Y.getValue(), y);
        return intent;
    }

    void getResultExtra() {
        vosoolOffloadDto = new VosoolOffloadDto();
        if (getIntent() != null) {
            vosoolOffloadDto.posBillId = getIntent().getExtras().getString(BundleEnum.BILL_ID.getValue());
            vosoolOffloadDto.posPayId = getIntent().getExtras().getString(BundleEnum.PAYMENT_ID.getValue());
            vosoolOffloadDto.x1 = getIntent().getExtras().getDouble(BundleEnum.X.getValue());
            vosoolOffloadDto.y1 = getIntent().getExtras().getDouble(BundleEnum.Y.getValue());
        }
        binding.textViewPrint.setVisibility(View.GONE);
        binding.buttonPrint.setVisibility(View.GONE);
    }

}