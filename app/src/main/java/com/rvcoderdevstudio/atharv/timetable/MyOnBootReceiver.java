package com.rvcoderdevstudio.atharv.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyOnBootReceiver extends BroadcastReceiver {

    SQLiteDatabase db;
    AlarmManager alarmMgr;
    Integer min,hour,id;
    String dateString,title;
    Intent i;
    PendingIntent alarmIntent;


    @Override
    public void onReceive(Context context, Intent intent) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Long dateLong = System.currentTimeMillis();
        dateString = format.format(dateLong);





        AlarmManager RepeatingAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent RepeatingIntent = new Intent(context, MyAlarmReceiver.class);
        PendingIntent RepeatingAlarmIntent = PendingIntent.getBroadcast(context, 0, RepeatingIntent, 0);

        Calendar RepeatingCalendar = Calendar.getInstance();
        RepeatingCalendar.setTimeInMillis(System.currentTimeMillis());
        RepeatingCalendar.set(Calendar.DATE,+1);
        RepeatingCalendar.set(Calendar.HOUR_OF_DAY, 0);
        RepeatingCalendar.set(Calendar.MINUTE, 1);

        RepeatingAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, RepeatingCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, RepeatingAlarmIntent);

        try {

            db = context.openOrCreateDatabase("TimeTable",context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("select * from '" + dateString + "'", null);

            cursor.moveToFirst();
            do {

                min = Integer.parseInt(cursor.getString(3));
                hour = Integer.parseInt(cursor.getString(4));
                title = cursor.getString(1);
                id = cursor.getInt(0);
                alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                i = new Intent(context, AlarmTrigger.class);
                i.putExtra("title",title);
                i.putExtra("id",id);
                alarmIntent = PendingIntent.getBroadcast(context, id, i, 0);


                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(System.currentTimeMillis());
                cal1.set(Calendar.HOUR_OF_DAY, hour);
                cal1.set(Calendar.MINUTE, min);

                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), alarmIntent);

            } while (cursor.moveToNext());

            cursor.close();

            Calendar cal2 = Calendar.getInstance();
            String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
            String weekDay = days[cal2.get(Calendar.DAY_OF_WEEK)-1];
            Cursor cursor1 = db.rawQuery("select * from '" + weekDay + "'", null);
            cursor1.moveToFirst();
            do {

                min = Integer.parseInt(cursor1.getString(3));
                hour = Integer.parseInt(cursor1.getString(4));
                title = cursor1.getString(1);
                id = cursor1.getInt(0)+200;
                AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, AlarmTrigger.class);
                i.putExtra("title",title);
                i.putExtra("time",hour.toString()+":"+min.toString());
                i.putExtra("id",id);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, i, 0);

                Calendar cal3  = Calendar.getInstance();
                cal3.setTimeInMillis(System.currentTimeMillis());
                cal3.set(Calendar.HOUR_OF_DAY, hour);
                cal3.set(Calendar.MINUTE, min);

                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal3.getTimeInMillis(), alarmIntent);

            } while (cursor1.moveToNext());

            cursor1.close();

        } catch (Exception e) {
            Log.e("DataBase error : ", e.toString());
        }



        try {

            Cursor cursor3 = db.rawQuery("select * from 'everyday'", null);
            cursor3.moveToFirst();
            do {

                min = Integer.parseInt(cursor3.getString(3));
                hour = Integer.parseInt(cursor3.getString(4));
                title = cursor3.getString(1);
                id = cursor3.getInt(0)+100;
                AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, AlarmTrigger.class);
                i.putExtra("title",title);
                i.putExtra("time",hour.toString()+":"+min.toString());
                i.putExtra("id",id);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, i, 0);

                Calendar cal4  = Calendar.getInstance();
                cal4.setTimeInMillis(System.currentTimeMillis());
                cal4.set(Calendar.HOUR_OF_DAY, hour);
                cal4.set(Calendar.MINUTE, min);

                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal4.getTimeInMillis(), alarmIntent);

            } while (cursor3.moveToNext());

            cursor3.close();
        } catch (Exception e) {
            Log.e("DataBase error : ", e.toString());
        }

        try {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String date = format.format(calendar.getTime());

            db = context.openOrCreateDatabase("TimeTable", context.MODE_PRIVATE, null);
            db.execSQL("drop table '" + date + "'");
            db.close();

        } catch (Exception e) {
            Log.e("yesterdays table : ", e.toString());
        }


    }

}
