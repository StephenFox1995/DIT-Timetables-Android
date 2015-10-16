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
import org.stephenfox.dittimetables.network.TimetableSession;

import java.util.ArrayList;

public class TimetableWeekActivity extends ListActivity {

  // TODO(stephenfox)
  // Should be a array of TimetableSessions:
  private ArrayList<TimetableSession> timetableSessions;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timetable_week_display);

    Intent intent = getIntent();

    this.timetableSessions = intent.getParcelableArrayListExtra("courseRequestURL");


    setListAdapter(new TimetableWeekListAdapter(this, timetableSessions));

  }


  class TimetableWeekListAdapter extends BaseAdapter {

    private ArrayList<TimetableSession> timetableSessions;
    private LayoutInflater inflater;

    public TimetableWeekListAdapter(Context context, ArrayList<TimetableSession> sessions) {
      this.timetableSessions = sessions;
      this.inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
      return timetableSessions.size();
    }

    @Override
    public Object getItem(int position) {
      return timetableSessions;
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
      TextView sessionNameTextView = (TextView)row.findViewById(R.id.sessionName);
      sessionNameTextView.setText(timetableSessions.get(position).getSessionName());

      TextView sessionTimeComponent = (TextView)row.findViewById(R.id.timeComponent);
      sessionTimeComponent.setText(timetableSessions.get(position).getStartTime());

      TextView sessionGroupsTextView = (TextView)row.findViewById(R.id.sessionGroups);
      sessionGroupsTextView.setText(timetableSessions.get(position).getSessionGroups()[0]);

      TextView sessionMasterTextView = (TextView)row.findViewById(R.id.sessionMaster);
      sessionMasterTextView.setText(timetableSessions.get(position).getSessionMaster());

      TextView sessionLocationTextView = (TextView)row.findViewById(R.id.sessionLocation);
      sessionLocationTextView.setText(timetableSessions.get(position).getSessionLocation());

      TextView sessionTypeTextView = (TextView)row.findViewById(R.id.sessionType);
      sessionTypeTextView.setText(timetableSessions.get(position).getSessionType());

      return row;
    }
  }

}
