package com.leon.receipt_receivables.adapters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.receipt_receivables.R;

import java.util.ArrayList;
import java.util.Collections;

public class ReadingAdapter extends
        RecyclerView.Adapter<ReadingAdapter.ReadingItemHolder> {
    private final ArrayList<ReadingItem> readingItemsTemp;
    private final ArrayList<ReadingItem> readingItems;

    public ReadingAdapter(ArrayList<ReadingItem> listItems) {
        this.readingItemsTemp = listItems;
        this.readingItems = listItems;
    }

    public void sort(int type, boolean ascend) {
        switch (type) {
            case 0:
                if (ascend)
                    Collections.sort(readingItemsTemp, (o1, o2) -> o1.name.compareTo(
                            o2.name));
                else Collections.sort(readingItemsTemp, (o2, o1) -> o1.name.compareTo(
                        o2.name));
                break;
            case 1:
                if (ascend)
                    Collections.sort(readingItemsTemp, (p1, p2) -> p1.debt - p2.debt);
                else
                    Collections.sort(readingItemsTemp, (p2, p1) -> p1.debt - p2.debt);
                break;
            case 2:
                if (ascend)
                    Collections.sort(readingItemsTemp, (o1, o2) -> o1.date.compareTo(
                            o2.date));
                else Collections.sort(readingItemsTemp, (o2, o1) -> o1.date.compareTo(
                        o2.date));
                break;
        }

    }

    public void search(String name, String billId, String Radif, String trackNumber,
                       String mobile, String lastDatePay, String address) {
        Log.e("Here", "Search");
    }

    @NonNull
    @Override
    public ReadingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        if (viewType % 2 == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_reading_1, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_reading_2, parent, false);
        }
        return new ReadingItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingItemHolder holder, int position) {
        ReadingItem readingItem = this.readingItemsTemp.get(position);
        holder.textViewTitle.setText(readingItem.name);
        holder.textViewDebt.setText(String.valueOf(readingItem.debt));
        holder.textViewDate.setText(String.valueOf(readingItem.date));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return readingItemsTemp.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public static class ReadingItem {
        String name;
        int debt;
        String date;

        public ReadingItem(String name, int debt, String date) {
            this.name = name;
            this.debt = debt;
            this.date = date;
        }
    }

    static class ReadingItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDebt;
        TextView textViewDate;

        public ReadingItemHolder(View viewItem) {
            super(viewItem);
            this.textViewTitle = viewItem.findViewById(R.id.text_view_name);
            this.textViewDebt = viewItem.findViewById(R.id.text_view_debt);
            this.textViewDate = viewItem.findViewById(R.id.text_view_date);
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        final OnItemClickListener mListener;
        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,
                                         OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && mListener != null) {
                                mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onLongItemClick(View view, int position);
        }
    }
}
