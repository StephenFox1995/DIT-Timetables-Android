package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;
import org.stephenfox.dittimetables.time.Time;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableDay;
import org.stephenfox.dittimetables.timetable.TimetableSession;
import org.stephenfox.dittimetables.timetable.TimetableSourceRetriever;

public class DayAssistantActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.day_assistant_activity);
    setup();
  }


  private void setup() {
    // TODO: This could be possible point of changing activity if there's no preferred timetable.
    TimetablePreferences preferences = new TimetablePreferences(this);
    String courseCode = preferences.getCourseCodePreference();
    String courseGroup = preferences.getCourseGroupPreference();

    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    sourceRetriever.fetchTimetable(courseCode, courseGroup,
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
      Log.d("SF", timetable.toString());
      this.sessions = timetable.getTimetableDay(day).getSessions();

    }

    @Override
    public int getCount() {
      TimetableDay timetableDay = timetable.getTimetableDay(day);
      return timetableDay.getSessionCount();
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
      View sessionDetailsView = convertView;

      if (sessionDetailsView == null) {
        sessionDetailsView = layoutInflater.inflate(R.layout.day_assistant_session, null);
      }
      return sessionDetailsView;
    }
  }
}
