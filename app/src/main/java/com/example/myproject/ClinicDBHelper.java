package com.example.myproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ClinicDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myData.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PATIENT =
            "create table patient (" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
                    "gender text,"+
                    "age int," +
                    "phonenumber text ," +
                    "addDate int);";
    private static final String CREATE_TABLE_APPOINTMENT =
            "create table appointment (" +
                    "id integer primary key autoincrement," +
                    "patientID int not null," +
                    "Date_time int not null," +
                    "patientCase text," +
                    "discount NUMERIC(4,2),"+
                    "price NUMERIC(10,2),"+
                    "duration int," +
                    "FOREIGN KEY (patientID) REFERENCES patient(id));";

    public ClinicDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ClinicDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PATIENT);
        db.execSQL(CREATE_TABLE_APPOINTMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClinicDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion +
                        "to " + newVersion + "which will destroy all old data");
        db.execSQL("Alter table patient add  patientphoto blob;");

    }

}
