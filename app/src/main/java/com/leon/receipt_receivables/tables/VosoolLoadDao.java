package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VosoolLoadDao {

    @Query("Select * From VosoolLoad")
    List<VosoolLoad> getAllVosoolLoad();

    @Insert
    void insertVosoolLoad(VosoolLoad vosoolLoad);

    @Insert
    void insertAllVosoolLoad(List<VosoolLoad> vosoolLoad);
}
