package com.leon.receipt_receivables.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"customId"}, unique = true))
public class ResultDictionary {
    @PrimaryKey(autoGenerate = true)
    public int customId;
    public int id;
    public String title;
    public boolean isSelected;
    public boolean isDisabled;
    public boolean hasSms;
}
