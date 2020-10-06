package com.example.reminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper( Context context) {
        super(context, "MedicationReminderApplication.db", null, 1);

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("Create table MedInfo(AlarmId text ,Date text, MedName text, Notes text,ReminderTimes text,Schedule text,nop text)");

        sqLiteDatabase.execSQL("Create table TakesInfo(AlarmId text ,NoOfTakes text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists MedInfo");

        sqLiteDatabase.execSQL("drop table if exists TakesInfo");


    }


    //insert data to MedInfo

    public boolean insertToMedInfo(String s1, String s2){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("Date",s1);
        contentValues.put("MedName",s2);

        long result= sqLiteDatabase.insert("MedInfo",null,contentValues);

        if (result==-1){

            return false;
        }

        else {

            return true;
        }

    }

    //inserting data with the alarm ID


    public boolean insertDataWithAlarmID(String s1, String s2, String s3, String s4,String s5, String s6,String s7){


        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("AlarmId",s1);
        contentValues.put("Date",s2);
        contentValues.put("MedName",s3);
        contentValues.put("Notes",s4);
        contentValues.put("ReminderTimes",s5);
        contentValues.put("Schedule",s6);
        contentValues.put("nop",s7);




        long result=sqLiteDatabase.insert("MedInfo",null,contentValues);

        if (result==-1){

            return false;
        }

        else {

            return true;
        }

    }

    //retrieving all data with AlarmID

    public Cursor getDataWithAlarmId(String date){


        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from MedInfo where Date = " +date,null);
        return cursor;
    }


    //getAlarmIDs

    public Cursor getAlarmIDs(String date){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select AlarmId from MedInfo where Date = " +date,null);
        return cursor;
    }


    //get med name by ID

    public Cursor getMedNameByID(String alarmId){




        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select MedName from MedInfo where AlarmId =" +alarmId,null);
        return cursor;

    }

    //getting all data by AlarmID

    public Cursor getAllDataByID(String alarmId){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from MedInfo where AlarmId =" +alarmId,null);
        return cursor;


    }

    //deleting data by AlarmID

public Integer deleteDataByID(String alarmId){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete("MedInfo","AlarmId=?",new String[]{alarmId});

}


//saving no of takes



    public boolean insertTakesWithAlarmID(String s1, String s2){


        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("AlarmId",s1);
        contentValues.put("NoOfTakes",s2);


        long result1=sqLiteDatabase.insert("TakesInfo",null,contentValues);

        if (result1==-1){

            return false;
        }

        else {

            return true;
        }

    }



    //get med No of takes

    public Cursor getNoOfTakesById(String alarmId){




        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select NoOfTakes from TakesInfo where AlarmId =" +alarmId,null);
        return cursor;

    }


    //deleting NOT by AlarmID

    public Integer deleteNoOfTakesById(String alarmId){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete("TakesInfo","AlarmId=?",new String[]{alarmId});

    }


    //getMedName And note

    public Cursor getMedNameAndNote(String alarmID){


        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select MedName,Notes from MedInfo where AlarmId =" +alarmID,null);
        return cursor;

    }



    //retrieving all data


    public Cursor allData(String date){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from MedInfo where Date = " +date,null);
        return cursor;
    }


    public Cursor getMedName(String date){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select MedName from MedInfo where Date =" +date,null);
        return cursor;
    }


    //getting the dates from table

    public Cursor getDates(){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select Date from MedInfo",null);
        return cursor;
    }


    //getting medinfo for reference date

//    public Cursor getMedInfo(String date){
//
//        return
//    }
}
