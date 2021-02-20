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
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
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
            SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), 1, null);

        } else {
            new CustomToast().warning(getString(R.string.printer_has_problem), Toast.LENGTH_LONG);
        }
    }

    void getExtra() {
        getResultExtra();
        resultReturns = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra(BundleEnum.RESULT.getValue()))
            resultReturns.addAll(intent.getStringArrayListExtra(BundleEnum.RESULT.getValue()));

        StringBuilder resultDescription = new StringBuilder();
        for (String resultReturn : resultReturns) {
            resultDescription.append(resultReturn).append("\n");
        }
        binding.textViewPrint.setText(String.valueOf(resultDescription));
    }

    public static Intent putIntent(Context context, String... resultValues) {
        Intent intent = new Intent(context, ResultActivity.class);
        ArrayList<String> resultValuesList = new ArrayList<>(Arrays.asList(resultValues));
        intent.putStringArrayListExtra(BundleEnum.RESULT.getValue(), resultValuesList);
        return intent;
    }

    public static Intent putIntentResult(Intent intent,String id, String billId, String paymentId,
                                         String maskedPan, String terminalNo, String merchantId,
                                         String trackNumber, String rrn, String ref, String amount,
                                         String txnDate, String txnTime, String description,
                                         double x, double y, boolean isPayed) {
        intent.putExtra(BundleEnum.ID.getValue(), id);
        intent.putExtra(BundleEnum.BILL_ID.getValue(), billId);
        intent.putExtra(BundleEnum.PAYMENT_ID.getValue(), paymentId);
        intent.putExtra(BundleEnum.MASKED_PAN.getValue(), maskedPan);
        intent.putExtra(BundleEnum.TERMINAL_NO.getValue(), terminalNo);
        intent.putExtra(BundleEnum.MERCHANT_ID.getValue(), merchantId);
        intent.putExtra(BundleEnum.TRACK_NUMBER.getValue(), trackNumber);
        intent.putExtra(BundleEnum.RRN.getValue(), rrn);
        intent.putExtra(BundleEnum.REF.getValue(), ref);
        intent.putExtra(BundleEnum.AMOUNT.getValue(), amount);
        intent.putExtra(BundleEnum.TXN_DATE.getValue(), txnDate);
        intent.putExtra(BundleEnum.TXN_TIME.getValue(), txnTime);
        intent.putExtra(BundleEnum.DESCRIPTION.getValue(), description);
        intent.putExtra(BundleEnum.IS_PAYED.getValue(), isPayed);
        intent.putExtra(BundleEnum.X.getValue(), x);
        intent.putExtra(BundleEnum.Y.getValue(), y);
        return intent;
    }

    void getResultExtra() {
        vosoolOffloadDto = new VosoolOffloadDto();
        if (getIntent() != null) {
            vosoolOffloadDto.posBillId = getIntent().getExtras().getString(BundleEnum.BILL_ID.getValue());
            vosoolOffloadDto.id = getIntent().getExtras().getString(BundleEnum.ID.getValue());
            vosoolOffloadDto.posPayId = getIntent().getExtras().getString(BundleEnum.PAYMENT_ID.getValue());
            vosoolOffloadDto.posTerminal = getIntent().getExtras().getString(BundleEnum.TERMINAL_NO.getValue());
            vosoolOffloadDto.bankTrackNumber = getIntent().getExtras().getString(BundleEnum.TRACK_NUMBER.getValue());
            vosoolOffloadDto.bankRRN = getIntent().getExtras().getString(BundleEnum.RRN.getValue());
            vosoolOffloadDto.description = getIntent().getExtras().getString(BundleEnum.DESCRIPTION.getValue());
            vosoolOffloadDto.cartNumber = getIntent().getExtras().getString(BundleEnum.MASKED_PAN.getValue());
            vosoolOffloadDto.isPaySuccess = getIntent().getExtras().getBoolean(BundleEnum.IS_PAYED.getValue());
            vosoolOffloadDto.posSerial = getIntent().getExtras().getString(BundleEnum.MERCHANT_ID.getValue());

            vosoolOffloadDto.posPayDate = getIntent().getExtras().getString(BundleEnum.TXN_DATE.getValue());

            vosoolOffloadDto.x1 = getIntent().getExtras().getDouble(BundleEnum.X.getValue());
            vosoolOffloadDto.y1 = getIntent().getExtras().getDouble(BundleEnum.Y.getValue());

//            String txnTime = getIntent().getExtras().getString(BundleEnum.TXN_TIME.getValue());
//            String ref = getIntent().getExtras().getString(BundleEnum.REF.getValue());
//            String amount = getIntent().getExtras().getString(BundleEnum.AMOUNT.getValue());
        } else {
            binding.textViewPrint.setVisibility(View.GONE);
            binding.buttonPrint.setVisibility(View.GONE);
        }
    }

}