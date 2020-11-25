package com.leon.receipt_receivables.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.databinding.ActivityResultBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.tables.PrintModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    void initialize() {
        getExtra();
    }

    void print(){

        if (SDKManager.getPrinterStatus() == SDKManager.STATUS_OK) {
            //Print method takes an instance of Printable Data and a DataCallback as input
            //In this example, the printer starts printing an instance of TestPrintableDataList
            //then after successful end of printing first item it starts printing an instance of ListPrintableData.

            //***** Print View or List of Objects *****
            ArrayList<PrintModel> printModels = new ArrayList<>();
            String title = "تست", des = "این سطر شماره ";
            int count = 4;
            for (int i = 1; i <= count; i++) {
                printModels.add(new PrintModel(title + i, des + i));
            }

            //SDKManager.print(MainActivity.this, new TestPrintableDataList(printModels), null);
            //SDKManager.print(MainActivity.this, new TestPrintableData(), objects -> SDKManager.print(MainActivity.this, new ListPrintableData("این سطر اول است", "سطر دوم پایین تر است", "سطر سوم زیر سطر دوم است."), null));
                    /*SDKManager.print(MainActivity.this, new TestPrintableData(), data -> {
                        SDKManager.print(MainActivity.this, new TestPrintableDataList(printModels), data1 -> {*//*
                            SDKManager.print(MainActivity.this, new TestPrintableDataList(printModels), data2 -> {
                                SDKManager.print(MainActivity.this, new TestPrintableDataList(printModels), null);
                            });
                        *//*});
                    });*/

            //***** Print Bitmap *****
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bitmap_print_test);
//            SDKManager.printBitmap(ResultActivity.this, bmp, data -> {
//                //End of print
//            });
        }
    }
    void getExtra(){
        ArrayList<String> resultReturns = new ArrayList<>();
        Intent intent = getIntent();
        if(intent.hasExtra(BundleEnum.RESULT.getValue()))
            resultReturns.addAll(intent.getStringArrayListExtra(BundleEnum.RESULT.getValue()));

        StringBuilder resultDescription = new StringBuilder("\n");
        for (String resultReturn: resultReturns) {
            resultDescription.append(resultReturn).append("\n");
        }
    }
    public static Intent putIntent(Context context, String... resultValues) {
        Intent intent = new Intent(context, ResultActivity.class);
        ArrayList<String> resultValuesList = new ArrayList<>(Arrays.asList(resultValues));
        intent.putStringArrayListExtra(BundleEnum.RESULT.getValue(), resultValuesList);
        return intent;
    }
}