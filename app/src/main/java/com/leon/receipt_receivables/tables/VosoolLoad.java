package com.leon.receipt_receivables.tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(indices = @Index(value = {"customId"}, unique = true))
public class VosoolLoad {
    @PrimaryKey(autoGenerate = true)
    public int customId;

    public String trackNumber;
    public int radif;
    public int zoneId;
    public String zoneTitle;
    public String billId;
    public String fullName;
    public int karbariId;
    public String address;
    public String mobile;
    public int tedadMaskooni;
    public int tedadTejari;
    public int tedadSaier;
    public int payable;
    public int owePeriods;
    public int oweDays;
    public String lastPayDate;
    public String lastPayDateBank;
    public String lastPayDateSabt;
    public int lastPayAmount;

    public int result;
    public double gisAccuracy;
    public double x;
    public double y;

    public boolean isPayed;
    public boolean isSent;
    public boolean isArchive;

    @Ignore
    public ArrayList<VosoolBill> vosoolBills;
}
