package com.leon.receipt_receivables.tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultDictionaryDao {

    @Query("Select * From ResultDictionary")
    List<ResultDictionary> getAllResultDictionary();

    @Insert
    void insertResultDictionary(ResultDictionary resultDictionary);

    @Insert
    void insertAllResultDictionary(List<ResultDictionary> resultDictionary);
}
