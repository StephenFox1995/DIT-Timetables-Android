package org.stephenfox.dittimetables.gui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.database.DatabaseTransactionStatus;
import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.network.WeekDownloader;
import org.stephenfox.dittimetables.timetable.EmptySessionsArrayException;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableGenerator;
import org.stephenfox.dittimetables.timetable.TimetableSession;

import java.util.ArrayList;

public class SaveCourseFragment extends Fragment {

  private Button saveCourseButton;
  private Button cancelActionButton;
  private String urlForCourseToSave;

  public static SaveCourseFragment newInstance() {
    SaveCourseFragment fragment = new SaveCourseFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.save_course_fragment, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    cancelActionButton = (Button)getActivity().findViewById(R.id.cancel_button);
    saveCourseButton = (Button)getActivity().findViewById(R.id.save_course_button);

    cancelActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
      }
    });

    saveCourseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        WeekDownloader weekDownloader = new WeekDownloader();
        weekDownloader.downloadWeekForCourse(urlForCourseToSave, new CustomAsyncTask.AsyncCallback() {
          @Override
          public void finished(Object data) {
            if (data == null) {
              Toast.makeText(getActivity().getApplicationContext(),
                  "Network error, could not download timetable.", Toast.LENGTH_SHORT).show();
              getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
            } else {
              Timetable timetable = generateTimetableForDatabase((String)data);
              beginDatabaseTransaction(timetable);
            }
          }
        });
      }
    });
  }


  /**
   * Parses a string into a timetable
   *
   * @param data The data used to generate the timetable.
   * @return Timetable The newly generate Timetable object.
   */
  private Timetable generateTimetableForDatabase(String data) {
    JsonParser jsonParser = new JsonParser();
    ArrayList<TimetableSession> sessions = jsonParser.parseSessionsForTimetable((String)data);
    Timetable timetable = null;

    try {
      TimetableGenerator generator = new TimetableGenerator(sessions);
      timetable = generator.generateTimetable();
      Log.d("timetable", timetable.toString());
    } catch (EmptySessionsArrayException e) {
      // TODO: Notify user of error.
      e.printStackTrace();
    }
    return timetable;
  }


  /**
   * Attempts to insert the timetable to the database.
   *
   * @param timetable The timetable to insert into the database.
   **/
  private void beginDatabaseTransaction(final Timetable timetable) {
    CustomAsyncTask customAsyncTask = new CustomAsyncTask();

    customAsyncTask.doCallbackTask(new CustomAsyncTask.AsyncExecutableForCallback() {
      @Override
      public Object executeAsync() {
        TimetableDatabase database = new TimetableDatabase(getActivity().getApplicationContext());
        database.open();
        DatabaseTransactionStatus status = database.addTimetable(timetable);
        database.close();
        return status;
      }
    }, new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        if (data == DatabaseTransactionStatus.Success) {
          Toast.makeText(getActivity().getApplicationContext(),
              "Timetable successfully save to device!", Toast.LENGTH_SHORT).show();
          getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
        }
      }
    });
  }


  public void setUrlForCourseToSave(String urlForCourseToSave) {
    this.urlForCourseToSave = urlForCourseToSave;
  }

}
