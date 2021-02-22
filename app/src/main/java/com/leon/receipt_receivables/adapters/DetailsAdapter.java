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

public class DetailsAdapter extends
        RecyclerView.Adapter<DetailsAdapter.DetailsItemHolder> {
    private final ArrayList<DetailsItem> detailsItemsTemp;
    private final ArrayList<DetailsItem> detailsItems;

    public DetailsAdapter(ArrayList<DetailsItem> listItems) {
        this.detailsItemsTemp = new ArrayList<>(listItems);
        this.detailsItems = new ArrayList<>(listItems);
    }

    @NonNull
    @Override
    public DetailsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        if (viewType % 2 == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_details_1, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_details_2, parent, false);
        }
        return new DetailsItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsItemHolder holder, int position) {
        DetailsItem detailsItem = this.detailsItemsTemp.get(position);
        holder.textViewDebt.setText(String.valueOf(detailsItem.debt));
        holder.textViewLastPayDate.setText(detailsItem.lastPayDate);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return detailsItemsTemp.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public static class DetailsItem {
        long debt;
        String lastPayDate;

        public DetailsItem(long debt, String lastPayDate) {
            this.debt = debt;
            this.lastPayDate = lastPayDate;
        }
    }

    static class DetailsItemHolder extends RecyclerView.ViewHolder {
        TextView textViewDebt;
        TextView textViewLastPayDate;

        public DetailsItemHolder(View viewItem) {
            super(viewItem);
            this.textViewDebt = viewItem.findViewById(R.id.text_view_debt);
            this.textViewLastPayDate = viewItem.findViewById(R.id.text_view_last_pay_date);
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
