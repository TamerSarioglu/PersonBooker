package com.tamersarioglu.personbooker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Veritabani extends SQLiteOpenHelper {

    public Veritabani(@Nullable Context context) {
        super(context, "kisiler.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"kisiler\" (\n\t\"kisi_id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n\t\"kisi_ad\"\tTEXT,\n\t\"kisi_tel\"\tTEXT\n);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kisiler");
        onCreate(db);
    }
}
