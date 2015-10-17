package org.stephenfox.dittimetables.network;


/**
 * Use this class for downloading a list of courses titles and ids
 * which timetable are available for the app.
 */
public class CourseDownloader extends HttpDownloader {


  /**®
   * The URL which hold all information on each course.
   */
  public static final String coursesURL = "http://www.timothybarnard.org/timetables/courses.php";


  /**
   * Downloads all the JSON data from {@link #coursesURL}
   * which holds a course name and id.
   *
   * @param callback This object/callback will be messaged when the
   *                 course name and identifiers have been downloaded.
   */
  public void downloadCourseNamesAndIdentifiers(AsyncDownloader.HttpAsyncCallback callback) {
    AsyncDownloader asyncDownloader = new AsyncDownloader(this);
    asyncDownloader.download(CourseDownloader.coursesURL, callback);
  }
}
