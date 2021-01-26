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
        this.context = context;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        convertView = mInflater.inflate(R.layout.item_print, parent, false);
        holder = new ViewHolder();
        holder.description = convertView.findViewById(R.id.text_view_description);
        holder.name = convertView.findViewById(R.id.text_view_name);

        holder.description.setText(printModels.get(position).getDescription());
        holder.name.setText(printModels.get(position).getName());
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
        return printModels.indexOf(getItem(position));
    }

    private static class ViewHolder {
        private TextView name, description;
    }
}

