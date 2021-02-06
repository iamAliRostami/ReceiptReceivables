package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VosoolBillDao {

    @Query("Select * From VosoolBill")
    List<VosoolBill> getAllVosoolBill();

    @Insert
    void insertVosoolBill(VosoolBill vosoolBill);

    @Insert
    void insertAllVosoolBill(List<VosoolBill> vosoolBills);
}
