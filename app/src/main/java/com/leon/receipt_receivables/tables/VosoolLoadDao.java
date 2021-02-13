package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VosoolLoadDao {

    @Query("Select * From VosoolLoad")
    List<VosoolLoad> getAllVosoolLoad();

    @Query("Select * From VosoolLoad WHERE isSent = :isSent")
    List<VosoolLoad> getSentVosoolLoad(boolean isSent);

    @Query("Select * From VosoolLoad WHERE isArchive = :isArchive")
    List<VosoolLoad> getVosoolLoadByArchive(boolean isArchive);

    @Insert
    void insertVosoolLoad(VosoolLoad vosoolLoad);

    @Query("SELECT COUNT(*) FROM VosoolLoad")
    int countTotalVossolLoad();

    @Query("SELECT COUNT(*) FROM VosoolLoad WHERE isSent = :isSent")
    int countSentVossolLoad(boolean isSent);

    @Query("SELECT COUNT(*) FROM VosoolLoad WHERE isPayed = :isPayed")
    int countPayedVossolLoad(boolean isPayed);

    @Insert
    void insertAllVosoolLoad(List<VosoolLoad> vosoolLoad);

    @Query("UPDATE VosoolLoad SET isPayed = :isPayed WHERE billId = :billId")
    void updateVosoolByPayed(boolean isPayed, String billId);

    @Query("UPDATE VosoolLoad SET isArchive = :isArchive WHERE billId = :billId")
    void updateVosoolByArchive(boolean isArchive, String billId);

    @Query("UPDATE VosoolLoad SET isArchive = :isArchive")
    void updateVosoolByArchive(boolean isArchive);
}
