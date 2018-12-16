package com.rvcoderdevstudio.atharv.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class everydayList extends AppCompatActivity {

    String dateString,timeStr;
    Integer timeInt;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everyday_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dateString = bundle.getString("date");

        List<String> title = new ArrayList<>();
        List<Integer> time = new ArrayList<>();

        try {

            db = openOrCreateDatabase("TimeTable", MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("select title from '" + dateString + "'", null);
            cursor.moveToFirst();
            do {
                title.add(cursor.getString(0));

            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            Log.e("DataBase error : ", e.toString());
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.everyday_recyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(title);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            db.close();}
        catch (Exception e){

        }

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

            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

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
                    int id = cr.getInt(0);
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
            Intent intent = new Intent(everydayList.this,ShowDescription.class);
            intent.putExtra("title",name);
            intent.putExtra("date",dateString);
            startActivity(intent);
        }

    }

    public void AddItem_everyday(View view) {
        Intent i = new Intent(this, SetTimeActivity.class);
        i.putExtra("date",dateString);
        startActivity(i);
    }

}