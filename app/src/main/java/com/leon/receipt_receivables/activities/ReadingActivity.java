package com.leon.receipt_receivables.activities;

import android.app.Activity;
import android.os.Debug;
import android.view.View;
import android.widget.AdapterView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.ReadingAdapter;
import com.leon.receipt_receivables.adapters.SpinnerCustomAdapter;
import com.leon.receipt_receivables.base_items.BaseActivity;
import com.leon.receipt_receivables.databinding.ActivityReadingBinding;
import com.leon.receipt_receivables.fragments.SearchFragment;
import com.leon.receipt_receivables.utils.CalendarTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ReadingActivity extends BaseActivity {
    ActivityReadingBinding binding;
    ArrayList<ReadingAdapter.ReadingItem> readingItems = new ArrayList<>();
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
        setupRecyclerView();
        setupSpinner();
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
        for (int i = 0; i < 100; i++) {
            Random r = new Random();
            long unixTime = (long) (+r.nextDouble() * 60 * 60 * 24 * 365);
            Date d = new Date(unixTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            CalendarTool calendarTool = new CalendarTool(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            readingItems.add(new ReadingAdapter.ReadingItem("name " + i,
                    new Random().nextInt(1000), calendarTool.getIranianDate()));

        }
        readingAdapter = new ReadingAdapter(readingItems);
        binding.recyclerView.setAdapter(readingAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewItemClickListener();
    }

    void recyclerViewItemClickListener() {
        binding.recyclerView.addOnItemTouchListener(
                new ReadingAdapter.RecyclerItemClickListener(activity, binding.recyclerView,
                        new ReadingAdapter.RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
    }

    public void search() {
        readingAdapter.search();
        readingAdapter.notifyDataSetChanged();
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