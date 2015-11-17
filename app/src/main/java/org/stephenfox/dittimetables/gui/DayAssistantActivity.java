package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;
import org.stephenfox.dittimetables.time.Time;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableDay;
import org.stephenfox.dittimetables.timetable.TimetableSession;
import org.stephenfox.dittimetables.timetable.TimetableSourceRetriever;

import java.util.ArrayList;

public class DayAssistantActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setup();
  }


  private void setup() {
    // TODO: This could be possible point of changing activity if there's no preferred timetable.
    TimetablePreferences preferences = new TimetablePreferences(this);
    String courseCode = preferences.getCourseCodePreference();
    String courseGroup = preferences.getCourseGroupPreference();

    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    sourceRetriever.fetchTimetable(courseCode,
        new TimetableSourceRetriever.TimetableRetrieverCallback() {
          @Override
          public void timetableRetrieved(Timetable timetable) {
            setListAdapter(new SessionDetailsAdapter(DayAssistantActivity.this, timetable));
          }
        });
  }


  private class SessionDetailsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Timetable timetable;
    String currentDay;
    Day day;
    TimetableSession[] sessions;


    public SessionDetailsAdapter(Context context, Timetable timetable) {
      this.context = context;
      this.timetable = timetable;
      this.layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
      this.currentDay = Time.getCurrentDay();
      this.day = Day.stringToDay(currentDay);
      this.sessions = timetable.getTimetableDay(day).getSessions();
    }

    @Override
    public int getCount() {
      TimetableDay timetableDay = timetable.getTimetableDay(day);
      return determineIncludedSessions(timetableDay.getSessions()).length;
    }

    @Override
    public Object getItem(int position) {
      return sessions[position];
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View row = convertView;

      if (row == null) {
        row = layoutInflater.inflate(R.layout.day_assistant_session, null);
      }

      TextView timeRemainingTextView =
          (TextView)row.findViewById(R.id.session_detail_time_remaining);
      TextView sessionname = (TextView)row.findViewById(R.id.session_detail_sessioname);
      sessionname.setText(sessions[position].getSessionName());

      TextView sessionMaster = (TextView)row.findViewById(R.id.session_master);
      sessionMaster.setText(sessions[position].getSessionMaster());

      TextView sessionLocation = (TextView)row.findViewById(R.id.session_location);
      sessionLocation.setText(sessions[position].getSessionLocation());

      TextView sessionType = (TextView)row.findViewById(R.id.session_type);
      sessionType.setText(sessions[position].getSessionType());

      return row;

    }


    /**
     * Determines what sessions to show in the list. They may be all finished.*/
    private TimetableSession[] determineIncludedSessions(TimetableSession[] sessions) {
      ArrayList<TimetableSession> lSessions = new ArrayList<>();

      for (TimetableSession session : sessions) {
        if (session.isActive()) {
          lSessions.add(session);
        }
      }
      return lSessions.toArray(new TimetableSession[lSessions.size()]);
    }
  }
}
