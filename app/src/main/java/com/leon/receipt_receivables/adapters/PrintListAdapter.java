package com.leon.receipt_receivables.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.tables.PrintModel;

import java.util.ArrayList;

public class PrintListAdapter extends BaseAdapter {
    private final ArrayList<PrintModel> printModels;
    private final Context context;

    public PrintListAdapter(Context context, ArrayList<PrintModel> printModels) {
        this.printModels = printModels;
        printModels.add(new PrintModel(""));
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PrintModel printModel = printModels.get(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;
        convertView = mInflater.inflate(R.layout.item_print, parent, false);
        holder = new ViewHolder();
        holder.name = convertView.findViewById(R.id.text_view_name);
        holder.name.setText(printModel.getDescription());
        if (position + 2 == printModels.size()) {
            holder.description = convertView.findViewById(R.id.text_view_description);
            holder.description.setVisibility(View.VISIBLE);
        }
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public int getCount() {
        return printModels.size();
    }

    @Override
    public Object getItem(int position) {
        return printModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
//    @Override
//    public long getItemId(int position) {
//        return printModels.indexOf(getItem(position));
//    }

    private static class ViewHolder {
        private TextView name, description;
    }
}

