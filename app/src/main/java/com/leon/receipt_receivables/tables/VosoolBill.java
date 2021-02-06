package com.leon.receipt_receivables.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"customId"}, unique = true))
public class VosoolBill {
    @PrimaryKey(autoGenerate = true)
    public int customId;
    public String id;
    public String vosoolDetailId;
    public int amount;
    public String billId;
    public String payId;
    public String dateBed;
    public int masraf;
    public int modat;
    public double rate;
    public String vosoolDetail;
}
