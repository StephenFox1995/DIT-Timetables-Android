package org.stephenfox.dittimetables.network;

/**
 * Use this class for downloading a courses
 * timetable for the week
 */
public class WeekDownloader extends HttpDownloader {



  /**
   * Downloads all the timetable information for a
   * course for the week.
   *
   * @param url The url to fetch the timetable information
   * @param callback A interface reference type that will receive the data via :
   *                 {@link CustomAsyncTask.AsyncCallback}
   *                  finished(..) method.
   *
   *                 Note: This callback will be on the main UI thread and not on the worker thread
   *                 that was spawned to fetched the http data.
   */
  public void downloadWeekForCourse(final String url, CustomAsyncTask.AsyncCallback callback) {
    CustomAsyncTask asyncTask = new CustomAsyncTask();
    asyncTask.doCallbackTask(new CustomAsyncTask.AsyncExecutableForCallback() {
      @Override
      public String executeAsync() {
        return getHttpData(url);
      }
    }, callback);
  }


  public static String constructURLToDownloadTimetableWeek(String id) {
    return "http://timothybarnard.org/timetables/classes.php?courseID=" + id + "&semester=1";
  }
}
