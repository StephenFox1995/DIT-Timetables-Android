package org.stephenfox.dittimetables.gui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.timetable.SessionStatus;
import org.stephenfox.dittimetables.timetable.TimetableDay;
import org.stephenfox.dittimetables.timetable.TimetableSession;


/**
 * Fragment which displays a today of the week
 * and the sessions belonging to that today.
 */
public class TimetableWeekPageFragment extends ListFragment {


  private TimetableDay timetableDay;


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

  public TimetableWeekPageFragment() {}



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(
        R.layout.timetable_week_display, container, false);

    if (!timetableDay.containsSessions()) {
      TextView noSessionsForDay = (TextView)view.findViewById(R.id.no_courses_for_day);
      noSessionsForDay.setText("No classes " + timetableDay.getDay().toString() + "!");
    } else {
      setListAdapter(new TimetableWeekListAdapter(getActivity().getApplicationContext(), timetableDay));
    }
    return view;
  }


  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    //addFragmentToViewHierarchy(this.timetableDay.getSession(position));
  }


//  private void addFragmentToViewHierarchy(TimetableSession session) {
//    FragmentManager fragmentManager = getFragmentManager();
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//    SessionDetailsFragment sessionDetailsFragment = SessionDetailsFragment.newInstance(session);
//
//    fragmentTransaction.add(R.id.session_detail_fragment, sessionDetailsFragment);
//    fragmentTransaction.commit();
//  }


  private void setTimetableDay(TimetableDay timetableDay) {
    this.timetableDay = timetableDay;
  }




  private class TimetableWeekListAdapter extends BaseAdapter {

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

      int colorForRow = colourForSessionStatus(determineSessionStatus(day.getSession(position)));

      View sessionStatus = row.findViewById(R.id.session_status);
      sessionStatus.setBackgroundColor(colorForRow);

      TextView sessionNameTextView = (TextView)row.findViewById(R.id.session_name);
      sessionNameTextView.setText(day.getSession(position).getSessionName());
      sessionNameTextView.setTextColor(colorForRow);

      TextView sessionTimeComponent = (TextView)row.findViewById(R.id.time_component);
      String startTime = day.getSession(position).getStartTime();
      String endTime = day.getSession(position).getEndTime();
      String timeComponent = startTime + " - " + endTime;
      sessionTimeComponent.setText(timeComponent);
      sessionTimeComponent.setTextColor(colorForRow);

      TextView sessionLocationTextView = (TextView)row.findViewById(R.id.sessionLocation);
      sessionLocationTextView.setText(day.getSession(position).getSessionLocation());
      sessionLocationTextView.setTextColor(colorForRow);

      return row;
    }


    /**
     * Determines the Status of a session. i.e If its finished, has to start etc.
     * Note: The today for the session must match the actual today of the week
     * in reality.
     * @return SessionStatus The status for a given session.
     */
    private SessionStatus determineSessionStatus(TimetableSession session) {
      return session.timeStatus();
    }


    private int colourForSessionStatus(SessionStatus status) {
      switch (status) {
        case Active:
          return ContextCompat.getColor(getContext(), R.color.timetable_row_green);
        case Finished:
          return ContextCompat.getColor(getContext(), R.color.timetable_row_red);
        case Later:
          return ContextCompat.getColor(getContext(), R.color.timetable_row_gray);
        case UnAssociatedDay:
          return ContextCompat.getColor(getContext(), R.color.timetable_row_gray);
        default:
          return 0;
      }
    }
  }
}
