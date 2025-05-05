package com.example.login;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BDOpenHelper extends SQLiteOpenHelper {

    public static final String bdName="agricola.sqlite";
    public static final int bdversion=1;

    public BDOpenHelper( Context context ) {
        super(context, bdName, null, bdversion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
}
