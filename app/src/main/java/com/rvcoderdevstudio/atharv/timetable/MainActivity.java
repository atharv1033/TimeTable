package com.rvcoderdevstudio.atharv.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String dateString, timeStr;
    Integer timeInt;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SQLiteDatabase db;
    SimpleDateFormat format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        try{

        }catch (Exception e){
            Log.e("DataBase doesn't exist:", e.toString());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, Setting Repeating Alarm that triggers all alarms of that day at 12:01 am.
            Log.d("Comments", "First time");


            AlarmManager RepeatingAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent RepeatingIntent = new Intent(this, MyAlarmReceiver.class);
            PendingIntent RepeatingAlarmIntent = PendingIntent.getBroadcast(this, 0, RepeatingIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);

            RepeatingAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, RepeatingAlarmIntent);

            //Initializing help on the first launch
            Intent help = new Intent(this, helpActivity.class);
            startActivity(help);

            settings.edit().putBoolean("my_first_time", false).commit();
        }

        format = new SimpleDateFormat("dd/MM/yyyy");
        Long dateLong = System.currentTimeMillis();
        dateString = format.format(dateLong);

        List<String> title = new ArrayList<>();


        try {

            db = openOrCreateDatabase("TimeTable", MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("select title from '" + dateString + "'", null);

            cursor.moveToFirst();
            do {
                title.add(cursor.getString(0));

            } while (cursor.moveToNext());

            cursor.close();


        } catch (Exception e) {
            Log.e("DataBase error : ", e.toString());
            title.add("No work on today's list");
            title.add("Add some Work to do...");

        }


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerList);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(title);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void AddItem(View view) {

        Intent i = new Intent(this, SetDateActivity.class);
        i.putExtra("functionality", "NEXT");
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu_layout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {
            Intent help = new Intent(this, helpActivity.class);
            startActivity(help);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_GetList) {

            Intent intent = new Intent(this, SetDateActivity.class);
            intent.putExtra("functionality", "SHOW");
            startActivity(intent);

        } else if (id == R.id.nav_tomorrow) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.DATE, +1);
            String tomorrowDate = format.format(calendar.getTime());

            Intent intent = new Intent(this, GetList.class);
            intent.putExtra("date", tomorrowDate);
            startActivity(intent);

        } else if (id == R.id.nav_everydayList) {

            Intent intent = new Intent(this, everydayList.class);
            intent.putExtra("date", "everyday");
            startActivity(intent);

        } else if (id == R.id.nav_weeklyList) {
            Intent intent = new Intent(this,WeeklyList.class);
            startActivity(intent);
        } else if (id == R.id.Nav_Notes) {
            Intent intent = new Intent(this,NotesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }



    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> values;


        class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtHeader;
            View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                txtHeader = (TextView) v.findViewById(R.id.ListText);
            }
        }

        public void add(int position, String item) {
            values.add(position, item);
            notifyItemInserted(position);
        }


        public MyAdapter(List<String> myDataset) {
            values = myDataset;
        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View v = inflater.inflate(R.layout.row_layout, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final String name = values.get(position);
            holder.txtHeader.setText(name);
            holder.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDescription(name);
                }
            });

            holder.txtHeader.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    remove(position);
                    try {

                        Cursor cr = db.rawQuery("select id from '" + dateString + "' where title = '" + name + "'", null);
                        cr.moveToFirst();
                        Integer id = cr.getInt(0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(getApplicationContext(), AlarmTrigger.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getApplicationContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.cancel(pendingIntent);

                        cr.close();

                        db.execSQL("delete from '" + dateString + "' where title = '" + name + "'");

                    }catch (Exception e) {
                        Log.e("DataBase error : ", e.toString());
                    }

                    return true;
                }
            });

        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public void showDescription(String name) {
            Intent intent = new Intent(MainActivity.this,ShowDescription.class);
            intent.putExtra("title",name);
            intent.putExtra("date",dateString);
            startActivity(intent);
        }

    }

}




