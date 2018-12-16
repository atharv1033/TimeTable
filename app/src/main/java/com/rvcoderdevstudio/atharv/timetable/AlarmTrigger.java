package com.rvcoderdevstudio.atharv.timetable;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmTrigger extends BroadcastReceiver {

    Integer id;

    @Override
    public void onReceive(Context context, Intent intent) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long dateLong = calendar.getTimeInMillis();
        String dateString = format.format(dateLong);

        String title;
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        id = bundle.getInt("id");


        createNotificationChannel(context);
        Intent i = new Intent(context, ShowDescription.class);
        i.putExtra("date", dateString);
        i.putExtra("title", title);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, i, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "TimeTable")
                .setSmallIcon(R.mipmap.clockcalender_luanch_icon)
                .setContentTitle("Work to do....")
                .setContentText(title)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent);

        mBuilder.setVibrate(new long[] { 1000 , 1000 , 1000 });
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, mBuilder.build());

    }

    private Boolean createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "com.rvcoderdevstudio.atharv.timetable";
            String description = "com.rvcoderdevstudio.atharv.timetable";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TimeTable", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            return true;
        }
        return false;

    }

}


