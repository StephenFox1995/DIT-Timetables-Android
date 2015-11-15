package org.stephenfox.dittimetables.timetable;

import android.content.Context;
import android.util.Log;

import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.network.WeekDownloader;

import java.util.ArrayList;

/**
 * Use this class to determine the source/ location
 * of where a courses timetable will be fetched i.e network or database.
 */
public class TimetableSourceRetriever {

  Context context;
  String courseCode;
  String courseID;
  TimetableRetrieverCallback callback;

  public TimetableSourceRetriever(Context context) {
    this.context = context;
  }


  /**
   * This method fetches a timetable from the appropriate source
   * either network or database.
   *
   * @param courseCode The course code of the timetable to fetch for e.g. DT228/3 etc.
   * @param courseID The id of the course on the server if needs be that it must be fetched there.
   *
   * @throws InvalidTimetableDataException This exception will be thrown if, when a timetable must be
   *                                       fetched from the server and their is insufficient data to
   *                                       constrict the timetable.
   **/
  public void fetchTimetable(String courseCode, String courseID, TimetableRetrieverCallback callback)
      throws InvalidTimetableDataException {

    this.courseCode = courseCode;
    this.courseID = courseID;
    // Query database for that course.
    // if in database return it.
    // Otherwise fetch from server.

    TimetableDatabase database = new TimetableDatabase(context);
    //if (database.timetableExists(courseCode)) {
      // Generate a timetable from database.
    //} else {
      String url = TimetableSourceRetriever.constructURLToDownloadTimetable(courseID);
      Log.d("SF", "URL: " + url);
      fetchTimetableFromServer(url, callback);
   // }
  }


  private void fetchTimetableFromServer(String url, final TimetableRetrieverCallback callback) {
    WeekDownloader weekDownloader = new WeekDownloader();
    weekDownloader.downloadWeekForCourse(url, new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        Timetable timetable = createTimetableFromNetworkData((String)data);
        callback.timetableRetrieved(timetable);
      }
    });
  }


  private Timetable createTimetableFromNetworkData(String data) {
    try {
      TimetableGenerator generator = new TimetableGenerator(parseJSON(data));
      return generator.generateTimetable(courseCode);
    } catch (InvalidTimetableDataException e) {

    }
    return null;
  }

  private ArrayList<TimetableSession> parseJSON(String JSONData) {
    JsonParser parser = new JsonParser();
    return parser.parseSessionsForTimetable(JSONData);
  }



  public static String constructURLToDownloadTimetable(int id) {
    return "http://timothybarnard.org/timetables/classes.php?courseID=" + id + "&semester=1";
  }

  public static String constructURLToDownloadTimetable(String id) {
    return "http://timothybarnard.org/timetables/classes.php?courseID=" + id + "&semester=1";
  }


  public interface TimetableRetrieverCallback {
    void timetableRetrieved(Timetable timetable);
  }
}
