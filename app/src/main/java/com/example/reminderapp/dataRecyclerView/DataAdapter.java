package com.example.reminderapp.dataRecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reminderapp.DatabaseHelper;
import com.example.reminderapp.MainActivity;
import com.example.reminderapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private List<DataObject> itemList;
    private Context context;
    DatabaseHelper dbhelper;

    String alarmId;






    public DataAdapter(List<DataObject> itemList, Context context){

        this.itemList=itemList;
        this.context=context;

        dbhelper=new DatabaseHelper(context);

    }

    @Override
    public DataViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        DataViewHolder dvh=new DataViewHolder(layoutView);


        return dvh;
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {



        holder.dummydata.setText(itemList.get(position).getDummydata());


        alarmId=itemList.get(position).getDummydata();


        try{

           Integer checkint= Integer.parseInt(alarmId);



            System.out.println("ALARM IDDDD ON DATABIND"+alarmId);


            Cursor cursor= dbhelper.getMedNameByID(alarmId);


            if (cursor.getCount()==0){

                Toast.makeText(context, "No Meds For This Date", Toast.LENGTH_SHORT).show();


            }

            else {

                while (cursor.moveToNext()){

                    holder.mednametv.setText(cursor.getString(0));

                    //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();



                }
            }

//            Toast.makeText(context, "AlarmID isss: "+alarmId, Toast.LENGTH_SHORT).show();


            Cursor cursor1= dbhelper.getNoOfTakesById(alarmId);


            if (cursor1.getCount()==0){

//                Toast.makeText(context, "You have never ttaken this med", Toast.LENGTH_SHORT).show();
                holder.nooftakestv.setText("0");


            }

            else {

                while (cursor1.moveToNext()){



                    System.out.println("CURSORRRRR"+cursor1.getString(0));
                    holder.nooftakestv.setText(cursor1.getString(0));

                    //  Toast.makeText(this, "Meds List "+cursor.getString(0), Toast.LENGTH_LONG).show();

                }
            }



        }

        catch (NumberFormatException e){

            e.printStackTrace();
        }







    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }




}
