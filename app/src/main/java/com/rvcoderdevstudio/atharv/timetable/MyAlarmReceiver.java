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

public class MyAlarmReceiver extends BroadcastReceiver {

    SQLiteDatabase db;
    Integer min,hour,id;
    String title,dateString;

    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Long dateLong = System.currentTimeMillis();
        dateString = format.format(dateLong);


        try {

            db = context.openOrCreateDatabase("TimeTable",context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("select * from '" + dateString + "'", null);
            cursor.moveToFirst();
            do {

                min = Integer.parseInt(cursor.getString(3));
                hour = Integer.parseInt(cursor.getString(4));
                title = cursor.getString(1);
                id = cursor.getInt(0);
                AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, AlarmTrigger.class);
                i.putExtra("title",title);
                i.putExtra("time",hour.toString()+":"+min.toString());
                i.putExtra("id",id);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, i, 0);

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

            Cursor cursor2 = db.rawQuery("select * from 'everyday'", null);
            cursor2.moveToFirst();
            do {

                min = Integer.parseInt(cursor2.getString(3));
                hour = Integer.parseInt(cursor2.getString(4));
                title = cursor2.getString(1);
                id = cursor2.getInt(0)+100;
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

            } while (cursor2.moveToNext());

            cursor2.close();
        } catch (Exception e) {
            Log.e("DataBase error : ", e.toString());
        }


        try {

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            String date = format.format(c.getTime());

            db = context.openOrCreateDatabase("TimeTable", context.MODE_PRIVATE, null);
            db.execSQL("drop table '" + date + "'");
            db.close();

        } catch (Exception e) {
            Log.e("yesterdays table : ", e.toString());
        }

    }
}
