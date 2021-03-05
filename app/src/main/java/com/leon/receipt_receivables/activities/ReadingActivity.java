package com.leon.receipt_receivables.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Debug;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.ReadingAdapter;
import com.leon.receipt_receivables.adapters.SpinnerCustomAdapter;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityReadingBinding;
import com.leon.receipt_receivables.enums.BundleEnum;
import com.leon.receipt_receivables.fragments.SearchFragment;
import com.leon.receipt_receivables.tables.KarbariDictionary;
import com.leon.receipt_receivables.tables.MyDatabase;
import com.leon.receipt_receivables.tables.MyDatabaseClient;
import com.leon.receipt_receivables.tables.ResultDictionary;
import com.leon.receipt_receivables.tables.VosoolBill;
import com.leon.receipt_receivables.tables.VosoolLoad;
import com.leon.receipt_receivables.utils.CustomProgressBar;
import com.leon.receipt_receivables.utils.CustomToast;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadingActivity extends BaseActivity {
    ActivityReadingBinding binding;
    ArrayList<ReadingAdapter.ReadingItem> readingItems = new ArrayList<>();
    ArrayList<KarbariDictionary> karbariDictionaries = new ArrayList<>();
    ArrayList<ResultDictionary> resultDictionaries = new ArrayList<>();
    ArrayList<VosoolBill> vosoolBills = new ArrayList<>();
    ArrayList<VosoolLoad> vosoolLoads = new ArrayList<>();
    ReadingAdapter readingAdapter;
    Activity activity;
    ArrayList<String> items = new ArrayList<>(Arrays.asList("نام", "بدهی", "تاریخ"));
    boolean ascend = true;
    int type = 0;

    @Override
    protected void initialize() {
        binding = ActivityReadingBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        activity = this;
        new GetDB().execute();
        setOnImageViewSortClickListener();
        setOnImageViewSearchClickListener();
    }

    void setOnImageViewSortClickListener() {
        binding.imageViewSort.setOnClickListener(v -> {
            binding.imageViewSort.setImageDrawable(
                    ContextCompat.getDrawable(activity,
                            ascend ? R.drawable.img_sort_ascend :
                                    R.drawable.img_sort_descend));
            ascend = !ascend;
            readingAdapter.sort(type, ascend);
            readingAdapter.notifyDataSetChanged();
        });
    }

    void setOnImageViewSearchClickListener() {
        binding.imageViewSearch.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            SearchFragment searchFragment = SearchFragment.newInstance();
            searchFragment.show(fragmentTransaction, "");
        });
    }

    void setupSpinner() {
        SpinnerCustomAdapter adapter = new SpinnerCustomAdapter(activity, items);
        binding.spinner.setAdapter(adapter);
        setOnSpinnerSelectedListener();
    }

    void setOnSpinnerSelectedListener() {
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                type = i;
                readingAdapter.sort(type, ascend);
                readingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void setupRecyclerView() {
        for (VosoolLoad vosoolLoad : vosoolLoads) {
            String karbari = "";
            for (KarbariDictionary karbariDictionary : karbariDictionaries)
                if (karbariDictionary.id == vosoolLoad.karbariId)
                    karbari = karbariDictionary.title;
            readingItems.add(new ReadingAdapter.ReadingItem(vosoolLoad.payable, vosoolLoad.fullName,
                    vosoolLoad.lastPayDate, vosoolLoad.mobile, vosoolLoad.address, vosoolLoad.radif,
                    vosoolLoad.billId, vosoolLoad.trackNumber, karbari, vosoolLoad.isSent));
        }
        if (readingItems.isEmpty()) {
            binding.linearLayoutList.setVisibility(View.GONE);
            binding.linearLayoutEmpty.setVisibility(View.VISIBLE);
        } else {
            setupSpinner();
            readingAdapter = new ReadingAdapter(readingItems);
            binding.recyclerView.setAdapter(readingAdapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerViewItemClickListener();
        }
    }

    void recyclerViewItemClickListener() {
        binding.recyclerView.addOnItemTouchListener(
                new ReadingAdapter.RecyclerItemClickListener(activity, binding.recyclerView,
                        new ReadingAdapter.RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(activity, PayActivity.class);
                                intent.putExtra(BundleEnum.Y.getValue(), BaseActivity.getGpsTracker().getLatitude());
                                intent.putExtra(BundleEnum.X.getValue(), BaseActivity.getGpsTracker().getLongitude());
                                Gson gson = new Gson();
                                boolean isPayed = true, isSent = false;
                                String vosool = null;
                                for (VosoolLoad vosoolLoad : vosoolLoads) {
                                    if (vosoolLoad.billId.equals(
                                            readingAdapter.getReading(position).billId)) {
                                        if (!vosoolLoad.isPayed) {
                                            vosool = gson.toJson(vosoolLoad);
                                            isPayed = false;
                                        }
                                        if (vosoolLoad.isSent)
                                            isSent = true;
                                    }
                                }
                                if (isSent) {
                                    new CustomToast().info("اطلاعات این اشتراک بارگذاری شده است.", Toast.LENGTH_LONG);
                                }
                                if (isPayed) {
                                    new CustomToast().warning("بدهی این اشتراک پرداخت شده است.", Toast.LENGTH_SHORT);
                                } else {
                                    intent.putExtra(BundleEnum.RESULT.getValue(), vosool);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
    }

    public void search(int debt, String name, String billId, String Radif, String mobile,
                       String lastDatePay, String address) {
        readingAdapter.search(debt, name, billId, Radif, mobile, lastDatePay, address);
    }

    @SuppressLint("StaticFieldLeak")
    class GetDB extends AsyncTask<Integer, Integer, Integer> {
        CustomProgressBar customProgressBar;

        public GetDB() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar = new CustomProgressBar();
            customProgressBar.show(activity, false);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            customProgressBar.getDialog().dismiss();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            MyDatabase myDatabase = MyDatabaseClient.getInstance(activity).getMyDatabase();
            karbariDictionaries.addAll(myDatabase.karbariDictionaryDao().getAllKarbariDictionary());
            resultDictionaries.addAll(myDatabase.resultDictionaryDao().getAllResultDictionary());
            vosoolLoads.addAll(myDatabase.vosoolLoadDao().getVosoolLoadByArchive(false));
            vosoolBills.addAll(myDatabase.vosoolBillDao().getAllVosoolBill());

            for (int i = 0; i < vosoolLoads.size(); i++) {
                vosoolLoads.get(i).vosoolBills = new ArrayList<>();
                for (int j = 0; j < vosoolBills.size(); j++) {
                    if (vosoolBills.get(j).billId.equals(vosoolLoads.get(i).billId))
                        vosoolLoads.get(i).vosoolBills.add(vosoolBills.get(j));
                }
            }
            runOnUiThread(ReadingActivity.this::setupRecyclerView);
            return null;
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
        readingItems = null;
        karbariDictionaries = null;
        resultDictionaries = null;
        vosoolBills = null;
        vosoolLoads = null;
        readingAdapter = null;
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