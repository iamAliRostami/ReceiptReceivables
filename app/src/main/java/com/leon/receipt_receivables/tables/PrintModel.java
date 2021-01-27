package com.leon.receipt_receivables.tables;

import android.graphics.drawable.BitmapDrawable;

public class PrintModel {
    String name,description;
    BitmapDrawable bitmapDrawable;

    public PrintModel(String description) {
        this.description = description;
    }

    public PrintModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public PrintModel(BitmapDrawable bitmapDrawable) {
        this.bitmapDrawable = bitmapDrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BitmapDrawable getBitmapDrawable() {
        return bitmapDrawable;
    }

    public void setBitmapDrawable(BitmapDrawable bitmapDrawable) {
        this.bitmapDrawable = bitmapDrawable;
    }
}
