package com.example.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CancelationReceiver extends BroadcastReceiver {

    String cancelAlarmID;

    PendingIntent pendingIntent;

    Intent intentt;

    @Override
    public void onReceive(Context context, Intent intent) {

         intentt=intent;

//        cancelAlarmID = intent.getExtras().getString("alarmID");

//        System.out.println("CANCELATIONNNNNNN ALARM IDDDDD"+cancelAlarmID);

        pendingIntent=intent.getParcelableExtra("intent");

        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
        am.cancel(pendingIntent);




//
//        AlarmManager alarmManager=(AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        PendingIntent cancelIntent = PendingIntent.getBroadcast(context.getApplicationContext(), Integer.parseInt(cancelAlarmID), intentt, 0);

//        alarmManager.cancel(cancelIntent);


//
//        String alarmId= intent.getStringExtra("alarmId");
//         Intent cancellationIntent=new Intent(context,AddMedInfo.class);

//        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(cancelAlarmID), intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        am.cancel(cancellationPendingIntent);

        Toast.makeText(context, "ALARN CANCELEDDDDD", Toast.LENGTH_SHORT).show();


        System.out.println("ALARMMMMM CANCEKLEDDDDDDDDDDDDDDDDDDD");

    }
}
