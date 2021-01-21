package com.leon.receipt_receivables.base_items;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.leon.receipt_receivables.BuildConfig;
import com.leon.receipt_receivables.MyApplication;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.activities.DownloadActivity;
import com.leon.receipt_receivables.activities.HelpActivity;
import com.leon.receipt_receivables.activities.MainActivity;
import com.leon.receipt_receivables.activities.ReadingActivity;
import com.leon.receipt_receivables.activities.ReportActivity;
import com.leon.receipt_receivables.activities.UploadActivity;
import com.leon.receipt_receivables.adapters.NavigationDrawerAdapter;
import com.leon.receipt_receivables.databinding.BaseActivityBinding;
import com.leon.receipt_receivables.enums.SharedReferenceKeys;
import com.leon.receipt_receivables.enums.SharedReferenceNames;
import com.leon.receipt_receivables.infrastructure.ISharedPreferenceManager;
import com.leon.receipt_receivables.utils.CustomToast;
import com.leon.receipt_receivables.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationDrawerAdapter adapter;
    List<NavigationDrawerAdapter.DrawerItem> dataList;
    BaseActivityBinding binding;
    ISharedPreferenceManager sharedPreferenceManager;
    boolean exit = false;

    protected abstract void initialize();

    @SuppressLint({"NewApi", "RtlHardcoded", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext(),
                SharedReferenceNames.ACCOUNT.getValue());
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        binding = BaseActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeBase();
        initialize();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new CustomToast().info(getString(R.string.how_to_exit));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    void setOnDrawerItemClick() {
        initializeImageViewSwitch();
        binding.imageViewHeader.setOnClickListener(v -> {
            if (MyApplication.POSITION != -1) {
                MyApplication.POSITION = -1;
                Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else
                binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.recyclerView.addOnItemTouchListener(
                new NavigationDrawerAdapter.RecyclerItemClickListener(MyApplication.getContext(),
                        binding.recyclerView, new NavigationDrawerAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        if (position == 5) {
                            exit = true;
                            MyApplication.POSITION = -1;
                            finishAffinity();
                        } else if (MyApplication.POSITION != position) {
                            MyApplication.POSITION = position;
                            Intent intent = new Intent();
                            if (position == 0) {
                                intent = new Intent(getApplicationContext(), DownloadActivity.class);
                            } else if (position == 1) {
                                intent = new Intent(getApplicationContext(), ReadingActivity.class);
                            } else if (position == 2) {
                                intent = new Intent(getApplicationContext(), UploadActivity.class);
                            } else if (position == 3) {
                                intent = new Intent(getApplicationContext(), ReportActivity.class);
                            } else if (position == 4) {
                                intent = new Intent(getApplicationContext(), HelpActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void initializeImageViewSwitch() {
        if (sharedPreferenceManager.getBoolData(SharedReferenceKeys.THEME.getValue())) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        binding.imageViewSwitch.setOnClickListener(v -> {
            sharedPreferenceManager.putData(SharedReferenceKeys.THEME.getValue(),
                    !sharedPreferenceManager.getBoolData(SharedReferenceKeys.THEME.getValue()));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            if (sharedPreferenceManager.getBoolData(SharedReferenceKeys.THEME.getValue())) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initializeBase() {
        TextView textView = findViewById(R.id.text_view_title);
        textView.setText(sharedPreferenceManager.getStringData(
                SharedReferenceKeys.DISPLAY_NAME.getValue()).concat(" (").concat(
                sharedPreferenceManager.getStringData(
                        SharedReferenceKeys.USER_CODE.getValue())).concat(")"));
        binding.textViewVersion.setText(getString(R.string.version).concat(" ")
                .concat(BuildConfig.VERSION_NAME));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataList = new ArrayList<>();
        fillDrawerRecyclerView();
        setOnDrawerItemClick();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(view1 -> binding.drawerLayout.openDrawer(Gravity.START));
    }

    void fillDrawerRecyclerView() {
        dataList = NavigationDrawerAdapter.DrawerItem.createItemList(
                getResources().getStringArray(R.array.menu), getResources().obtainTypedArray(
                        R.array.icons));
        adapter = new NavigationDrawerAdapter(this, dataList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        binding.recyclerView.setNestedScrollingEnabled(true);
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
        if (exit)
            android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}