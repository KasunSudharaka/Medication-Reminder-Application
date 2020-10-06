package com.example.reminderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.text.InputType;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Integer.valueOf;

public class AddMedInfo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;


    private String selectedDate;

    private EditText selecteddateet,mednameet;
    private Button loadbtn,getmednamebtn;
    private FloatingActionButton savebtn;

    //dbhandler

//    private MySQLiteDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;


    //new code

    DatabaseHelper dbhelper;


    //LinearLayouts

    private LinearLayout takeoneLayout,reminderTimesLayout,scheduleLayout,quantityLayout,alarmLayoutnotesLayout,notesLayout;

    EditText vedt=null,edPop;
    Button btOk=null;

    private Spinner remindTimesSpinner,scheduletypeSpinner;

    //alarmID
    private String alarmID;


    //calendar instance;
    Calendar c;

    //notes,remindertimes,schedule and nop
    String reminderNote;
    String reminderTimes;
    String schedule;
    String nop;
    int nod;
    int nom;
    int noh;
    String nodString;

    AlarmManager alarmManager;
    Intent intent;

//TextView
    private TextView timetv,inttv,notetv,noofdaystv,customtv;

    private int year;
    private int month;
    private int day;


    private static final String filename="alarmID.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med_info);

        toolbar=findViewById(R.id.customtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


//        selecteddateet=(EditText) findViewById(R.id.selecteddateet);
        mednameet=(EditText) findViewById(R.id.mednameet);

        savebtn=(FloatingActionButton) findViewById(R.id.savebtn);
//        loadbtn=(Button)findViewById(R.id.loadbtn);
//        getmednamebtn=(Button)findViewById(R.id.getNamebtn);




        //layouts
        takeoneLayout=(LinearLayout)findViewById(R.id.takeonelayout);
        reminderTimesLayout=(LinearLayout)findViewById(R.id.remindertimeslayout);
        scheduleLayout=(LinearLayout)findViewById(R.id.schedulelayout);
        quantityLayout=(LinearLayout)findViewById(R.id.quantitylayout);
//        alarmLayoutnotesLayout=(LinearLayout)findViewById(R.id.alarmlayout);
        notesLayout=(LinearLayout)findViewById(R.id.noteslyout);


        remindTimesSpinner=(Spinner)findViewById(R.id.remindtimesspinner);
        scheduletypeSpinner=(Spinner)findViewById(R.id.scheduletypespinner);

        //textviews
        timetv=(TextView)findViewById(R.id.timetv);
        inttv=(TextView)findViewById(R.id.inttv);
        notetv=(TextView)findViewById(R.id.notetv);
        noofdaystv=(TextView)findViewById(R.id.noofdaystv);
        customtv=(TextView)findViewById(R.id.customtv);




        alarmManager=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        intent=new Intent(this,AlertReceiver.class);


//calendar instance
        c=Calendar.getInstance();
        c.clear();


        Bundle extras=getIntent().getExtras();


        if (extras!=null){

            selectedDate=getIntent().getStringExtra("selectedDate");
            year=Integer.parseInt(getIntent().getStringExtra("year"));
            month=Integer.parseInt(getIntent().getStringExtra("month"))-1;
            day=Integer.parseInt(getIntent().getStringExtra("day"));


            System.out.println("SELECTED DATEEEEEEE"+selectedDate);
            System.out.println("SELECTED Yearrr"+year);
            System.out.println("SELECTED month"+month);
            System.out.println("SELECTED day"+day);



//            selecteddateet.setText(selectedDate.toString());




        }




        //new database

        dbhelper=new DatabaseHelper(this);



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                insertDataToDB();

                //gettng time from timepicker and setAlarm


                if (!mednameet.getText().toString().equals("")) {

                startAlarm(c);


                //new code

//                boolean insert= dbhelper.insertToMedInfo(selectedDate,mednameet.getText().toString());
//
//                if (insert==true){
//
//                    mednameet.setText("");
//                }





                    //inserting data with AlarmId


                    boolean insertData = dbhelper.insertDataWithAlarmID(alarmID, selectedDate, mednameet.getText().toString(), reminderNote, reminderTimes, schedule, nop);

                    if (insertData == true) {

                        mednameet.setText("");
                        Toast.makeText(AddMedInfo.this, "Successfully Insterted!", Toast.LENGTH_LONG).show();

                        System.out.println("Alarm ID and DATA INSERTEDDDD");
                    }


                    System.out.println("UNSELCTED TIMEEEE" + c.getTimeInMillis());

                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


                }

                else {

                    Toast.makeText(AddMedInfo.this, "Please Fill The Medication Name", Toast.LENGTH_SHORT).show();
                }

            }
        });


//
//        getmednamebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Cursor cursor1=dbhelper.getDataWithAlarmId(selectedDate);
//
//
//                if (cursor1.getCount()==0){
//
//                    Toast.makeText(AddMedInfo.this, "No Data", Toast.LENGTH_SHORT).show();
//                }
//
//
//                else {
//
//
//
//                    while (cursor1.moveToNext()){
//
//                        mednameet.setText(cursor1.getString(0));
//                        Toast.makeText(AddMedInfo.this, "Medicine Name: "+cursor1.getString(0), Toast.LENGTH_LONG).show();
//                        System.out.println("MEDNAMEEEEEEEEE"+cursor1.getString(0));
//                }
//
//                }
//            }
//        });


        takeoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCalendarViewFragment();
//                showCalendarView(AddMedInfo.this);
            }
        });

        quantityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


             showAddNoOfPills(AddMedInfo.this);



            }
        });




        notesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNoteDialog(AddMedInfo.this);



            }
        });




        //remindertimesspinner

        remindTimesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddMedInfo.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();


                if (adapterView.getSelectedItem().toString().equals("Per Every n Minutes")){

                    showAddNoOfMinutes(AddMedInfo.this);

                }



                if (adapterView.getSelectedItem().toString().equals("Per Every n Hours")){

                    showAddNoOfHours(AddMedInfo.this);

                }


                reminderTimes=adapterView.getSelectedItem().toString();



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //scheduletype spinner

        scheduletypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AddMedInfo.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                String scheduletype=adapterView.getSelectedItem().toString();;



                schedule=scheduletype;

//                Toast.makeText(AddMedInfo.this, schedule, Toast.LENGTH_SHORT).show();

//                switch (scheduletype){
//
//
//                    case "Continous Treatment":
////
//                        schedule=scheduletype;
////                        showAddNoOfDays(AddMedInfo.this);
//                        break;
//
//
//                    case "Only One Day":
//                        schedule=  scheduletype;
//                        break;
//
//                    default:
//                        System.out.println("Select a method");
//
//
//                }

//                if (adapterView.getSelectedItem().toString()=="No Of Days"){
//
//                    Toast.makeText(AddMedInfo.this, "Adapterrrrrrrrrrrrrrr", Toast.LENGTH_SHORT).show();
//
//                    showAddNoOfDays(AddMedInfo.this);
//
//
//                }
//
//                else {
//                    schedule=  scheduletype;
//
//                }

            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }




    //dialogBox show

    private void showAddNoteDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a Reminder Note")
                .setMessage("Add a special note to remind")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reminderNote= String.valueOf(taskEditText.getText());
                        notetv.setText(reminderNote);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    //no of pills

    private void showAddNoOfPills(Context c) {
        final EditText quantityEditText = new EditText(c);
        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Pills Quantity")
                .setMessage("How many pills do u take?")
                .setView(quantityEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nop= String.valueOf(quantityEditText.getText()+" Pills");
                        inttv.setText(String.valueOf(quantityEditText.getText()+" Pills"));
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    //no of minutes

    private void showAddNoOfMinutes(Context c) {
        final EditText nomEditText = new EditText(c);
        nomEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Number of minutes")
                .setMessage("Please set the minute interval")
                .setView(nomEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nom= Integer.parseInt(nomEditText.getText().toString());
                        customtv.setText(String.valueOf(nomEditText.getText()+" Minutes"));
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    //no of minutes

    private void showAddNoOfHours(Context c) {
        final EditText nohEditText = new EditText(c);
        nohEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Number of hours")
                .setMessage("Please set the hours interval")
                .setView(nohEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noh= Integer.parseInt(nohEditText.getText().toString());
                        customtv.setText(String.valueOf(nohEditText.getText()+" Hours"));
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    //no of days

    private void showAddNoOfDays(Context c) {
        final EditText nodEditText = new EditText(c);
        nodEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("No Of Days")
                .setMessage("How many days you take medication?")
                .setView(nodEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nodString= String.valueOf(nodEditText.getText());
                        nod= Integer.parseInt(String.valueOf(nodEditText.getText()));
                        noofdaystv.setText(nodString+ "Day(s)");
                        schedule=nodString+ " Day(s)";
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }



    private void showCalendarViewFragment(){


        DialogFragment timepicker=new TimePickerFragment();
        timepicker.show(getSupportFragmentManager(),"time picker");

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);
        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

//        c.setTimeInMillis(System.currentTimeMillis());
//        c.set(2020,3,31,14,13);


        System.out.println("SET GET TIME CUREENT IN MS"+c.getTimeInMillis());

        Toast.makeText(this, "Hour: "+hourOfDay+ "Minute : "+minute, Toast.LENGTH_SHORT).show();
        timetv.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));

    }



//startAarm Method

    private void startAlarm(Calendar c) {


         generateIDs();



//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,Integer.parseInt(alarmID),intent,0);
//
//        if (c.before(Calendar.getInstance())){
//
//            c.add(Calendar.DATE,1);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
//        }
//
//
//        Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();





        switch (reminderTimes){

            case "Once Per Day":



                if (schedule.equals("Continous Treatment")) {
//
//                    Bundle bun = new Bundle();
//                    bun.putString("alarmID", alarmID);
//                    intent.setAction("" + Math.random());
//                    intent.putExtras(bun);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    intent.putExtra("alarmID",alarmID);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        System.out.println("SET GET TIME CUREENT 2NDDD IN MS"+c.getTimeInMillis());


                        //new

//                        new Utils().setPreference("isFirstStart", false, getActivity().getApplicationContext());


//                        intent.putExtra("medName",mednameet.getText().toString());
//                        intent.putExtra("note",reminderNote);
////                        intent.putExtra("alarmID",alarmID);

                        saveInsharedPreferences();

//                        sendBroadcast(intent);


                        System.out.println("MEdnameeeeeeee "+mednameet.getText().toString());


                        System.out.println("ALARM SETTTTTTTT");
                    }

                }

                if (schedule.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmID);



                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , pendingIntent1);

                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");

                    }


                }


                System.out.println("ALARM DID NOT SETTTTTTTTTTTT");

                break;

            case "Twice Per Day":



                if (schedule.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmID);



                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent1);


                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedule.equals("Only One Day")){

//                    Bundle bun = new Bundle();
//                    bun.putString("alarmID", alarmID);
//                    intent.setAction("" + Math.random());
//                    intent.putExtras(bun);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    intent.putExtra("alarmID",alarmID);

                    PendingIntent pendingIntent4 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent4);

//                        long stoptime=c.getTimeInMillis()+4*60*1000;



                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");

                        Intent cancellationIntent = new Intent(this, CancelationReceiver.class);
                        cancellationIntent.putExtra("intent", pendingIntent4);
//                        cancellationIntent.putExtra("alarmID",alarmID);
                        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+25*60*60*1000, cancellationPendingIntent);

                        System.out.println("CANCEL ALARM TRIGGEREDDDDDD");


//                        if (c.getTimeInMillis()==stoptime) {
//
//                            PendingIntent cancelationIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);
//                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 4 * 60 * 1000, cancelationIntent);
//                            alarmManager.cancel(cancelationIntent);
//
//                        }



                    }




                }



                break;

            case "3 Times Per Day":


                if (schedule.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , AlarmManager.INTERVAL_HOUR * 8, pendingIntent2);

                    }

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedule.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , AlarmManager.INTERVAL_HOUR * 8, pendingIntent1);


                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");



                        Intent cancellationIntent = new Intent(this, CancelationReceiver.class);
                        cancellationIntent.putExtra("intent", pendingIntent1);
//                        cancellationIntent.putExtra("alarmID",alarmID);
                        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+25*60*60*1000, cancellationPendingIntent);

                        System.out.println("CANCEL ALARM TRIGGEREDDDDDD");


                    }


                }

                break;



            case "Per Every n Minutes":


                if (schedule.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent5 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , nom*60*1000, pendingIntent5);

                    }

                    reminderTimes="Per Every "+nom+" Minutes";

                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedule.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent6 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , nom*60*1000, pendingIntent6);


                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");



                        Intent cancellationIntent = new Intent(this, CancelationReceiver.class);
                        cancellationIntent.putExtra("intent", pendingIntent6);
//                        cancellationIntent.putExtra("alarmID",alarmID);
                        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+24*60*60*1000, cancellationPendingIntent);

                        System.out.println("CANCEL ALARM TRIGGEREDDDDDD");


                    }

                    reminderTimes="Per Every "+nom+" Minutes";


                }

                break;


            case "Per Every n Hours":

                if (schedule.equals("Continous Treatment")) {

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent7 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , noh*60*60*1000, pendingIntent7);

                    }

                    reminderTimes="Per Every "+noh+" Hours";


                }
//                Toast.makeText(this, "The Alarm ID is: "+alarmID, Toast.LENGTH_SHORT).show();


                if (schedule.equals("Only One Day")){

                    intent.putExtra("alarmID",alarmID);


                    PendingIntent pendingIntent8 = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), intent, 0);

                    if (c.before(Calendar.getInstance())) {

                        c.add(Calendar.DATE, 1);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , noh*60*60*1000, pendingIntent8);


                        System.out.println("ONEDAYYYYYYYY ALARM SETTT");



                        Intent cancellationIntent = new Intent(this, CancelationReceiver.class);
                        cancellationIntent.putExtra("intent", pendingIntent8);
//                        cancellationIntent.putExtra("alarmID",alarmID);
                        PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(alarmID), cancellationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+24*60*60*1000, cancellationPendingIntent);

                        System.out.println("CANCEL ALARM TRIGGEREDDDDDD");


                    }

                    reminderTimes="Per Every "+noh+" Hours";

                }

                break;






            default:

                System.out.println("Please choose a reminder type");

        }




//



//        if (reminderTimes=="Once Per Day" && schedule=="Continous Treatment"){
//
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,Integer.parseInt(alarmID),intent,0);
//
//            if (c.before(Calendar.getInstance())){
//
//                c.add(Calendar.DATE,1);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+60*1000,AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
//
//                System.out.println("METHOD CALLEEDDDDDDDD");
//            }
//
//
//        }
//
//
//        else if(reminderTimes=="Twice Per Day" && schedule=="Continous Treatment"){
//
//
//
//            PendingIntent pendingIntent1=PendingIntent.getBroadcast(this,Integer.parseInt(alarmID),intent,0);
//
//            if (c.before(Calendar.getInstance())){
//
//                c.add(Calendar.DATE,1);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+60*1000,AlarmManager.INTERVAL_HALF_DAY,pendingIntent1);
//            }
//
//
//
//        }
//
//
//        else if(reminderTimes=="3 Times Per Day" && schedule=="Continous Treatment"){
//
//
//
//            PendingIntent pendingIntent2=PendingIntent.getBroadcast(this,Integer.parseInt(alarmID),intent,0);
//
//            if (c.before(Calendar.getInstance())){
//
//                c.add(Calendar.DATE,1);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+60*1000,AlarmManager.INTERVAL_HOUR*8,pendingIntent2);
//            }
//
//
//        }




    }


    public void saveInsharedPreferences(){



        SharedPreferences prefs= getSharedPreferences("aName", MODE_PRIVATE);
        //save the value
        prefs.edit().putString("alarmID", alarmID).apply();

        System.out.println("ALAERDADSD"+alarmID);
    }





    public void generateIDs(){

        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);

        alarmID=Integer.toString(n);
    }

    @Override
    protected void onStop() {
        super.onStop();

        intent.putExtra("newAlarmID",alarmID);
//        startActivity(intent);
    }

    //    public void save(){
//
//        String text=alarmID;
//        FileOutputStream fos=null;
//
//
//        try {
//            fos=openFileOutput(filename,MODE_PRIVATE);
//            fos.write(text.getBytes());
//
//            Toast.makeText(this,"Saved to"+getFilesDir()+ "/"+filename,Toast.LENGTH_LONG).show();
//        }
//
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        finally {
//            if (fos!=null){
//
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//    }
//





    public void setAlarmOnce(){


    }



    //this code was in onCreate

    //database sqllite

//        try {
//            dbHandler=new MySQLiteDBHandler(this,"MedicineDatabase",null,1);
//            sqLiteDatabase=dbHandler.getWritableDatabase();
//
//            sqLiteDatabase.execSQL("CREATE TABLE MedInfo(Date text, MedName text)");
//        }
//
//        catch (Exception e){
//
//            e.printStackTrace();
//        }



//        loadbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                readDB();
//
//
//                //new code
//                Cursor cursor=dbhelper.allData(selectedDate);
//
//                if (cursor.getCount()==0){
//
//                    Toast.makeText(AddMedInfo.this, "No Data", Toast.LENGTH_SHORT).show();
//                }
//
//
//                else {
//
//                    while (cursor.moveToNext()){
//
//                        Toast.makeText(AddMedInfo.this, "Date : "+cursor.getString(0), Toast.LENGTH_LONG).show();
//                        Toast.makeText(AddMedInfo.this, "Medicine Name : "+cursor.getString(1), Toast.LENGTH_LONG).show();
//
//                    }
//                }
//
//
//            }
//        });


//    public void insertDataToDB(){
//
//        ContentValues contentValues=new ContentValues();
//        contentValues.put("Date",selectedDate.toString());
//        contentValues.put("MedName",mednameet.getText().toString());
//
//        sqLiteDatabase.insert("MedInfo",null,contentValues);
//        System.out.println("INSERTED INTO THE DATABASEEEEEEEEE");
//        mednameet.setText("");
//
//    }
//
//
//
//    public void readDB(){
//
//
//        String query="Select MedName from MedInfo where Date =" +selectedDate;
//
//
//
//
//        try {
//
//            Cursor cursor=sqLiteDatabase.rawQuery(query,null,null);
//            cursor.moveToFirst();
//            mednameet.setText(cursor.getString(0));
//
//            System.out.println("SELECTED DATEEEEEEE"+selectedDate);
//
//        }
//
//        catch (Exception e){
//
//            e.printStackTrace();
//            mednameet.setText("");
//
//            System.out.println("ERRROORRRRRR");
//        }
//
//    }

    //dialogBox show

//    private void showCalendarView(Context c) {
//        final TimePicker timepicker = new TimePicker(c);
//
//        AlertDialog dialog = new AlertDialog.Builder(c)
//                .setTitle("")
//                .setMessage("Select the time of first take")
//                .setView(timepicker)
//                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String task = String.valueOf(timepicker.getMinute());
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .create();
//        dialog.show();
//    }



}
