package org.stephenfox.dittimetables.gui;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;

public class TimetableWeekDisplay extends ListActivity {

    private String courseRequestURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_week_display);

        //Intent intent = getIntent();
        //this.courseRequestURL = intent.getStringExtra("courseRequestURL");
    }



    class TimetableWeekListAdapter extends BaseAdapter {

        private String sessionName = null;
        private LayoutInflater inflater;


        public TimetableWeekListAdapter(Context context, String name) {
            this.sessionName = name;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return sessionName;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;

            if (row == null) {
                inflater.inflate(R.layout.timetable_session_row, null);
            }

            TextView sessionNameTextView = (TextView)row.findViewById(R.id.sessionName);
            sessionNameTextView.setText(sessionName);
            return sessionNameTextView;
        }
    }

}
