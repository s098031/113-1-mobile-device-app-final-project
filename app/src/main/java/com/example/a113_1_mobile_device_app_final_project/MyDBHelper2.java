package com.example.a113_1_mobile_device_app_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper2 extends SQLiteOpenHelper {
    private static final String name = "mdatabase2.db";
    private static final int version =1;
    MyDBHelper2(Context context){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db2)
    {
        db2.execSQL("CREATE TABLE myTable(_id INTEGER PRIMARY KEY AUTOINCREMENT, item text NOT NULL, price integer NOT NULL, date text NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL("DROP TABLE IF EXISTS myTable");
        onCreate(db2);
    }
}