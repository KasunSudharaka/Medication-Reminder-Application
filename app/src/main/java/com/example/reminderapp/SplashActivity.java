package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private static int Splash_time_out=2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    Intent homeIntent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();

            }



        },Splash_time_out);
    }
}
