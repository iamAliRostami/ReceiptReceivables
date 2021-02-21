package com.leon.receipt_receivables.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.kishcore.sdk.hybrid.api.GeneralBillPaymentCallback;
import com.kishcore.sdk.hybrid.api.SDKManager;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.DetailsAdapter;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.enums.ProgressType;
import com.leon.receipt_receivables.infrastructure.IAbfaService;
import com.leon.receipt_receivables.infrastructure.ICallback;
import com.leon.receipt_receivables.infrastructure.ICallbackError;
import com.leon.receipt_receivables.infrastructure.ICallbackIncomplete;
import com.leon.receipt_receivables.tables.CustomPlace;
import com.leon.receipt_receivables.tables.VosoolBill;
import com.leon.receipt_receivables.tables.VosoolLoad;
import com.leon.receipt_receivables.utils.CoordinateConversion;
import com.leon.receipt_receivables.utils.CustomToast;
import com.leon.receipt_receivables.utils.GPSTracker;
import com.leon.receipt_receivables.utils.HttpClientWrapper;
import com.leon.receipt_receivables.utils.NetworkHelper;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.leon.receipt_receivables.MyApplication.hostApp;
import static com.leon.receipt_receivables.utils.PermissionManager.isNetworkAvailable;

public class PayActivity extends AppCompatActivity {
    com.leon.receipt_receivables.databinding.ActivityPayBinding binding;
    Activity activity;
    VosoolLoad vosoolLoad;
    ArrayList<DetailsAdapter.DetailsItem> detailsItems = new ArrayList<>();
    DetailsAdapter detailsAdapter;
    private boolean detail = true, map = true;
    GPSTracker gpsTracker;
    double x, y, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        binding = com.leon.receipt_receivables.databinding.ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        gpsTracker = new GPSTracker(activity);
        if (getIntent() != null) {
            String vosool = getIntent().getExtras().getString(BundleEnum.RESULT.getValue());
            latitude = getIntent().getExtras().getDouble(BundleEnum.Y.getValue());
            longitude = getIntent().getExtras().getDouble(BundleEnum.X.getValue());
            Gson gson = new Gson();
            vosoolLoad = gson.fromJson(vosool, VosoolLoad.class);
            setupRecyclerView();
        }
        getXY(vosoolLoad.billId/*"1040475216313"*/);
        initialize();
    }


    void initialize() {
        initializeMap();
        binding.textViewBillId.setText(vosoolLoad.billId);
        binding.textViewPaymentId.setText(vosoolLoad.vosoolBills.get(vosoolLoad.vosoolBills.size() - 1).payId);

        onButtonsClickListener();
    }

    void setupRecyclerView() {
        for (VosoolBill vosoolBill : vosoolLoad.vosoolBills) {
            detailsItems.add(new DetailsAdapter.DetailsItem(vosoolBill.amount, vosoolBill.dateBed));
        }
        detailsAdapter = new DetailsAdapter(detailsItems);
        binding.recyclerViewDetails.setAdapter(detailsAdapter);
        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(activity));
    }

    void onButtonsClickListener() {
        binding.buttonDetails.setOnClickListener(v -> {
            binding.recyclerViewDetails.setVisibility(detail ? View.VISIBLE : View.GONE);
            binding.mapView.setVisibility(View.GONE);
            detail = !detail;
            map = true;
        });
        binding.buttonMap.setOnClickListener(v -> {
            binding.mapView.setVisibility(map ? View.VISIBLE : View.GONE);
            binding.recyclerViewDetails.setVisibility(View.GONE);
            map = !map;
            detail = true;
        });
        binding.buttonStatus.setOnClickListener(v -> {
            String paymentId = binding.textViewPaymentId.getText().toString();
            String billId = binding.textViewBillId.getText().toString();
            startActivity(
                    ResultCustomActivity.putIntentResult(PayActivity.this, vosoolLoad.id,
                            billId, paymentId, x, y));
            finish();
        });
        binding.buttonBillPayment.setOnClickListener(v -> {
            if (isNetworkAvailable(activity)) {
                String paymentId = binding.textViewPaymentId.getText().toString();
                String billId = binding.textViewBillId.getText().toString();
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
                                        vosoolLoad.id,billId, paymentId, maskedPan, "terminalNo",
                                        "merchantId", "traceNumber",
                                        "rrn", "ref", "amount", "txnDate", "txnTime", statusDescription, x, y, false));
                        finish();
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
                                vosoolLoad.id,billId, paymentId, maskedPan, terminalNo, merchantId, traceNumber,
                                rrn, ref, amount, txnDate, txnTime, "description", x, y, true));
                        finish();
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
                                vosoolLoad.id,billId, paymentId, maskedPan, terminalNo, merchantId, traceNumber,
                                rrn, ref, amount, txnDate, txnTime, errorDescription, x, y, false));
                        finish();
                    }

                    @Override
                    public void onPaymentCancelled(String billId1, String paymentId,
                                                   String maskedPan, String panHash) {
                        startActivity(ResultActivity.putIntentResult(ResultActivity.putIntent(PayActivity.this,
                                String.format(Locale.ENGLISH, "فرایند پرداخت قبض با شناسه قبض %s و شناسه پرداخت %s توسط کاربر لغو شد.", billId, paymentId)),
                                vosoolLoad.id,billId, paymentId, maskedPan, "terminalNo",
                                "merchantId", "traceNumber", "rrn",
                                "ref", "amount", "txnDate", "txnTime", "description", x, y, false));
                        finish();
                    }
                });
            } else {
                new CustomToast().warning(getString(R.string.turn_internet_on));
            }
        });

    }

    @SuppressLint("MissingPermission")
    void initializeMap() {
        binding.mapView.getZoomController().
                setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        binding.mapView.setMultiTouchControls(true);
        IMapController mapController = binding.mapView.getController();
        mapController.setZoom(15.5);

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        if (latitude == 0 || longitude == 0) {
            startPoint = new GeoPoint(x, y);
        }
        mapController.setCenter(startPoint);

        addPlace(startPoint, true);

//        MyLocationNewOverlay locationOverlay =
//                new MyLocationNewOverlay(new GpsMyLocationProvider(activity), binding.mapView);
//        MyLocationNewOverlay locationOverlay =
//                new MyLocationNewOverlay(binding.mapView);
//        locationOverlay.enableFollowLocation();
//
//        Drawable currentDraw = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
//        Bitmap currentIcon = null;
//        if (currentDraw != null) {
//            currentIcon = ((BitmapDrawable) currentDraw).getBitmap();
//        }
//        locationOverlay.setPersonIcon(currentIcon);
//
//        locationOverlay.enableMyLocation();
//        binding.mapView.getOverlays().add(locationOverlay);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void addPlace(GeoPoint p, boolean currentLocation) {
        GeoPoint startPoint = new GeoPoint(p.getLatitude(), p.getLongitude());
        Marker startMarker = new Marker(binding.mapView);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        if (currentLocation)
            startMarker.setIcon(getResources().getDrawable(R.drawable.osm_ic_follow_me));
        binding.mapView.getOverlayManager().add(startMarker);
    }

    void getXY(String billId) {
        Retrofit retrofit = NetworkHelper.getInstance(false);
        IAbfaService iAbfaService = retrofit.create(IAbfaService.class);
        Call<CustomPlace> call = iAbfaService.getXY(billId);
        HttpClientWrapper.callHttpAsync(call, ProgressType.NOT_SHOW.getValue(), activity,
                new GetXY(), new GetXYIncomplete(), new GetError());
    }

    class GetXY implements ICallback<CustomPlace> {
        @Override
        public void execute(Response<CustomPlace> response) {
            if (response.body() != null && response.body().X != 0 && response.body().Y != 0) {
                String utm = "39 S ".concat(String.valueOf(response.body().X)).concat(" ")
                        .concat(String.valueOf(response.body().Y));
                CoordinateConversion conversion = new CoordinateConversion();
                double[] latLong = conversion.utm2LatLon(utm);
                x = latLong[0];
                y = latLong[1];
                if (x != 0 && y != 0) {
                    binding.buttonMap.setVisibility(View.VISIBLE);
                    addPlace(new GeoPoint(latLong[0], latLong[1]), false);
                }
            }
        }
    }

    static class GetXYIncomplete implements ICallbackIncomplete<CustomPlace> {
        @Override
        public void executeIncomplete(Response<CustomPlace> response) {
            Log.e("Error", response.toString());
        }
    }

    static class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            Log.e("Error", t.getMessage());
        }
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