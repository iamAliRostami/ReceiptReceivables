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

    //    @Query("SELECT COUNT(VosoolOffloadDto.posBillId) FROM VosoolOffloadDto")
    //    @Query("SELECT COUNT(1) FROM VosoolOffloadDto GROUP BY VosoolOffloadDto.posBillId HAVING COUNT(1) > 3")
//    List<Integer> getRepetitiveBillId();
    @Query("SELECT COUNT(1) FROM (SELECT COUNT(1) FROM VosoolOffloadDto GROUP BY VosoolOffloadDto.posBillId HAVING COUNT(1) > 2)")
    int getRepetitiveBillId();

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
