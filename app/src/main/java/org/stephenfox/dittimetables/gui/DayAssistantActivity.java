package org.stephenfox.dittimetables.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.stephenfox.dittimetables.timetable.SessionStatus;
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

    String courseGroup = TimetablePreferences.getCourseGroupPreference(this);
    if (courseGroup != null) {
      setTitle(courseGroup);
      setup();
    } else {
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.settings, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings:
        displaySettingsActivity();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setup() {
    String courseCode = TimetablePreferences.getCourseCodePreference(this);

    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    sourceRetriever.fetchTimetable(courseCode,
        new TimetableSourceRetriever.TimetableRetrieverCallback() {
          @Override
          public void timetableRetrieved(Timetable timetable) {
            setListAdapter(timetable);
          }
        });
  }

  /**
   * This sets the list adapter if there are any sessions to fill it with
   * otherwise, displays view to user that there are no sessions for that day.
   **/
  private void setListAdapter(Timetable timetable) {
    Day today = Day.stringToDay(Time.getCurrentDay());

    if (timetable != null && timetable.containsDay(today)) {
      if (timetable.getTimetableDay(today).containsSessions()) {
        listView.setAdapter(new SessionDetailsAdapter(DayAssistantActivity.this, timetable));
      }
      return;
    }

    Log.v("SF", "No sessions today");
    TextView noSessionsForToday = (TextView)findViewById(R.id.no_sessions);
    noSessionsForToday.setText("All done for the day.");
  }



  private void displaySettingsActivity() {
    SettingsActivity settingsActivity = new SettingsActivity();
    settingsActivity.setSettingsCallback(new SettingsActivity.SettingsCallback() {
      @Override
      public void timetableRemovedCallback() {
        finish();
      }
    });

    Intent settingsActivityIntent = new Intent(this, settingsActivity.getClass());
    startActivity(settingsActivityIntent);
  }


  private class SessionDetailsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Timetable timetable;
    String currentDay;
    Day today;
    TimetableSession[] sessions;


    public SessionDetailsAdapter(Context context, Timetable timetable) {
      this.context = context;
      this.timetable = timetable;
      this.layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
      this.currentDay = Time.getCurrentDay();
      this.today = Day.stringToDay(currentDay);
      this.sessions = timetable.getTimetableDay(today).getSessions();
    }


    @Override
    public int getCount() {
      TimetableDay timetableDay = timetable.getTimetableDay(today);
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
     * In that case none will be included.
     **/
    private TimetableSession[] determineIncludedSessions(TimetableSession[] sessions) {

      ArrayList<TimetableSession> lSessions = new ArrayList<>();
      Day today = Day.stringToDay(Time.getCurrentDay());

      for (TimetableSession session : sessions) {
        if (session.isActiveForDay(today)) {
          if (session.isActive(getCurrentTime()) ||
              session.timeStatus(getCurrentTime()) == SessionStatus.Later) {
            lSessions.add(session);
          }
        }
      }
      return lSessions.toArray(new TimetableSession[lSessions.size()]);
    }
  }


  private int determineColourForSessionDetails(TimetableSession session) {
    if (session.isActive(getCurrentTime())) {
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

    if (session.isActive(getCurrentTime())) {
      return "NOW: " + (endTime - currentTime);
    } else if (session.timeStatus(getCurrentTime()).equals(SessionStatus.Later)) {
      return "LATER: " + (startTime - currentTime);
    } else if (session.timeStatus(getCurrentTime()).equals(SessionStatus.Finished)){
      return "FINISED";
    } else {
      return "";
    }
  }

  private float convertToHoursAndMinutes(float time) {
    return ((time / 100) * 60);
  }


  private String formatTimeString(float time) {
    float hoursAndMinutesValue = convertToHoursAndMinutes(time);
    String formattedString = String.format("%.2f", hoursAndMinutesValue);
    String hours = formattedString.split("\\.")[0];
    char minutes = formattedString.toCharArray()[formattedString.length() - 2];

    return hours + "."  + minutes + "hr(s)";
  }


  private float getCurrentTime() {
    return Float.parseFloat(Utilities.stringWithReplacedIndex(Time.getCurrentTime(), '.', 2));
  }
}
