package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.adapters.SpinnerCustomAdapter;
import com.leon.receipt_receivables.databinding.ActivityResultBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.PrintModel;
import com.leon.receipt_receivables.tables.PrintableDataList;
import com.leon.receipt_receivables.tables.ResultDictionary;
import com.leon.receipt_receivables.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;
    ArrayList<String> resultReturns;
    ArrayList<ResultDictionary> resultDictionaries = new ArrayList<>();
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    void initialize() {
        getExtra();
        setOnButtonPrintClickListener();
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

    void print() {
        if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
            ArrayList<PrintModel> printModels = new ArrayList<>();
            for (int i = 0; i < resultReturns.size(); i++) {
                printModels.add(new PrintModel(resultReturns.get(i)));
            }
            SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), 1, null);

        } else {
            new CustomToast().warning("پرینتر با مشکل مواجه است.", Toast.LENGTH_LONG);
        }
    }

    void getExtra() {
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
}