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

import junit.framework.Assert;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.TimetableSession;

import java.util.ArrayList;

public class TimetableWeekDisplay extends ListActivity {

    // TODO(stephenfox)
    // Should be a array of TimetableSessions:
    private TimetableSession timetableSession;

    private ArrayList<TimetableSession> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_week_display);

        Intent intent = getIntent();

        this.a = intent.getParcelableExtra("courseRequestURL");

        this.timetableSession = a.get(0);
        setListAdapter(new TimetableWeekListAdapter(this, timetableSession));
    }



    class TimetableWeekListAdapter extends BaseAdapter {

        private TimetableSession timetableSession;
        private LayoutInflater inflater;

        public TimetableWeekListAdapter(Context context, TimetableSession session) {
            this.timetableSession = session;
            this.inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return timetableSession;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;

            if (row == null) {
                row = inflater.inflate(R.layout.timetable_session_row, null);
            }
            /* TODO: (stephenfox):
            * Use full time component*/
            TextView sessionTimeComponent = (TextView)row.findViewById(R.id.timeComponent);
            sessionTimeComponent.setText(timetableSession.getStartTime());

            TextView sessionNameTextView = (TextView)row.findViewById(R.id.sessionName);
            sessionNameTextView.setText(timetableSession.getSessionName());

            return row;
        }
    }

}
