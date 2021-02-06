package com.leon.receipt_receivables.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import com.leon.receipt_receivables.MyApplication;

public class MyDatabaseClient {

    private static MyDatabaseClient mInstance;
    private final MyDatabase myDatabase;

    private MyDatabaseClient(Context context) {
        myDatabase = Room.databaseBuilder(context, MyDatabase.class, MyApplication.getDBName())
                .allowMainThreadQueries().build();
    }

    public static synchronized MyDatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDatabaseClient(context);
        }
        return mInstance;
    }

    public static void migration(Context context) {
        Room.databaseBuilder(context, MyDatabase.class,
                MyApplication.getDBName()).
                fallbackToDestructiveMigration().
                addMigrations(MyDatabase.MIGRATION_1_2).
                allowMainThreadQueries().
                build();
    }

    public static void deleteAndReset(Context context) {
        SQLiteDatabase database;
        database = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(MyApplication.getDBName()), null);
        String deleteTable = "DELETE FROM " + MyApplication.getDBName();
        database.execSQL(deleteTable);
        String deleteSqLiteSequence = "DELETE FROM sqlite_sequence WHERE name = '" + MyApplication.getDBName() + "'";
        database.execSQL(deleteSqLiteSequence);
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
