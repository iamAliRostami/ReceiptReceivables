package com.leon.receipt_receivables.adapters;

import android.content.Context;
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
    private final ArrayList<ReadingItem> readingItem;

    public ReadingAdapter(ArrayList<ReadingItem> listItems) {
        this.readingItem = listItems;
    }

    public void sort(int type, boolean ascend) {
//        Collections.sort(readingItem, (o1, o2) -> o1.debt.compareTo(
//                o2.debt));
        switch (type) {
            case 1:
                if (ascend)
                    Collections.sort(readingItem, (o1, o2) -> o1.ItemName.compareTo(
                            o2.ItemName));
                else Collections.sort(readingItem, (o2, o1) -> o1.ItemName.compareTo(
                        o2.ItemName));
                break;
            case 2:
                if (ascend)
                    Collections.sort(readingItem, (p1, p2) -> p1.debt - p2.debt);
                else
                    Collections.sort(readingItem, (p2, p1) -> p1.debt - p2.debt);
        }

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
        ReadingItem readingItem = this.readingItem.get(position);
        holder.textViewTitle.setText(readingItem.ItemName);
        holder.textViewDebt.setText(String.valueOf(readingItem.debt));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return readingItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public static class ReadingItem {
        String ItemName;
        int debt;

        public ReadingItem(String itemName, int debt) {
            this.ItemName = itemName;
            this.debt = debt;
        }
    }

    static class ReadingItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDebt;

        public ReadingItemHolder(View viewItem) {
            super(viewItem);
            this.textViewTitle = viewItem.findViewById(R.id.text_view_title);
            this.textViewDebt = viewItem.findViewById(R.id.text_view_debt);
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
