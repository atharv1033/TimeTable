package com.rvcoderdevstudio.atharv.timetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class SetTimeActivity extends AppCompatActivity {

    TimePicker timePicker;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        date = bundle.getString("date");
        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    public void setTime(View view) {

        int min,hour;
        min = timePicker.getCurrentMinute();
        hour = timePicker.getCurrentHour();

        Intent i = new Intent(this,SetDescriptionActivity.class);
        i.putExtra("minutes",min);
        i.putExtra("hours",hour);
        i.putExtra("date",date);
        startActivity(i);
    }
}
