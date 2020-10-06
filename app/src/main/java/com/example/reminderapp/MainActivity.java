package com.example.reminderapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.dataRecyclerView.DataAdapter;
import com.example.reminderapp.dataRecyclerView.DataObject;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DatePickerListener {

    //listview
    private RecyclerView mmdataRecyclerView;
    private RecyclerView.Adapter mmdataAdapter;
    private RecyclerView.LayoutManager mmDataLayoutManager;


    private Toolbar toolbar;

    //datepicker
    private HorizontalPicker hPicker;


    private Button addnewbtn;
    private ImageView addnewimg;

    //selectedDate

    private DateTime selectedDate;

    private static String selectedDateString;


    //dbhelper

    DatabaseHelper dbhelper;

    public static String APP_TAG="AlarmTest";


    int year;
    int month;
    int day;

    LinearLayout backgroundlayout;

    ImageView refreshbtn;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.customtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backgroundlayout=(LinearLayout)findViewById(R.id.backgroundlayout);


        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
//
//        toolbar.setLogo(getDrawable(R.drawable.docnnurse));
//        toolbar.setTitle("Medication Reminder");


        //listview

        mmdataRecyclerView=(RecyclerView)findViewById(R.id.dataListView);

        mmdataRecyclerView.setNestedScrollingEnabled(false);
        mmdataRecyclerView.setHasFixedSize(true);



        mmDataLayoutManager=new LinearLayoutManager(MainActivity.this);

        mmdataRecyclerView.setLayoutManager(mmDataLayoutManager);
        mmdataAdapter =new DataAdapter(getDataSet(),MainActivity.this);
        mmdataRecyclerView.setAdapter(mmdataAdapter);

//        addnewbtn=(Button)findViewById(R.id.addnewbtn);

        addnewimg=(ImageView)findViewById(R.id.addnewimg);

        dbhelper=new DatabaseHelper(this);

        refreshbtn=(ImageView)findViewById(R.id.refreshbtn);




        getAlarmIDs();







//datepiicker
        hPicker=(HorizontalPicker)findViewById(R.id.datePicker);
        hPicker.setListener(this)
                .setDays(180)
                .setOffset(60)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getColor(R.color.bluelight))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getColor(R.color.colorPrimaryDark))
                .showTodayButton(false)
                .init();

        hPicker.setBackgroundColor(Color.LTGRAY);
        hPicker.setDate(new DateTime());




        addnewimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                AlarmManager alarmManager=(AlarmManager)view.getContext().getSystemService(Context.ALARM_SERVICE);
//                Intent intent=new Intent(view.getContext(), AlertReceiver.class);
//                PendingIntent cancelIntent = PendingIntent.getBroadcast(view.getContext(), 396377, intent, 0);
//
//                alarmManager.cancel(cancelIntent);

                Intent in=new Intent(MainActivity.this,AddMedInfo.class);



                if (selectedDate!=null){

                    selectedDate.toLocalDate();

                }
                in.putExtra("selectedDate",selectedDateString.toString());
                in.putExtra("year",Integer.toString(year));
                in.putExtra("month",Integer.toString(month));
                in.putExtra("day",Integer.toString(day));

                startActivity(in);

                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });


        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.refresh, null);
                final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(popupView, Gravity.TOP,5,5);

                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                popupWindow.update();


                View container = popupWindow.getContentView().getRootView();
                if(container != null) {
                    WindowManager wm = (WindowManager)MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
                    WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
                    p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    p.dimAmount = 0.6f;
                    if(wm != null) {
                        wm.updateViewLayout(container, p);

//                        SharedPreferences prefs= getSharedPreferences("SaveDate", MODE_PRIVATE);
//                        //save the value
//                        prefs.edit().putString("selectedDate", selectedDateString).apply();
//                        prefs.edit().putString("year", Integer.toString(year)).apply();
//                        prefs.edit().putString("month", Integer.toString(month)).apply();
//                        prefs.edit().putString("day", Integer.toString(day)).apply();
//



                    }
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        popupWindow.dismiss();
                        recreate();

//                        SharedPreferences prefs= view.getContext().getSharedPreferences("SaveDate", MODE_PRIVATE);
//                        //get the value
//
////                        selectedDateString=prefs.getString("selectedDate", "20200101");
//                        year=Integer.parseInt(prefs.getString("year", "2020"));
//                        month=Integer.parseInt(prefs.getString("month", "01"));
//                        day=Integer.parseInt(prefs.getString("day", "01"));



                    }





                },3000);








            }
        });

       // getAllTheDates();

        getMedInfoRef();


    }



    @Override
    public void onDateSelected(DateTime dateSelected) {

        selectedDate=dateSelected;
        selectedDate.toLocalDate();
      //  Toast.makeText(this, selectedDate.toString(), Toast.LENGTH_LONG).show();

        LocalDate dd =selectedDate.toLocalDate();

        year=dd.get(DateTimeFieldType.year());
        month=dd.get(DateTimeFieldType.monthOfYear());
       day=dd.get(DateTimeFieldType.dayOfMonth());

        selectedDateString=Integer.toString(year)+Integer.toString(month)+ Integer.toString(day);
//        Toast.makeText(this,        dd.toString(), Toast.LENGTH_LONG).show();


//        getMedInfoRef();
        getAlarmIDs();

    }



//getting the dates
private void getAllTheDates() {

    Cursor cursor=dbhelper.getDates();

    if (cursor.getCount()==0){

        Toast.makeText(this, "No Dates to retrieve", Toast.LENGTH_SHORT).show();
    }

    else {

        while (cursor.moveToNext()){


           // Toast.makeText(this, "Dates List "+cursor.getString(0), Toast.LENGTH_LONG).show();

            DataObject obj= new DataObject(cursor.getString(0));
            resultsData.add(obj);

            mmdataAdapter.notifyDataSetChanged();


        }

    }

}





//getting medinfo for reference date

    public void getMedInfoRef() {

Cursor cursor=dbhelper.getMedName(selectedDateString);

resultsData.removeAll(resultsData);


if (cursor.getCount()==0){

//    Toast.makeText(this, "No Meds For This Date", Toast.LENGTH_SHORT).show();
    resultsData.removeAll(resultsData);
    mmdataAdapter.notifyDataSetChanged();


}

else {

    while (cursor.moveToNext()){

      //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();

        DataObject obj= new DataObject(cursor.getString(0));
        resultsData.add(obj);

        mmdataAdapter.notifyDataSetChanged();


    }
}

    }


    //getting AlarmIDs

    public void getAlarmIDs(){

        Cursor cursor=dbhelper.getAlarmIDs(selectedDateString);

        resultsData.removeAll(resultsData);



        if (cursor.getCount()==0){

            Toast.makeText(this, "No Reminders For This Date", Toast.LENGTH_SHORT).show();
            resultsData.removeAll(resultsData);
            mmdataAdapter.notifyDataSetChanged();
            backgroundlayout.setBackgroundResource(R.drawable.back2);


        }

        else {

            while (cursor.moveToNext()){

                //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();

                DataObject obj= new DataObject(cursor.getString(0));
                resultsData.add(obj);

                mmdataAdapter.notifyDataSetChanged();

                backgroundlayout.setBackgroundResource(R.drawable.backempty);



            }
        }

    }






    //listview

    private ArrayList resultsData=new ArrayList<DataObject>();

    private ArrayList<DataObject> getDataSet() {

        return resultsData;
    }



    @Override
    protected void onResume() {
        super.onResume();

        getAlarmIDs();

        System.out.println("ON RESUMEEEEEEEEE");

    }



    @Override
    protected final void onRestoreInstanceState(final Bundle inState)
    {
        // Restore the saved variables.
        selectedDateString = inState.getString("selectedDate", "20200101");
        year = inState.getInt("year");
        month = inState.getInt("month");
        day = inState.getInt("day");

    }
    @Override
    protected final void onSaveInstanceState(final Bundle outState) {
        // Save the variables.
        super.onSaveInstanceState(outState);

        outState.putString("selectedDate",selectedDateString);
        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);
    }








}
