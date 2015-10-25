package org.stephenfox.dittimetables.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.timetable.TimetableSession;



public class SessionDetailsFragment extends Fragment {

  private String timeRemaining;
  private String sessionName;
  private TimetableSession timetableSession;


  /**
   * Use this method to construct a new instance.
   */
  public static SessionDetailsFragment newInstance(TimetableSession session) {
    SessionDetailsFragment fragment = new SessionDetailsFragment();
    fragment.setArguments(new Bundle());
    fragment.setTimetableSession(session);
    return fragment;
  }



  @Nullable
  @Override
  public View
  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_session_details, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }


  public void setTimetableSession(TimetableSession session) {
    this.timetableSession = session;
  }
}
