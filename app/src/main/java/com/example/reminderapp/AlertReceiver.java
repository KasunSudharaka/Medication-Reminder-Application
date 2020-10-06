package com.example.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

public class AlertReceiver extends BroadcastReceiver {


    private static final String filename="alarmID.txt";

    Context contexxt;
    String textin;

    String alarmId;
    String newalarmID;

    DatabaseHelper dbHelper;

    String medname;
    String note;

    @Override
    public void onReceive(Context context, Intent intent) {

        contexxt=context;

        dbHelper=new DatabaseHelper(context);


//        load();


//        String medname=intent.getStringExtra ("medName");
//        String note=intent.getExtras().getString("note");
//        String alarmId=intent.getExtras().getString("newAlarmID");


//        Intent resultIntent = new Intent(context, AddMedInfo.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        Bundle b = intent.getExtras();


        newalarmID = intent.getExtras().getString("alarmID");
        System.out.println("NEW ALARM IDDDDD"+newalarmID);

        getSharedPreferences();
        getInfoforNoti();
//        System.out.println("NOTEEEE"+note);

        System.out.println("ALARMIDD"+alarmId);

        NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getChannel1Notification("It's Time to take your meds :)","Did you take your Medication : "+medname+"?");
        notificationHelper.getManager().notify(1,nb.build());

    }



    public void getSharedPreferences(){


        SharedPreferences prefs= contexxt.getSharedPreferences("aName", MODE_PRIVATE);
        //save the value

        alarmId=prefs.getString("alarmID", "default");



    }

    public void getInfoforNoti(){



        Cursor cursor= dbHelper.getMedNameByID(newalarmID);


        if (cursor.getCount()==0){

            Toast.makeText(contexxt, "No Meds For This Date", Toast.LENGTH_SHORT).show();


        }

        else {

            while (cursor.moveToNext()){

                medname=cursor.getString(0);


                //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();



            }
        }

    }


//    public void load(){
//
//
//        FileInputStream fis=null;
//
//        try {
//            fis=contexxt.openFileInput(filename);
//
//            InputStreamReader isr=new InputStreamReader(fis);
//            BufferedReader br=new BufferedReader(isr);
//            StringBuilder sb=new StringBuilder();
//
//
//
//            while((textin=br.readLine())!=null){
//
//                sb.append(textin).append("\n");
//
//                alarmId=textin;
//                Toast.makeText(contexxt, "FILEEE"+textin, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//
//
//        catch (FileNotFoundException e)
//
//        {
//            e.printStackTrace();
//        }
//
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        finally {
//            if (fis!=null){
//
//                try {
//                    fis.close();
//                }
//
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
