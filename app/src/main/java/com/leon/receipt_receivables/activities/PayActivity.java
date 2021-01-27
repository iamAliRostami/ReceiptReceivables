package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.GeneralBillPaymentCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.databinding.ActivityPayBinding;
import com.leon.receipt_receivables.utils.CustomToast;

import java.util.Locale;

import static com.leon.receipt_receivables.MyApplication.hostApp;
import static com.leon.receipt_receivables.MyApplication.isNetworkAvailable;

public class PayActivity extends AppCompatActivity {
    ActivityPayBinding binding;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        initialize();
    }

    void initialize() {
        onButtonBillPaymentClickListener();
    }

    void onButtonBillPaymentClickListener() {
        binding.buttonBillPayment.setOnClickListener(v -> {
            if (binding.editTextBillId.getText().toString().isEmpty()) {
                binding.editTextBillId.setError(getString(R.string.error_empty));
                View view = binding.editTextBillId;
                view.requestFocus();
                return;
            }
            if (binding.editTextPurchaseId.getText().toString().isEmpty()) {
                binding.editTextPurchaseId.setError(getString(R.string.error_empty));
                View view = binding.editTextPurchaseId;
                view.requestFocus();
                return;
            }
            if (isNetworkAvailable(activity)) {
                String invoiceNumber = binding.editTextPurchaseId.getText().toString();//"4351790968";
                String billId = binding.editTextBillId.getText().toString();//"10018315";
                SDKManager.billPayment(PayActivity.this, hostApp, billId, invoiceNumber, "-1", "OK", "OK", new GeneralBillPaymentCallback() {
                    @Override
                    public void onPaymentInitializationFailed(int status, String statusDescription,
                                                              String billId, String paymentId,
                                                              String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntent(PayActivity.this,
                                String.format(Locale.ENGLISH,
                                        "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد.", billId, paymentId),
                                String.format(Locale.ENGLISH,
                                        "کد وضعیت: %d", status),
                                String.format(Locale.ENGLISH, "شرح خطا:\n%s", statusDescription)
                                /*String.format(Locale.ENGLISH,
                                        "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد.\nکد وضعیت:%d\n" +
                                                statusDescription, billId, paymentId, status),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:%s", maskedPan),
                                String.format(Locale.ENGLISH, "هش کارت کاربر:%s", panHash)*/));
                    }

                    @Override
                    public void onPaymentSucceed(String terminalNo, String merchantId,
                                                 String posSerial, String billId, String paymentId,
                                                 String traceNumber, String rrn, String ref,
                                                 String amount, String txnDate, String txnTime,
                                                 String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntent(PayActivity.this,
                                "پرداخت قبض با موفقیت انجام شد.",
                                String.format(Locale.ENGLISH, "شناسه قبض:\n%s", billId),
                                String.format(Locale.ENGLISH, "شناسه پرداخت:\n%s", paymentId),
                                String.format(Locale.ENGLISH, "کد پیگیری:\n%s", traceNumber),
                                String.format(Locale.ENGLISH, "شماره مرجع بازیابی:\n%s", rrn),
                                String.format(Locale.ENGLISH, "مبلغ تراکنش:\n%s", amount),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:\n%s", maskedPan)/*,
                                String.format(Locale.ENGLISH, "هش کارت کاربر:%s", panHash)*/));
                    }

                    @Override
                    public void onPaymentFailed(int errorCode, String errorDescription,
                                                String terminalNo, String merchantId,
                                                String posSerial, String billId, String paymentId,
                                                String traceNumber, String rrn, String ref,
                                                String amount, String txnDate, String txnTime,
                                                String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntent(PayActivity.this, "پرداخت قبض با خطا مواجه شد.",
                                String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                String.format(Locale.ENGLISH, "شرح خطا:\n%s", errorDescription),
                                String.format(Locale.ENGLISH, "شناسه قبض:\n%s", billId),
                                String.format(Locale.ENGLISH, "شناسه پرداخت:\n%s", paymentId),
                                String.format(Locale.ENGLISH, "کد پیگیری:\n%s", traceNumber),
                                String.format(Locale.ENGLISH, "شماره مرجع بازیابی:\n%s", rrn),
                                String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:\n%s", maskedPan)/*,
                                String.format(Locale.ENGLISH, "هش کارت کاربر:%s", panHash)*/));
                    }

                    @Override
                    public void onPaymentCancelled(String billId, String paymentId,
                                                   String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntent(PayActivity.this,
                                String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId)/*,
                                String.format(Locale.ENGLISH, "شماره کارت کاربر:%s", maskedPan),
                                String.format(Locale.ENGLISH, "هش کارت کاربر:%s", panHash)*/));
                    }
                });
            } else {
                new CustomToast().warning(getString(R.string.turn_internet_on));
            }
        });

    }
}