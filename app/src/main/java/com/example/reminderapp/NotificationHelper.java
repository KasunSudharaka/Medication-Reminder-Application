package com.example.reminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    private static final String channel1ID="channel1ID";
    private static final String channel1Name="channel 1";
    private static final String channel2ID="channel2ID";
    private static final String channel2Name="channel 2";


    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannels();
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel1=new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);

        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2=new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_DEFAULT);

        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel2);
    }



    public NotificationManager getManager()
    {

        if (mManager==null){

            mManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }




    public NotificationCompat.Builder getChannel1Notification(String title, String message){


        Intent resultIntent=new Intent(this,MainActivity.class);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_take_med)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message){

        return new NotificationCompat.Builder(getApplicationContext(),channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_take_med);
    }

}
