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
   * @param callback A interface reference type that will receive the data
   *                 via :
   *                 {@link org.stephenfox.dittimetables.network.AsyncDownloader.HttpAsyncCallback}
   *                  finished(..) method.
   *                 Note: This callback will be on the main UI thread and not on the worker thread
   *                 that was spawned to fetched the http data.
   */
  public void downloadWeekForCourse(String url, AsyncDownloader.HttpAsyncCallback callback) {
    AsyncDownloader asyncDownloader = new AsyncDownloader(this);
    asyncDownloader.download(url, callback);
  }
}
