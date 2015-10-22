package org.stephenfox.dittimetables.gui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.timetable.TimetableDay;


/**
 * Fragment which displays a day of the week
 * and the sessions belonging to that day.
 */
public class TimetableWeekPageFragment extends ListFragment {

  TimetableDay timetableDay;


  /**
   * Use this method to construct a new instance with a TimetableDay object.
   */
  public static TimetableWeekPageFragment newInstance(TimetableDay day) {
    Bundle args = new Bundle();

    TimetableWeekPageFragment fragment = new TimetableWeekPageFragment();
    fragment.setArguments(args);
    fragment.setTimetableDay(day);

    return fragment;
  }



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(
        R.layout.timetable_week_display, container, false);

    if (!timetableDay.containsSessions()) {
      TextView noSessionsForDay = (TextView)view.findViewById(R.id.no_courses_for_day);
      noSessionsForDay.setText("No classes today!");

    } else {
      setListAdapter(new TimetableWeekListAdapter(getActivity().getApplicationContext(), timetableDay));
    }
    return view;
  }



  private void displayNoSessionsView() {

  }

  private void setTimetableDay(TimetableDay timetableDay) {
    this.timetableDay = timetableDay;
  }




  class TimetableWeekListAdapter extends BaseAdapter {


    private TimetableDay day;
    private LayoutInflater inflater;


    public TimetableWeekListAdapter(Context context, TimetableDay day) {
      this.day = day;
      this.inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
      return this.day.getSessionCount();
    }


    @Override
    public Object getItem(int position) {
      return this.day.getSession(position);
    }


    @Override
    public long getItemId(int position) {
      return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      View row = convertView;

      if (row == null) {
        row = this.inflater.inflate(R.layout.timetable_session_row, null);
      }


      TextView sessionNameTextView = (TextView)row.findViewById(R.id.sessionName);
      sessionNameTextView.setText(day.getSession(position).getSessionName());

      TextView sessionTimeComponent = (TextView)row.findViewById(R.id.timeComponent);
      String startTime = day.getSession(position).getStartTime();
      String endTime = day.getSession(position).getEndTime();
      String timeComponent = startTime + " - " + endTime;
      sessionTimeComponent.setText(timeComponent);

      TextView sessionGroupsTextView = (TextView)row.findViewById(R.id.sessionGroups);
      sessionGroupsTextView.setText(day.getSession(position).getSessionGroups()[0]);

      TextView sessionMasterTextView = (TextView)row.findViewById(R.id.sessionMaster);
      sessionMasterTextView.setText(day.getSession(position).getSessionMaster());

      TextView sessionLocationTextView = (TextView)row.findViewById(R.id.sessionLocation);
      sessionLocationTextView.setText(day.getSession(position).getSessionLocation());

      TextView sessionTypeTextView = (TextView)row.findViewById(R.id.sessionType);
      sessionTypeTextView.setText(day.getSession(position).getSessionType());

      return row;
    }
  }
}
