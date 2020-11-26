package com.leon.receipt_receivables.tables;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kishcore.sdk.hybrid.api.PrintableData;
import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.adapters.PrintListAdapter;
import com.leon.receipt_receivables.utils.print.Tools;

import java.util.ArrayList;

public class PrintableDataList implements PrintableData {
    private final ArrayList<PrintModel> items;

    public PrintableDataList(ArrayList<PrintModel> items) {
        this.items = items;
    }

    @SuppressLint("InflateParams")
    @Override
    public View toView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = null;
        if (inflater != null) {
            root = inflater.inflate(R.layout.list_printable_data, null);
        }

        ListView listView = root.findViewById(R.id.lv_test);
        listView.setAdapter(new PrintListAdapter(context, items));
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        listView.setLayoutParams(layoutParams);
        Tools.setListViewHeightBasedOnChildren(listView);
        return root;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
