package com.leon.receipt_receivables.tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"customId"}, unique = true))
public class VosoolOffloadDto {
    @PrimaryKey(autoGenerate = true)
    public int customId;
    public String id;
    public int resultId;
    public String description;
    public String bankTrackNumber;
    public String bankRRN;
    public String cartNumber;
    public String posSerial;
    public String posTerminal;
    public boolean isPaySuccess;
    public boolean isSent;
    public String posPayDate;
    public String posBillId;
    public String posPayId;
    public double x1;
    public double y1;
    public double accuracy;
    public double x2;
    public double y2;
    @Ignore
    public String name;
    @Ignore
    public long amount;
}
