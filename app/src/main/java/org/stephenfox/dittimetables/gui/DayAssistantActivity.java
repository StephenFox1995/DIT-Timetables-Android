package org.stephenfox.dittimetables.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;
import org.stephenfox.dittimetables.time.Time;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableDay;
import org.stephenfox.dittimetables.timetable.TimetableSession;
import org.stephenfox.dittimetables.timetable.TimetableSourceRetriever;
import org.stephenfox.dittimetables.utilities.Utilities;

import java.util.ArrayList;

public class DayAssistantActivity extends AppCompatActivity {

  ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.day_assistant_activity);
    listView = (ListView)findViewById(R.id.list);

    TimetablePreferences preferences = new TimetablePreferences(this);
    String courseGroup = preferences.getCourseGroupPreference();

    setTitle(courseGroup);
    setup();
  }



  private void setup() {
    // TODO: This could be possible point of changing activity if there's no preferred timetable.
    TimetablePreferences preferences = new TimetablePreferences(this);
    String courseCode = preferences.getCourseCodePreference();

    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    sourceRetriever.fetchTimetable(courseCode,
        new TimetableSourceRetriever.TimetableRetrieverCallback() {
          @Override
          public void timetableRetrieved(Timetable timetable) {
            listView.setAdapter(new SessionDetailsAdapter(DayAssistantActivity.this, timetable));
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

      int colourForHeader = determineColourForSessionDetails(sessions[position]);
      LinearLayout layout = (LinearLayout)row.findViewById(R.id.session_details_header);
      layout.setBackgroundColor(colourForHeader);

      TextView timeRemainingTextView =
          (TextView)row.findViewById(R.id.session_detail_time_remaining);
      timeRemainingTextView.setText(stringForTimeComponent(sessions[position]));

      TextView sessionname = (TextView)row.findViewById(R.id.session_detail_sessionname);
      sessionname.setText(sessions[position].getSessionName());

      TextView sessionMaster = (TextView)row.findViewById(R.id.session_master);
      sessionMaster.setText(sessions[position].getSessionMaster());

      TextView sessionLocation = (TextView)row.findViewById(R.id.session_location);
      sessionLocation.setText(sessions[position].getSessionLocation());

      TextView sessionType = (TextView)row.findViewById(R.id.session_type);
      sessionType.setText(sessions[position].getSessionType());

      TextView timeComponent = (TextView)row.findViewById(R.id.session_time);
      String time = sessions[position].getStartTime() + " - " + sessions[position].getEndTime();
      timeComponent.setText(time);

      return row;
    }


    /**
     * Determines what sessions to show in the list. They may be all finished.
     **/
    private TimetableSession[] determineIncludedSessions(TimetableSession[] sessions) {
      ArrayList<TimetableSession> lSessions = new ArrayList<>();
      Day today = Day.stringToDay(Time.getCurrentDay());

      for (TimetableSession session : sessions) {
        if (session.isActive() || session.getDay() == today) {
          lSessions.add(session);
        }
      }
      return lSessions.toArray(new TimetableSession[lSessions.size()]);
    }
  }

  private int determineColourForSessionDetails(TimetableSession session) {
    if (session.isActive()) {
      return ContextCompat.getColor(getApplicationContext(), R.color.sessiom_detail_active_green);
    } else {
      return ContextCompat.getColor(getApplicationContext(), R.color.session_details_view_blue);
    }
  }

  private String stringForTimeComponent(TimetableSession session) {
    float currentTime =
        Float.parseFloat(Utilities.stringWithReplacedIndex(Time.getCurrentTime(), '.', 2));
    float startTime =
        Float.parseFloat(Utilities.stringWithReplacedIndex(session.getStartTime(), '.', 2));
    float endTime =
        Float.parseFloat(Utilities.stringWithReplacedIndex(session.getEndTime(), '.', 2));

    if (session.isActive()) {
      float timeRemaining = endTime - currentTime;
      return timeRemaining + " remaining.";
    } else {
      float timeUntil = startTime - currentTime;
      return timeUntil + " until.";
    }
  }
}
