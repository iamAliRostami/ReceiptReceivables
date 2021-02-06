package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface KarbariDictionaryDao {

    @Query("Select * From KarbariDictionary")
    List<KarbariDictionary> getAllKarbariDictionary();

    @Insert
    void insertKarbariDictionary(KarbariDictionary karbariDictionary);

    @Insert
    void insertKarbariDictionary(List<KarbariDictionary> karbariDictionary);
}
