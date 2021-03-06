package org.stephenfox.dittimetables.gui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.stephenfox.dittimetables.timetable.InvalidTimetableDataException;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableBuilder;
import org.stephenfox.dittimetables.timetable.TimetableSession;

public class SaveCourseFragment extends Fragment implements View.OnClickListener {

  private String courseID;
  private String courseCode;
  private CourseSavedCallback callback;


  public static SaveCourseFragment newInstance(String courseID, String courseCode) {
    SaveCourseFragment fragment = new SaveCourseFragment();
    Bundle args = new Bundle();
    args.putString("courseID", courseID);
    args.putString("courseCode", courseCode);
    fragment.setArguments(args);

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

    Bundle args = getArguments();
    this.courseID = args.getString("courseID");
    this.courseCode = args.getString("courseCode");


    Button cancelActionButton = (Button) getActivity().findViewById(R.id.cancel_button);
    Button saveCourseButton = (Button) getActivity().findViewById(R.id.save_course_button);

    cancelActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
      }
    });
    saveCourseButton.setOnClickListener(this);
  }


  @Override
  public void onClick(View v) {
    TimetableDatabase database = new TimetableDatabase(getActivity().getApplicationContext());

    if (!database.canAddTimetableToDatabase()) {
      Toast.makeText(getActivity().getApplicationContext(),
          "You already have a timetable saved!", Toast.LENGTH_SHORT).show();
      getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
      return;
    }

    String url = WeekDownloader.constructURLToDownloadTimetableWeek(courseID);
    WeekDownloader weekDownloader = new WeekDownloader();
    weekDownloader.downloadWeekForCourse(url, new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        if (data == null) {
          Toast.makeText(getActivity().getApplicationContext(),
              "Network error, could not download timetable.", Toast.LENGTH_SHORT).show();
          getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
        } else {
          try {
            Timetable timetable = generateTimetableForDatabase((String) data);
            beginDatabaseTransaction(timetable);
          } catch (InvalidTimetableDataException e) {
            Toast.makeText(getActivity().getApplicationContext(),
                "Timetable not available for course.", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
          }
        }
      }
    });
  }


  /**
   * Parses a string into a timetable
   *
   * @param data The data used to generate the timetable.
   * @return Timetable The newly generate Timetable object.
   *
   * @throws InvalidTimetableDataException this exception will be thrown if the
   *         the data used to generate the timetable is invalid.
   */
  private Timetable generateTimetableForDatabase(String data) throws InvalidTimetableDataException {
    JsonParser jsonParser = new JsonParser();
    TimetableSession[] sessions = jsonParser.parseSessionsForTimetable(data);
    TimetableBuilder builder = new TimetableBuilder(sessions);

    return builder.buildTimetable(courseCode);
  }



  /**
   * Attempts to insert the timetable to the database.
   * This method will notify the user via a Toast if the
   * insertion was successful.
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
          callback.fragmentHasSavedCourse(timetable.getCourseCode(), true);
        }
        else {
          Toast.makeText(getActivity().getApplicationContext(),
              "There was an error saving, please try again.", Toast.LENGTH_SHORT).show();
          getFragmentManager().beginTransaction().remove(SaveCourseFragment.this).commit();
        }
      }
    });
  }

  public void setCallback(CourseSavedCallback callback) {
    this.callback = callback;
  }

  public interface CourseSavedCallback {
    /**
     * A message is sent to a delegate when this fragment will be
     * removed from the view hierarchy.
     *
     * @param courseCode The course code of the timetable saved.
     * @param isRemoving A boolean value determining if the fragment
     *                   is in the process of being removed. i.e the
     *                   user no longer needs it.
     */
    void fragmentHasSavedCourse(String courseCode, boolean isRemoving);
  }

}
