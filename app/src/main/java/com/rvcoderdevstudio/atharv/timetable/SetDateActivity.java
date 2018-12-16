package com.rvcoderdevstudio.atharv.timetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class SetDateActivity extends AppCompatActivity {

    DatePicker datePicker;
    String str_functionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        str_functionality = bundle.getString("functionality");

        Button btn = findViewById(R.id.button);
        btn.setText(str_functionality);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis());
    }

    public void setDate(View view) {

        Integer day,month,year;
        String dateString;

        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth() + 1;
        year = datePicker.getYear();

        String dayStr,monthStr,yearStr ;
        if (day < 10) { dayStr = "0" + day.toString();  }
        else {dayStr = day.toString();}
        if (month < 10) {monthStr = "0" + month.toString(); }
        else {monthStr = month.toString(); }
        yearStr = year.toString();

        dateString = dayStr + "/" + monthStr + "/" + yearStr;

        Toast.makeText(this, dateString, Toast.LENGTH_SHORT).show();

        if(str_functionality.equals("NEXT")) {

            Intent i = new Intent(this, SetTimeActivity.class);
            i.putExtra("date", dateString);
            startActivity(i);

        }else if(str_functionality.equals("SHOW")){

            Intent i = new Intent(this, GetList.class);
            i.putExtra("date",dateString);
            startActivity(i);

        }else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }

    /* public void show(View view) {

        Integer day,month,year;
        String dateString;

        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth() + 1;
        year = datePicker.getYear();

        String dayStr,monthStr,yearStr ;
        if (day < 10) { dayStr = "0" + day.toString();  }
        else {dayStr = day.toString();}
        if (month < 10) {monthStr = "0" + month.toString(); }
        else {monthStr = month.toString(); }
        yearStr = year.toString();

        dateString = dayStr + "/" + monthStr + "/" + yearStr;

        Intent i = new Intent(this, GetList.class);
        i.putExtra("date",dateString);
        startActivity(i);

    }

    public void everydayList(View view) {

        Intent intent = new Intent(this,everydayList.class);
        intent.putExtra("date","everyday");
        startActivity(intent);

    } */
}
