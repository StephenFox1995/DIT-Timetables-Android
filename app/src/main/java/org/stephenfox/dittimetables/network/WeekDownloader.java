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
   * @param callback The object to s*/
  public void downloadWeekForCourse(String url, AsyncDownloader.HttpAsyncCallback callback) {
    AsyncDownloader asyncDownloader = new AsyncDownloader(this);
    asyncDownloader.download(url, callback);
  }
}
