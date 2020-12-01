package com.leon.receipt_receivables.base_items;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.leon.receipt_receivables.BuildConfig;
import com.leon.receipt_receivables.MyApplication;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.activities.MainActivity;
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
    RecyclerView recyclerView;
    NavigationDrawerAdapter adapter;
    List<NavigationDrawerAdapter.DrawerItem> dataList;
    BaseActivityBinding binding;
    ISharedPreferenceManager sharedPreferenceManager;

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
        binding.imageViewHeader.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            if (MyApplication.POSITION != -1) {
                MyApplication.POSITION = -1;
                Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else
                binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        recyclerView.addOnItemTouchListener(
                new NavigationDrawerAdapter.RecyclerItemClickListener(MyApplication.getContext(),
                        recyclerView, new NavigationDrawerAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        if (position == 5) {
                            MyApplication.POSITION = -1;
                            finishAffinity();
                        } else if (MyApplication.POSITION != position) {
                            MyApplication.POSITION = position;
                            Intent intent = new Intent();
                            if (position == 0) {
//                                intent = new Intent(getApplicationContext(), DownloadActivity.class);
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
        recyclerView = binding.recyclerView;
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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        recyclerView.setNestedScrollingEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}