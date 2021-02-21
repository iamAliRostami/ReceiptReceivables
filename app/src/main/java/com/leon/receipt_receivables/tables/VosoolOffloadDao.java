package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VosoolOffloadDao {

    @Query("Select * From VosoolOffloadDto")
    List<VosoolOffloadDto> getAllVosoolOffloadDto();

    @Query("Select * From VosoolOffloadDto WHERE isSent = :isSent")
    List<VosoolOffloadDto> getVosoolOffloadDtoBySent(boolean isSent);

    @Insert
    void insertVosoolOffloadDto(VosoolOffloadDto vosoolOffloadDto);

    @Insert
    void insertVosoolOffloadDto(List<VosoolOffloadDto> vosoolOffloadDto);

    @Query("UPDATE VosoolOffloadDto SET isSent = :isSent WHERE posBillId = :billId")
    void updateVosoolOffloadDtoBySent(boolean isSent, String billId);

    @Query("UPDATE VosoolOffloadDto SET isSent = :isSent")
    void updateVosoolOffloadDtoBySent(boolean isSent);

    @Update
    void updateVosoolOffloadDtoBySent(VosoolOffloadDto vosoolOffloadDto);
}
