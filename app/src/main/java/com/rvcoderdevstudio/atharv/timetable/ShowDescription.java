package com.rvcoderdevstudio.atharv.timetable;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_description);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String dateString = bundle.getString("date");
        String title = bundle.getString("title");
        String description,hours,min,time;
        TextView titleView,descriptionView,timeView;
        titleView = (TextView) findViewById(R.id.titleText);
        descriptionView = (TextView) findViewById(R.id.descriptionText);
        timeView = (TextView) findViewById(R.id.timeText);
        description = "";
        time = "";
        SQLiteDatabase db;

        if(dateString.equals("note")) {
            db = openOrCreateDatabase("TimeTable", MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("select * from '" + dateString + "' where title = '" + title + "'" ,null);
            cursor.moveToFirst();
            description = cursor.getString(2);
        }else{

        try {

            db = openOrCreateDatabase("TimeTable", MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("select * from '" + dateString + "' where title = '" + title + "'" ,null);
            cursor.moveToFirst();
            description = cursor.getString(2);
            min = cursor.getString(3);
            hours = cursor.getString(4);
            Integer minInt = Integer.parseInt(min);
            if(minInt < 10) {
                min = "0"+min;
            }
            time = hours+":"+min;
            cursor.close();
            db.close();

        }catch(Exception ex) {
            Log.e("Database error :",ex.getMessage());
            }}

            titleView.setText(title);
            descriptionView.setText(description);
            timeView.setText(time);



    }
}
