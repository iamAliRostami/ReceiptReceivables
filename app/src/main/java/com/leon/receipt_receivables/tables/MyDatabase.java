package com.leon.receipt_receivables.tables;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {SavedLocation.class, VosoolBill.class, ResultDictionary.class,
        KarbariDictionary.class, VosoolLoad.class},
        version = 2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("Alter TABLE \"VosoolLoad\" Add column  isSent Integer;");
        }
    };

    public abstract SavedLocationsDao savedLocationDao();

    public abstract KarbariDictionaryDao karbariDictionaryDao();

    public abstract ResultDictionaryDao resultDictionaryDao();

    public abstract VosoolBillDao vosoolBillDao();

    public abstract VosoolLoadDao vosoolLoadDao();

}

