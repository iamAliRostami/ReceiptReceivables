package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.kishcore.sdk.hybrid.api.GeneralBillPaymentCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.DetailsAdapter;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.tables.VosoolBill;
import com.leon.receipt_receivables.tables.VosoolLoad;
import com.leon.receipt_receivables.utils.CustomToast;

import java.util.ArrayList;
import java.util.Locale;

import static com.leon.receipt_receivables.MyApplication.hostApp;
import static com.leon.receipt_receivables.utils.PermissionManager.isNetworkAvailable;

public class PayActivity extends AppCompatActivity {
    com.leon.receipt_receivables.databinding.ActivityPayBinding binding;
    Activity activity;
    VosoolLoad vosoolLoad;
    ArrayList<DetailsAdapter.DetailsItem> detailsItems = new ArrayList<>();
    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.leon.receipt_receivables.databinding.ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        if (getIntent() != null) {
            String vosool = getIntent().getExtras().getString(BundleEnum.RESULT.getValue());
            Gson gson = new Gson();
            vosoolLoad = gson.fromJson(vosool, VosoolLoad.class);
            setupRecyclerView();
        }
        initialize();
    }

    void initialize() {
        binding.editTextBillId.setText(vosoolLoad.billId);
        binding.editTextBillId.setEnabled(false);
        binding.editTextPaymentId.setText(vosoolLoad.vosoolBills.get(vosoolLoad.vosoolBills.size() - 1).payId);
        binding.editTextPaymentId.setEnabled(false);
        onButtonBillPaymentClickListener();
        binding.buttonDetails.setOnClickListener(v -> binding.recyclerViewDetails.setVisibility(View.VISIBLE));
    }

    void setupRecyclerView() {
        for (VosoolBill vosoolBill : vosoolLoad.vosoolBills) {
            detailsItems.add(new DetailsAdapter.DetailsItem(vosoolBill.amount, vosoolBill.dateBed));
        }
        detailsAdapter = new DetailsAdapter(detailsItems);
        binding.recyclerViewDetails.setAdapter(detailsAdapter);
        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(activity));
    }

    void onButtonBillPaymentClickListener() {
        binding.buttonBillPayment.setOnClickListener(v -> {
            if (binding.editTextBillId.getText().toString().isEmpty()) {
                binding.editTextBillId.setError(getString(R.string.error_empty));
                View view = binding.editTextBillId;
                view.requestFocus();
                return;
            }
            if (binding.editTextPaymentId.getText().toString().isEmpty()) {
                binding.editTextPaymentId.setError(getString(R.string.error_empty));
                View view = binding.editTextPaymentId;
                view.requestFocus();
                return;
            }
            if (isNetworkAvailable(activity)) {
                String paymentId = binding.editTextPaymentId.getText().toString();
                String billId = binding.editTextBillId.getText().toString();
                SDKManager.billPayment(PayActivity.this, hostApp, billId, paymentId, "-1", "OK", "OK", new GeneralBillPaymentCallback() {
                    @Override
                    public void onPaymentInitializationFailed(int status, String statusDescription,
                                                              String billId, String paymentId,
                                                              String maskedPan, String panHash) {
                        startActivity(
                                ResultActivity.putIntentResult(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH,
                                                "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد.", billId, paymentId),
                                        String.format(Locale.ENGLISH,
                                                "کد وضعیت: %d", status),
                                        String.format(Locale.ENGLISH, "شرح خطا:\n%s", statusDescription)),
                                        billId, paymentId, maskedPan, "terminalNo",
                                        "merchantId", "traceNumber",
                                        "rrn", "ref", "amount", "txnDate", "txnTime"));
                    }

                    @Override
                    public void onPaymentSucceed(String terminalNo, String merchantId,
                                                 String posSerial, String billId, String paymentId,
                                                 String traceNumber, String rrn, String ref,
                                                 String amount, String txnDate, String txnTime,
                                                 String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntentResult(
                                ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت قبض با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "شناسه قبض:\n%s", billId),
                                        String.format(Locale.ENGLISH, "شناسه پرداخت:\n%s", paymentId),
                                        String.format(Locale.ENGLISH, "کد پیگیری:\n%s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی:\n%s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش:\n%s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n%s", maskedPan)),
                                billId, paymentId, maskedPan, terminalNo, merchantId, traceNumber,
                                rrn, ref, amount, txnDate, txnTime));
                    }

                    @Override
                    public void onPaymentFailed(int errorCode, String errorDescription,
                                                String terminalNo, String merchantId,
                                                String posSerial, String billId1, String paymentId,
                                                String traceNumber, String rrn, String ref,
                                                String amount, String txnDate, String txnTime,
                                                String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntentResult(
                                ResultActivity.putIntent(PayActivity.this, "پرداخت قبض با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا:\n%s", errorDescription),
                                        String.format(Locale.ENGLISH, "شناسه قبض:\n%s", billId),
                                        String.format(Locale.ENGLISH, "شناسه پرداخت:\n%s", paymentId),
                                        String.format(Locale.ENGLISH, "کد پیگیری:\n%s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی:\n%s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n%s", maskedPan)),
                                billId, paymentId, maskedPan, terminalNo, merchantId, traceNumber,
                                rrn, ref, amount, txnDate, txnTime));
                    }

                    @Override
                    public void onPaymentCancelled(String billId1, String paymentId,
                                                   String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntentResult(ResultActivity.putIntent(PayActivity.this,
                                String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId)),
                                billId, paymentId, maskedPan, "terminalNo",
                                "merchantId", "traceNumber", "rrn",
                                "ref", "amount", "txnDate", "txnTime"));
                    }
                });
            } else {
                new CustomToast().warning(getString(R.string.turn_internet_on));
            }
        });

    }
}