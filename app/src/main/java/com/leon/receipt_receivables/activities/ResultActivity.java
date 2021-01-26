package com.leon.receipt_receivables.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.databinding.ActivityResultBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.tables.PrintModel;
import com.leon.receipt_receivables.tables.PrintableDataList;
import com.leon.receipt_receivables.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;
    ArrayList<String> resultReturns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
        print();
    }

    void initialize() {
        getExtra();
    }

    void print() {
        if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
            ArrayList<PrintModel> printModels = new ArrayList<>();
            for (int i = 0; i < resultReturns.size(); i++) {
                printModels.add(new PrintModel(resultReturns.get(i).substring(0, resultReturns.get(i).indexOf(":") + 1),
                        resultReturns.get(i).substring(resultReturns.get(i).indexOf(":") + 1)));
            }
            SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), 1, null);

//            SDKManager.print(ResultActivity.this, new PrintableData(), objects -> SDKManager.print(ResultActivity.this, new ListPrintableData("این سطر اول است", "سطر دوم پایین تر است", "سطر سوم زیر سطر دوم است."), null));
//            SDKManager.print(ResultActivity.this, new PrintableData(), data1 -> {
//                SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), data2 -> {
//                    SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), data3 -> {
//                        SDKManager.print(ResultActivity.this, new PrintableDataList(printModels), null);
//                    });
//                });
//            });

            //***** Print Bitmap *****
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_print_test);
//            SDKManager.printBitmap(ResultActivity.this, bmp, data -> {
//                //End of print
//            });
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
        Log.e("result", String.valueOf(resultDescription));
    }

    public static Intent putIntent(Context context, String... resultValues) {
        Intent intent = new Intent(context, ResultActivity.class);
        ArrayList<String> resultValuesList = new ArrayList<>(Arrays.asList(resultValues));
        intent.putStringArrayListExtra(BundleEnum.RESULT.getValue(), resultValuesList);
        return intent;
    }
}