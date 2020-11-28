package com.leon.receipt_receivables.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kishcore.sdk.hybrid.api.HostApp;
import com.kishcore.sdk.irankish.rahyab.api.BillCallback;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.databinding.ActivityPayBinding;
import com.leon.receipt_receivables.dialogs.SelectWayDialog;
import com.leon.receipt_receivables.utils.print.Tools;

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
        onButtonPurchaseClickListener();
    }

    void onButtonPurchaseClickListener() {
        binding.buttonPurchase.setOnClickListener(v -> {
            String invoiceNumber = binding.editTextPurchaseId.getText().toString();
            String amount = Tools.convertToNumber(binding.editTextAmount.getText().toString());

            if (TextUtils.isEmpty(amount))
                binding.editTextAmount.setError(getString(R.string.error_empty));
            else if (hostApp == HostApp.IKC) {
                com.kishcore.sdk.irankish.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount, "-1", "تست پیغام تبلیغاتی زیر رسید",
                        "تست پیغام نمایش",
                        new com.kishcore.sdk.irankish.rahyab.api.PaymentCallback() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentInitializationFailed(int status, String statusDescription,
                                                                      String reserveNumber,
                                                                      String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, reserveNumber, status),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn,
                                                         String ref, String amount, String txnDate,
                                                         String txnTime, String maskedPan,
                                                         String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate,
                                                        String txnTime, String maskedPan,
                                                        String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan,
                                                           String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }
                        });
            } else if (hostApp == HostApp.SEP_IKCC) {
                com.kishcore.sdk.sepIkcc.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount, "-1", "تست پیغام تبلیغاتی زیر رسید",
                        "تست پیغام نمایش",
                        new com.kishcore.sdk.sepIkcc.rahyab.api.PaymentCallback() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentInitializationFailed(int status, String statusDescription,
                                                                      String reserveNumber,
                                                                      String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, reserveNumber, status),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn,
                                                         String ref, String amount,
                                                         String txnDate, String txnTime,
                                                         String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate,
                                                        String txnTime, String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan,
                                                           String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }
                        });
            } else if (hostApp == HostApp.FANAVA) {
                com.kishcore.sdk.fanava.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount,
                        new com.kishcore.sdk.fanava.rahyab.api.PaymentCallback() {
                            @Override
                            public void onPaymentInitializationFailed(String reserveNumber,
                                                                      String maskedPan,
                                                                      String errorDescription, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر: %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر: %s\n", panHash)));
                            }

                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn, String ref,
                                                         String amount, String txnDate,
                                                         String txnTime, String maskedPan,
                                                         String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر: %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر: %s\n", panHash)));
                            }

                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate,
                                                        String txnTime, String maskedPan,
                                                        String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر: %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر: %s\n", panHash)));
                            }

                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan,
                                                           String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر: %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر: %s\n", panHash)));
                            }
                        });
            } else if (hostApp == HostApp.SEPEHR) {
                Tools.displaySafeDialog(PayActivity.this, new SelectWayDialog(
                        PayActivity.this, data -> {
                    String paymentWay = "";
                    if (data != null && data.length > 0)
                        paymentWay = (String) data[0];
                    com.kishcore.sdk.sepehr.rahyab.api.SDKManager.purchase(
                            PayActivity.this, invoiceNumber, amount,
                            paymentWay.equals("normal") ? 0 : 1,
                            new com.kishcore.sdk.sepehr.rahyab.api.PaymentCallback() {
                                @Override
                                public void onPaymentInitializationFailed(String reserveNumber,
                                                                          String maskedPan,
                                                                          String errorDescription,
                                                                          String panHash) {
                                    startActivity(ResultActivity.putIntent(PayActivity.this,
                                            String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                            String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                            String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                                }

                                @Override
                                public void onPaymentSucceed(String terminalNo, String merchantId,
                                                             String posSerial, String reserveNumber,
                                                             String traceNumber, String rrn,
                                                             String ref, String amount1,
                                                             String txnDate, String txnTime,
                                                             String maskedPan, String panHash) {
                                    startActivity(ResultActivity.putIntent(PayActivity.this,
                                            "پرداخت با موفقیت انجام شد.",
                                            String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                            String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                            String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                            String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                            String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount1),
                                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                            String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                                    Toast.makeText(PayActivity.this,
                                            "PayActivity onPaymentSucceed",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPaymentFailed(int errorCode, String errorDescription,
                                                            String terminalNo, String merchantId,
                                                            String posSerial, String reserveNumber,
                                                            String traceNumber, String rrn,
                                                            String ref, String amount1,
                                                            String txnDate, String txnTime,
                                                            String maskedPan, String panHash) {
                                    startActivity(ResultActivity.putIntent(PayActivity.this,
                                            "پرداخت با خطا مواجه شد.",
                                            String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                            String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                            String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                            String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                            String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                            String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                            String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                            String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                            String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                            String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount1),
                                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                            String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                                }

                                @Override
                                public void onPaymentCancelled(String reserveNumber,
                                                               String maskedPan, String panHash) {
                                    startActivity(ResultActivity.putIntent(PayActivity.this,
                                            String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                            String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                            String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
                                }
                            });
                }));
            } else if (hostApp == HostApp.PEC) {
                com.kishcore.sdk.parsian.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount,
                        new com.kishcore.sdk.parsian.rahyab.api.PaymentCallback() {
                            @Override
                            public void onPaymentInitializationFailed(String reserveNumber,
                                                                      String maskedPan,
                                                                      String errorDescription) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn, String ref,
                                                         String amount, String txnDate, String txnTime,
                                                         String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate, String txnTime,
                                                        String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }
                        });
            } else if (hostApp == HostApp.PEC_MEHRANA) {
                com.kishcore.sdk.mehrana.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount, new com.kishcore.sdk.mehrana.rahyab.api.PaymentCallback() {
                            @Override
                            public void onPaymentInitializationFailed(String reserveNumber,
                                                                      String maskedPan,
                                                                      String errorDescription) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn, String ref,
                                                         String amount, String txnDate, String txnTime,
                                                         String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate, String txnTime,
                                                        String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }
                        });
            } else if (hostApp == HostApp.NAVACO) {
                com.kishcore.sdk.navaco.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount,
                        new com.kishcore.sdk.navaco.rahyab.api.PaymentCallback() {

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentInitializationFailed(String reserveNumber,
                                                                      String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentSucceed(String reserveNumber, String rrn,
                                                         String traceNumber, String amount,
                                                         String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.", String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", rrn),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentFailed(String reserveNumber, String traceNumber,
                                                        int errorCode, String errorDescription,
                                                        String amount, String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد. در صورت کسر وجه از حساب شما، مبلغ مذکور طی ۷۲ ساعت به حساب شما عودت خواهد شد در غیراینصورت حداکثر ظرف ۴۵ روز به حساب شما واریز می گردد.",
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                            }
                        });
            } else if (hostApp == HostApp.SEP) {
                com.kishcore.sdk.sep.rahyab.api.SDKManager.purchase(PayActivity.this,
                        invoiceNumber, amount,
                        new com.kishcore.sdk.sep.rahyab.api.PaymentCallback() {
                            @Override
                            public void onPaymentInitializationFailed(String reserveNumber,
                                                                      String maskedPan,
                                                                      String errorDescription) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد.", reserveNumber),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentSucceed(String terminalNo, String merchantId,
                                                         String posSerial, String reserveNumber,
                                                         String traceNumber, String rrn, String ref,
                                                         String amount, String txnDate, String txnTime,
                                                         String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با موفقیت انجام شد.",
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentFailed(int errorCode, String errorDescription,
                                                        String terminalNo, String merchantId,
                                                        String posSerial, String reserveNumber,
                                                        String traceNumber, String rrn, String ref,
                                                        String amount, String txnDate, String txnTime,
                                                        String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        "پرداخت با خطا مواجه شد.",
                                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
                                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
                                        String.format(Locale.ENGLISH, "شماره ترمینال: %s", terminalNo),
                                        String.format(Locale.ENGLISH, "کد پذیرنده: %s", merchantId),
                                        String.format(Locale.ENGLISH, "سریال دستگاه: %s", posSerial),
                                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
                                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                        String.format(Locale.ENGLISH, "شماره مرجع: %s", ref),
                                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan)));
                            }

                            @Override
                            public void onPaymentCancelled(String reserveNumber, String maskedPan) {
                                startActivity(ResultActivity.putIntent(PayActivity.this,
                                        String.format(Locale.ENGLISH,
                                                "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.",
                                                reserveNumber),
                                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s",
                                                maskedPan)));
                            }
                        });
            }
        });
    }

    void onButtonBillPaymentClickListener() {
        String invoiceNumber = binding.editTextPurchaseId.getText().toString();
        String billId = binding.buttonBillPayment.getText().toString();
        if (hostApp == HostApp.IKC) {
            com.kishcore.sdk.irankish.rahyab.api.SDKManager.payBill(PayActivity.this,
                    billId, invoiceNumber, "-1", "OK", "OK", new BillCallback() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentInitializationFailed(int status, String statusDescription,
                                                                  String billId, String paymentId,
                                                                  String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    String.format(Locale.ENGLISH, "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, billId, paymentId, status),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentSucceed(String terminalNo, String merchantId,
                                                     String posSerial, String billId,
                                                     String paymentId, String traceNumber,
                                                     String rrn, String ref, String amount,
                                                     String txnDate, String txnTime,
                                                     String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    "پرداخت قبض با موفقیت انجام شد.",
                                    String.format(Locale.ENGLISH, "شناسه قبض: %s", billId),
                                    String.format(Locale.ENGLISH, "شناسه پرداخت: %s", paymentId),
                                    String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                    String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                    String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentFailed(int errorCode, String errorDescription,
                                                    String terminalNo, String merchantId,
                                                    String posSerial, String billId,
                                                    String paymentId, String traceNumber,
                                                    String rrn, String ref, String amount,
                                                    String txnDate, String txnTime,
                                                    String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    "پرداخت قبض با خطا مواجه شد.",
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

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentCancelled(String billId, String paymentId,
                                                       String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }
                    });
        } else if (hostApp == HostApp.SEP_IKCC) {
            com.kishcore.sdk.sepIkcc.rahyab.api.SDKManager.payBill(PayActivity.this, billId,
                    invoiceNumber, "-1", "OK", "OK",
                    new com.kishcore.sdk.sepIkcc.rahyab.api.BillCallback() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentInitializationFailed(int status,
                                                                  String statusDescription,
                                                                  String billId, String paymentId,
                                                                  String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    String.format(Locale.ENGLISH, "آغاز فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, billId, paymentId, status),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentSucceed(String terminalNo, String merchantId,
                                                     String posSerial, String billId,
                                                     String paymentId, String traceNumber,
                                                     String rrn, String ref, String amount,
                                                     String txnDate, String txnTime,
                                                     String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    "پرداخت قبض با موفقیت انجام شد.",
                                    String.format(Locale.ENGLISH, "شناسه قبض: %s", billId),
                                    String.format(Locale.ENGLISH, "شناسه پرداخت: %s", paymentId),
                                    String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
                                    String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
                                    String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentFailed(int errorCode, String errorDescription,
                                                    String terminalNo, String merchantId,
                                                    String posSerial, String billId,
                                                    String paymentId, String traceNumber,
                                                    String rrn, String ref, String amount,
                                                    String txnDate, String txnTime,
                                                    String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    "پرداخت قبض با خطا مواجه شد.",
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

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onPaymentCancelled(String billId, String paymentId,
                                                       String maskedPan, String panHash) {
                            startActivity(ResultActivity.putIntent(PayActivity.this,
                                    String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId),
                                    String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
                                    String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
                        }
                    });
        }
    }
}