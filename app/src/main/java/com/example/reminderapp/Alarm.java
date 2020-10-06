package com.example.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        WakeLocker.acquire(context);


        Toast.makeText(context, "ALARM......", Toast.LENGTH_LONG).show();

        Vibrator v=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(3000);


        WakeLocker.release();

    }
}
