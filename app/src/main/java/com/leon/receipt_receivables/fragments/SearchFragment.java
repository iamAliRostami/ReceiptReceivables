package com.leon.receipt_receivables.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.leon.receipt_receivables.activities.ReadingActivity;
import com.leon.receipt_receivables.databinding.FragmentSearchBinding;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SearchFragment extends DialogFragment {
    FragmentSearchBinding binding;
    Activity activity;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        activity = getActivity();
        initialize();
        return binding.getRoot();
    }

    void initialize() {
        binding.buttonSearch.setOnClickListener(v -> {
            ((ReadingActivity) activity).search(binding.editTextName.getText().toString(),
                    binding.editTextBillId.getText().toString(),
                    binding.editTextRadif.getText().toString(),
                    binding.editTextTrackNumber.getText().toString(),
                    binding.editTextMobile.getText().toString(),
                    binding.textViewLastPay.getText().toString(),
                    binding.editTextAddress.getText().toString());
            dismiss();
        });
        binding.textViewLastPay.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
            datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
            datePickerDialog.setDisableDaysAgo(false);
            datePickerDialog.setTextSizeTitle(10.0f);
            datePickerDialog.setTextSizeWeek(12.0f);
            datePickerDialog.setTextSizeDate(14.0f);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.setOnSingleDateSelectedListener(date ->
                    binding.textViewLastPay.setText(date.getPersianShortDate()));
            datePickerDialog.showDialog();
        });
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
        super.onResume();
    }
}