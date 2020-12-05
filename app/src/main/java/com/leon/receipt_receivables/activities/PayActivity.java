package com.leon.receipt_receivables.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.GeneralBillPaymentCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.databinding.ActivityPayBinding;

import java.util.Locale;

import static com.leon.receipt_receivables.MyApplication.hostApp;

public class PayActivity extends AppCompatActivity {
    ActivityPayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    void initialize() {
        onButtonBillPaymentClickListener();
    }
    void onButtonBillPaymentClickListener() {
        binding.buttonBillPayment.setOnClickListener(v -> {
            Log.e("here", "onclick");
            String invoiceNumber = "4351790968";//binding.editTextPurchaseId.getText().toString();
            String billId = "10018315";//binding.editTextBillId.getText().toString();

            SDKManager.billPayment(PayActivity.this, hostApp, billId, invoiceNumber, "-1", "OK", "OK", new GeneralBillPaymentCallback() {
                @Override
                public void onPaymentInitializationFailed(int status, String statusDescription, String billId, String paymentId, String maskedPan, String panHash) {
                    startActivity(ResultActivity.putIntent(PayActivity.this, String.format(Locale.ENGLISH, "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, billId, paymentId, status), String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan), String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String billId, String paymentId, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    startActivity(ResultActivity.putIntent(PayActivity.this, "پرداخت قبض با موفقیت انجام شد.",
                            String.format(Locale.ENGLISH, "شناسه قبض: %s", billId),
                            String.format(Locale.ENGLISH, "شناسه پرداخت: %s", paymentId),
                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String billId, String paymentId, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
                    startActivity(ResultActivity.putIntent(PayActivity.this, "پرداخت قبض با خطا مواجه شد.",
                            String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                            String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                            String.format(Locale.ENGLISH, "شناسه قبض: %s", billId),
                            String.format(Locale.ENGLISH, "شناسه پرداخت: %s", paymentId),
                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                            String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }

                @Override
                public void onPaymentCancelled(String billId, String paymentId, String maskedPan, String panHash) {
                    startActivity(ResultActivity.putIntent(PayActivity.this, String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId), String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan), String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                }
            });
        });

    }
}