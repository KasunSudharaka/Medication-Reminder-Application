package com.example.reminderapp.dataRecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.AddMedInfo;
import com.example.reminderapp.AlertReceiver;
import com.example.reminderapp.CancelationReceiver;
import com.example.reminderapp.DatabaseHelper;
import com.example.reminderapp.MainActivity;
import com.example.reminderapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView dummydata,mednametv,nooftakestv;

    private TextView newmednametv,remindertimestv,scheduletv,noptv,notestv;

private FloatingActionButton deleteReminderbtn,okayreminderbtn,takemedbtn;


DatabaseHelper dbHelper;

private String alarmId;


private String reminderTimesrt,schedulert;

private String checkString;

 StringBuffer alphabetic, num;

 int nom, noh;

    AlarmManager alarmManager;
    Intent intent;

    View vieww;

    private int takesCounter;


    MainActivity ma;

    public DataViewHolder( View itemView)



    {


        super(itemView);

        dummydata=(TextView) itemView.findViewById(R.id.dummydatatv);
        mednametv=(TextView) itemView.findViewById(R.id.mednametv);
        nooftakestv=(TextView) itemView.findViewById(R.id.takestv);


        itemView.setOnClickListener(this);

        ma=new MainActivity();


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(final View view) {



        alarmId=dummydata.getText().toString();

        dbHelper=new DatabaseHelper(view.getContext());

        alarmManager=(AlarmManager)view.getContext().getSystemService(Context.ALARM_SERVICE);
        intent=new Intent(view.getContext(),AlertReceiver.class);

        vieww=view;

        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        remindertimestv = (TextView) popupView.findViewById(R.id.remindertimestv);
        scheduletv = (TextView) popupView.findViewById(R.id.scheduletv);
        noptv = (TextView) popupView.findViewById(R.id.noptv);
        notestv = (TextView) popupView.findViewById(R.id.notestv);
        newmednametv = (TextView) popupView.findViewById(R.id.newmedNametv);


        deleteReminderbtn  = (FloatingActionButton)popupView.findViewById(R.id.deleteReminderbtn);
        okayreminderbtn  = (FloatingActionButton)popupView.findViewById(R.id.okayReminderBtn);
        takemedbtn  = (FloatingActionButton)popupView.findViewById(R.id.takemedbtn);


//        edPop.requestFocus();
//                edPop.setText(vedt.getText().toString());
        popupWindow.showAtLocation(popupView, Gravity.CENTER,5,5);

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
      popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.update();


        View container = popupWindow.getContentView().getRootView();
        if(container != null) {
            WindowManager wm = (WindowManager)view.getContext().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = 0.6f;
            if(wm != null) {
                wm.updateViewLayout(container, p);
            }
        }


        getAllData();


        deleteReminderbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                        vedt.setText(edPop.getText().toString());

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Reminder")
                        .setMessage("All the reminder data will be deleted. Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                AlarmManager alarmManager=(AlarmManager)view.getContext().getSystemService(Context.ALARM_SERVICE);
                                Intent intent=new Intent(view.getContext(), AlertReceiver.class);



                                try {

                                    Integer checkint = Integer.parseInt(alarmId);


                                    PendingIntent cancelIntent = PendingIntent.getBroadcast(view.getContext(), Integer.parseInt(alarmId), intent, 0);

                                    alarmManager.cancel(cancelIntent);

                                    System.out.println("Alarm deleted for AlarmiD" + alarmId);

                                    Integer delet = dbHelper.deleteDataByID(alarmId);

                                    Integer delet1 = dbHelper.deleteNoOfTakesById(alarmId);

//                                refreshNumberOfTakes();
//                                ((MainActivity)vieww.getContext()).getAlarmIDs();

                                    ((MainActivity) vieww.getContext()).getMedInfoRef();


                                    popupWindow.dismiss();


                                    Toast.makeText(view.getContext(), "Reminder details deleted successfully", Toast.LENGTH_SHORT).show();

                                }


                                catch (NumberFormatException e){

                                    e.printStackTrace();

                                    Toast.makeText(view.getContext(), "Delete failed!. Please refresh before trying again.", Toast.LENGTH_SHORT).show();
                                }



                            }})
                        .setNegativeButton(android.R.string.no, null).show();



            }
        });

        okayreminderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

            }
        });

        takemedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Take Meds")
                        .setMessage("You are about to take your meds. Your future reminders will be rescheduled according to the current time.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                updateAlarm();

                                increaseCounter();
//                                refreshNumberOfTakes();

//                                ((MainActivity)vieww.getContext()).getAlarmIDs();
//
//                                ((MainActivity)vieww.getContext()).getMedInfoRef();


                                popupWindow.dismiss();



                            }})
                        .setNegativeButton(android.R.string.cancel, null).show();



            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAllData() {


        try {
            Integer checkint = Integer.parseInt(alarmId);


            Cursor cursor = dbHelper.getAllDataByID(alarmId);


            if (cursor.getCount() == 0) {

                Toast.makeText(itemView.getContext(), "No Meds For This Date", Toast.LENGTH_SHORT).show();


            } else {

                while (cursor.moveToNext()) {

                    newmednametv.setText(cursor.getString(2));
                    remindertimestv.setText(cursor.getString(4));
                    scheduletv.setText(cursor.getString(5));
                    noptv.setText(cursor.getString(6));
                    notestv.setText(cursor.getString(3));


                    checkString = cursor.getString(4);
                    schedulert = cursor.getString(5);


                    splitString(checkString);

                    //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();


                }
            }

        }

        catch (NumberFormatException e){

            e.printStackTrace();
        }



    }

    //distinguish numerics and alphabetics from reminderinfo

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void splitString(String str)
    {
     alphabetic = new StringBuffer();
                num = new StringBuffer();

        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i))){

                num.append(str.charAt(i));

                nom=Integer.parseInt(num.toString());
                noh=Integer.parseInt(num.toString());


            }
            else if(Character.isAlphabetic(str.charAt(i))){

                alphabetic.append(str.charAt(i));
                reminderTimesrt=alphabetic.toString();

            }
        }

        System.out.println(reminderTimesrt);
        System.out.println(nom);
        System.out.println(noh);

    }



    //refresh no of takes
    public void refreshNumberOfTakes(){


        Cursor cursor4= dbHelper.getNoOfTakesById(alarmId);


        if (cursor4.getCount()==0){

            System.out.println("NOOOOOOOOOOOOOOOOOOOOOO" + takesCounter);

            Toast.makeText(itemView.getContext(), "You have never ttaken this med", Toast.LENGTH_SHORT).show();


        }

        else {

            while (cursor4.moveToNext()){



                takesCounter = Integer.parseInt(cursor4.getString(0));


                System.out.println("INCREASING METHOD COUNTER" + takesCounter);
                //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();


            }
    }

    }


    public void increaseCounter(){


        Cursor cursor4= dbHelper.getNoOfTakesById(alarmId);


        if (cursor4.getCount()==0){

            System.out.println("NOOOOOOOOOOOOOOOOOOOOOO" + takesCounter);

            Toast.makeText(itemView.getContext(), "You have never ttaken this med", Toast.LENGTH_SHORT).show();


        }

        else {

           while (cursor4.moveToNext()){



                    takesCounter = Integer.parseInt(cursor4.getString(0));


                    System.out.println("INCREASING METHOD COUNTER" + takesCounter);
                    //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();


            }
        }


        takesCounter++;
        System.out.println("COUNTERRR"+takesCounter);


        boolean insertNoOfTakes=dbHelper.insertTakesWithAlarmID(alarmId,Integer.toString(takesCounter));

        if (insertNoOfTakes==true){

            System.out.println("NO OF TAKES INSERTED");
        }


    }



    private void updateAlarm() {


        switch (reminderTimesrt){

            case "Once Per Day":



                if (schedulert.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        System.out.println("ALARM SETTTTTTTT");
                    }

                }


                if (schedulert.equals("Only One Day")) {


                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , pendingIntent1);

                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");

                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                System.out.println("ALARM DID NOT SETTTTTTTTTTTT");

                break;

            case "Twice Per Day":



                if (schedulert.equals("Continous Treatment")) {



                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , AlarmManager.INTERVAL_HALF_DAY, pendingIntent1);
                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();

                if (schedulert.equals("Only One Day")) {


                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2*60*1000, pendingIntent1);

                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");

                    }

                }



                break;

            case "3 Times Per Day":


                if (schedulert.equals("Continous Treatment")) {


                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , AlarmManager.INTERVAL_HOUR * 8, pendingIntent2);
                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedulert.equals("Only One Day")) {

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR * 8, pendingIntent1);

                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");

                    }

                }

                break;



            case "PerEveryMinutes":


                if (schedulert.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent5 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);



                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                       alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+nom*60*1000, nom*60*1000, pendingIntent5);

                 }

                }
//               Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedulert.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent6 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+nom*60*1000 , nom*60*1000, pendingIntent6);


                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");



                    }


                }

                break;


            case "PerEveryHours":

                if (schedulert.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent7 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+noh*60*60*1000 , noh*60*60*1000, pendingIntent7);

                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedulert.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmId);


                    PendingIntent pendingIntent8 = PendingIntent.getBroadcast(vieww.getContext(), Integer.parseInt(alarmId), intent, 0);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+noh*60*60*1000 , noh*60*60*1000, pendingIntent8);


                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");



                    }


                }

                break;


            default:

                System.out.println("Please choose a reminder type");

        }


    }


}
