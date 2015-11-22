package org.stephenfox.dittimetables.timetable;

import android.content.Context;
import android.util.Log;

import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.network.CourseAndServerIDsDataSource;
import org.stephenfox.dittimetables.network.WeekDownloader;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;

/**
 * Use this class to determine the source/ location
 * of where a courses timetable will be fetched i.e network or database.
 */
public class TimetableSourceRetriever {

  Context context;
  String courseCode;
  TimetableRetrieverCallback callback;
  TimetableDatabase database;

  public TimetableSourceRetriever(Context context) {
    this.context = context;
  }


  /**
   * This method fetches a timetable from the appropriate source
   * either network or database.
   *
   * @param courseCode The course code of the timetable to fetch for e.g. DT228/3 etc.
   * @param callback This will be called when the timetable has been found either from the server or
   *                 the database. A null value will be passed in the event a timetable cannot
   *                 be created, i.e there's insufficient information for it most likely when coming
   *                 from the server.
   **/
  public void
  fetchTimetable(String courseCode, TimetableRetrieverCallback callback) {
    this.courseCode = courseCode;
    this.callback = callback;

    database = new TimetableDatabase(context);

    if (database.timetableExists(courseCode)) {
      fetchTimetableFromDatabase();
    } else {
      String courseID = CourseAndServerIDsDataSource.getTimetableIDForCourseCode(courseCode);
      String url = WeekDownloader.constructURLToDownloadTimetableWeek(courseID);
      fetchTimetableFromServer(url);
    }
  }



  /**
   * A helper method to fetch a timetable from the database.
   * This will message the callback, when fetched.
   */
  private void fetchTimetableFromDatabase() {
    TimetableSession[] sessions;

    // Find the courseGroup that is preferred by the user.
    String courseGroup = TimetablePreferences.getCourseGroupPreference(context);

    if (courseGroup != null) {
       sessions = database.getSessionForGroup(courseCode, courseGroup);
    } else {
      sessions = database.getSessions(courseCode);
    }

    for (TimetableSession session : sessions) {
      Log.d("SFNEW", session.toString());
    }

    try {
      TimetableBuilder builder = new TimetableBuilder(sessions);
      callback.timetableRetrieved(builder.buildTimetable(courseCode));
    } catch (InvalidTimetableDataException e) {
      Log.e("SF", "Invalid timetable data exception thrown.");
      callback.timetableRetrieved(null);
    }
  }


  /**
   * A helper method to fetch timetable data from the server. When found or, possibly not found the
   * callback will be messaged.
   *
   * @param url The url to download the timetable from the server.
   */
  private void fetchTimetableFromServer(String url) {
    WeekDownloader weekDownloader = new WeekDownloader();
    weekDownloader.downloadWeekForCourse(url, new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        Timetable timetable = createTimetableFromNetworkData((String)data);
        callback.timetableRetrieved(timetable);
      }
    });
  }


  /**
   * Creates a Timetable object from json data from the server.
   *
   * @param data The json data.
   * @return A new timetable instance if the json data was sufficient to create the timetable
   *         otherwise, null value will be passed.
   **/
  private Timetable createTimetableFromNetworkData(String data) {
    try {
      TimetableBuilder builder = new TimetableBuilder(parseJSON(data));
      return builder.buildTimetable(courseCode);
    } catch (InvalidTimetableDataException e) {
      return null;
    }
  }


  private TimetableSession[] parseJSON(String JSONData) {
    JsonParser parser = new JsonParser();
    return parser.parseSessionsForTimetable(JSONData);
  }


  public interface TimetableRetrieverCallback {
    void timetableRetrieved(Timetable timetable);
  }
}
