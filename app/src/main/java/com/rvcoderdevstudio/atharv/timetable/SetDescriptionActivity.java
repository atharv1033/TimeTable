package com.rvcoderdevstudio.atharv.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class SetDescriptionActivity extends AppCompatActivity {

    Integer minInt,hourInt;
    String minStr,hourStr,dateStr;
    EditText editTitle , editDescription;
    String title , description ;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_description);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        minInt = bundle.getInt("minutes");
        hourInt = bundle.getInt("hours");
        minStr = minInt.toString();
        hourStr = hourInt.toString();
        dateStr = bundle.getString("date");
        editTitle = (EditText) findViewById(R.id.editText);
        editDescription = (EditText) findViewById(R.id.editText2);
         db = openOrCreateDatabase("TimeTable", MODE_PRIVATE, null);
        if(dateStr.equals("note")){
            Button btn = findViewById(R.id.button3);
            btn.setText("SAVE");
        }
    }

    public void setJob(View view) {


        title = editTitle.getText().toString();
        description = editDescription.getText().toString();

        if(title.isEmpty()) {
            Toast.makeText(this, "title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dateStr.equals("note")) {
            try {

                db.execSQL("create table if not exists '" + dateStr + "'(id integer primary key autoincrement not null,title varchar unique not null,description varchar)");
                db.execSQL("insert into '" + dateStr + "' (title,description) values('" + title + "','" + description + "')");
                Toast.makeText(this, "NOTE ADDED TO THE LIST", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("DataBase error : ", e.toString());
                Toast.makeText(this, "NOTE NOT ADDED TO THE LIST", Toast.LENGTH_SHORT).show();
            }
            Intent intent1 = new Intent(this,NotesActivity.class);
            startActivity(intent1);
            return;
        }
        Toast.makeText(this, hourStr+":"+minStr, Toast.LENGTH_SHORT).show();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Long dateLong = System.currentTimeMillis();
        String CurrentDate = format.format(dateLong);


            try {

                db.execSQL("create table if not exists '" + dateStr + "'(id integer primary key autoincrement not null,title varchar unique not null,description varchar,min varchar,hour varchar,flag boolean)");
                db.execSQL("insert into '" + dateStr + "' (title,description,min,hour) values('" + title + "','" + description + "','" + minStr + "','" + hourStr + "')");
                Toast.makeText(this, "JOB ADDED TO THE LIST", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("DataBase error : ", e.toString());
                Toast.makeText(this, "JOB NOT ADDED TO THE LIST", Toast.LENGTH_SHORT).show();
            }

        Calendar cal1 = Calendar.getInstance();
        String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String weekDay = days[cal1.get(Calendar.DAY_OF_WEEK)-1];



        if(CurrentDate.equals(dateStr) || dateStr.equals("everyday") || dateStr.equals(weekDay)) {

                try {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hourInt);
                    calendar.set(Calendar.MINUTE, minInt);

                    if(calendar.getTimeInMillis() > System.currentTimeMillis()) {

                        Cursor cursor = db.rawQuery("select id from '" + dateStr + "' where title = '" + title + "'", null);
                        cursor.moveToFirst();
                        int id = cursor.getInt(0);
                        if(dateStr.equals("everyday")){id=id+100;}
                        else if(dateStr.equals(weekDay)){id=id+200;}
                        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                        Intent i = new Intent(this, AlarmTrigger.class);
                        i.putExtra("title", title);
                        i.putExtra("id", id);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, id, i, 0);

                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

                        cursor.close();

                    }

                }catch (Exception e) {
                    Log.e("DataBase error : ", e.toString());
                }


        }


        Intent intent;
            if(dateStr.equals("everyday")) {
                intent = new Intent(this,everydayList.class);
                intent.putExtra("date","everyday");
                startActivity(intent);
            }else if(CurrentDate.equals(dateStr)){
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else{
                intent = new Intent(this,GetList.class);
                intent.putExtra("date",dateStr);
                startActivity(intent); }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();

    }

}
