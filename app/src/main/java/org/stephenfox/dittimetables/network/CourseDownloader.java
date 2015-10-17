package org.stephenfox.dittimetables.network;


import android.os.AsyncTask;


public class CourseDownloader extends HttpDownloader {

  /**
   * The URL which hold all information on each course.
   */
  public static final String coursesURL = "http://www.timothybarnard.org/timetables/courses.php";


  /**
   * Downloads all the JSON data from {@link #coursesURL}
   * which holds a courseName and id.
   */
  public void downloadCourseNamesAndIdentifiers() {
    AsyncDownloader iDownloader = new AsyncDownloader(this);
    iDownloader.execute(CourseDownloader.coursesURL);
  }


  /**
   * Use to class to invoke methods on a HttpDownloader
   * class off the main ui thread.
   */
  private class AsyncDownloader extends AsyncTask<String, Void, String> {

    private HttpDownloader httpDownloader;


    public <T extends  HttpDownloader>AsyncDownloader(T httpDownloader) {
      this.httpDownloader = httpDownloader;
    }

    @Override
    protected String doInBackground(String... params) {
      return httpDownloader.getHttpData(params[0]);
    }
  }


}
