package com.leon.receipt_receivables.tables;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {SavedLocation.class, VosoolBill.class, ResultDictionary.class,
        KarbariDictionary.class, VosoolLoad.class, VosoolOffloadDto.class},
        version = 3, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final Migration MIGRATION_1_2 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("Alter TABLE \"VosoolLoad\" Add column  isSent Integer;");
            database.execSQL("CREATE TABLE \"VosoolOffloadDto\" (\n" +
                    "\t\"resultId\"\tINTEGER,\n" +
                    "\t\"description\"\tTEXT,\n" +
                    "\t\"bankTrackNumber\"\tTEXT,\n" +
                    "\t\"bankRRN\"\tTEXT,\n" +
                    "\t\"cartNumber\"\tTEXT,\n" +
                    "\t\"posSerial\"\tTEXT,\n" +
                    "\t\"posTerminal\"\tTEXT,\n" +
                    "\t\"isPaySuccess\"\tINTEGER,\n" +
                    "\t\"isSent\"\tINTEGER,\n" +
                    "\t\"posPayDate\"\tTEXT,\n" +
                    "\t\"posBillId\"\tTEXT,\n" +
                    "\t\"posPayId\"\tTEXT,\n" +
                    "\t\"x1\"\tREAL,\n" +
                    "\t\"y1\"\tREAL,\n" +
                    "\t\"accuracy\"\tREAL,\n" +
                    "\t\"customId\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"x2\"\tREAL,\n" +
                    "\t\"y2\"\tREAL\n" +
                    ");");
        }
    };

    public abstract SavedLocationsDao savedLocationDao();

    public abstract KarbariDictionaryDao karbariDictionaryDao();

    public abstract ResultDictionaryDao resultDictionaryDao();

    public abstract VosoolBillDao vosoolBillDao();

    public abstract VosoolLoadDao vosoolLoadDao();

    public abstract VosoolOffloadDao vosoolOffloadDao();

}

