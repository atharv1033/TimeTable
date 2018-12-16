package com.rvcoderdevstudio.atharv.timetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeeklyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_list);

        List<String> weekDays = new ArrayList<String>();
        weekDays.add("Sunday");
        weekDays.add("Monday");
        weekDays.add("Tuesday");
        weekDays.add("Wednesday");
        weekDays.add("Thursday");
        weekDays.add("Friday");
        weekDays.add("Saturday");

        RecyclerView weeklyList = findViewById(R.id.weeklyList);
        weeklyList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        weeklyList.setLayoutManager(mLayoutManager);
        MyAdapter adapter = new MyAdapter(weekDays);
        weeklyList.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
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
                    GetList(name);
                }
            });

        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public void GetList(String name) {
            Intent intent = new Intent(WeeklyList.this,GetList.class);
            intent.putExtra("date",name);
            startActivity(intent);
        }

    }

}
