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
    private final ArrayList<ReadingItem> readingItemsTemp;
    private final ArrayList<ReadingItem> readingItems;

    public ReadingAdapter(ArrayList<ReadingItem> listItems) {
        this.readingItemsTemp = new ArrayList<>(listItems);
        this.readingItems = new ArrayList<>(listItems);
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
                    Collections.sort(readingItemsTemp, (o1, o2) -> o1.lastPayDate.compareTo(
                            o2.lastPayDate));
                else Collections.sort(readingItemsTemp, (o2, o1) -> o1.lastPayDate.compareTo(
                        o2.lastPayDate));
                break;
        }

    }

    public void search(int debt, String name, String billId, String radif,
                       String mobile, String lastDatePay, String address) {
        ArrayList<ReadingItem> readingItemsSearch = new ArrayList<>(readingItems);
        readingItemsTemp.clear();
        if (name.length() == 0)
            readingItemsTemp.addAll(readingItemsSearch);
        else {
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.name != null && readingItem.name.contains(name)))) {
                    readingItemsTemp.remove(readingItem);
                    readingItemsTemp.add(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }

        if (lastDatePay.length() > 0) {
            lastDatePay = lastDatePay.substring(2);
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.lastPayDate != null && readingItem.lastPayDate.compareTo(lastDatePay) > 0))) {
                    readingItemsTemp.remove(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }

        if (billId.length() > 0) {
            readingItemsTemp.clear();
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.billId != null && readingItem.billId.contains(billId)))) {
                    readingItemsTemp.remove(readingItem);
                    readingItemsTemp.add(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }

        if (radif.length() > 0) {
            readingItemsTemp.clear();
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.radif != null && readingItem.radif.contains(radif)))) {
                    readingItemsTemp.remove(readingItem);
                    readingItemsTemp.add(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }

        if (mobile.length() > 0) {
            readingItemsTemp.clear();
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.mobile != null && readingItem.mobile.contains(mobile)))) {
                    readingItemsTemp.remove(readingItem);
                    readingItemsTemp.add(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }
        if (address.length() > 0) {
            readingItemsTemp.clear();
            for (ReadingItem readingItem : readingItemsSearch) {
                if (((readingItem.address != null && readingItem.address.contains(address)))) {
                    readingItemsTemp.remove(readingItem);
                    readingItemsTemp.add(readingItem);
                }
            }
            readingItemsSearch.clear();
            readingItemsSearch.addAll(readingItemsTemp);
        }
        for (ReadingItem readingItem : readingItemsSearch) {
            if (((readingItem.debt < debt))) {
                readingItemsTemp.remove(readingItem);
            }
        }
        notifyDataSetChanged();
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
        holder.textViewDebt.setText(String.valueOf(readingItem.debt));
        holder.textViewName.setText(readingItem.name);
        holder.textViewLastPayDate.setText(readingItem.lastPayDate);

        holder.textViewMobile.setText(readingItem.mobile);
        holder.textViewAddress.setText(readingItem.address);
        holder.textViewBillId.setText(readingItem.billId);
        holder.textViewTrackNumber.setText(readingItem.trackNumber);
        holder.textViewRadif.setText(readingItem.radif);
        holder.textViewKarbari.setText(readingItem.karbari);
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

    public String billId(int position) {
        return readingItemsTemp.get(position).billId;
    }

    public static class ReadingItem {
        int debt;
        String name;
        String lastPayDate;
        String mobile;
        String address;
        String radif;
        String billId;
        String trackNumber;
        String karbari;

        public ReadingItem(int debt, String name, String lastPayDate, String mobile, String address,
                           int radif, String billId, String trackNumber, String karbari) {
            this.debt = debt;
            this.name = name;
            this.lastPayDate = lastPayDate;
            this.mobile = mobile;
            this.address = address;
            this.radif = String.valueOf(radif);
            this.billId = billId;
            this.trackNumber = trackNumber;
            this.karbari = karbari;
        }
    }

    static class ReadingItemHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDebt;
        TextView textViewLastPayDate;
        TextView textViewAddress;
        TextView textViewMobile;
        TextView textViewTrackNumber;
        TextView textViewRadif;
        TextView textViewBillId;
        TextView textViewKarbari;

        public ReadingItemHolder(View viewItem) {
            super(viewItem);
            this.textViewName = viewItem.findViewById(R.id.text_view_name);
            this.textViewDebt = viewItem.findViewById(R.id.text_view_debt);
            this.textViewLastPayDate = viewItem.findViewById(R.id.text_view_last_pay_date);


            this.textViewMobile = viewItem.findViewById(R.id.text_view_mobile);
            this.textViewAddress = viewItem.findViewById(R.id.text_view_address);
            this.textViewTrackNumber = viewItem.findViewById(R.id.text_view_track_number);
            this.textViewRadif = viewItem.findViewById(R.id.text_view_radif);
            this.textViewBillId = viewItem.findViewById(R.id.text_view_bill_id);
            this.textViewKarbari = viewItem.findViewById(R.id.text_view_Karbari);
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
