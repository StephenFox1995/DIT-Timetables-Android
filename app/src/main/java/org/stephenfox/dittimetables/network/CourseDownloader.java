package org.stephenfox.dittimetables.network;


import android.os.AsyncTask;
import org.stephenfox.dittimetables.gui.HttpAsyncCallback;


public class CourseDownloader extends HttpDownloader {


  /**
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
  public void downloadCourseNamesAndIdentifiers(HttpAsyncCallback callback) {
    AsyncDownloader asyncDownloader = new AsyncDownloader(this);
    asyncDownloader.download(CourseDownloader.coursesURL, callback);
  }


  /**
   * Use to class to invoke methods on a HttpDownloader
   * class off the main ui thread.
   */
  private class AsyncDownloader extends AsyncTask<String, Void, String> {

    private HttpDownloader httpDownloader;
    private HttpAsyncCallback callback;


    public <T extends  HttpDownloader>AsyncDownloader(T httpDownloader) {
      this.httpDownloader = httpDownloader;
    }

    @Override
    protected String doInBackground(String... params) {
      return httpDownloader.getHttpData(params[0]); // Download our http data.
    }


    /**
     * Downloads the course names and identifiers from the server.
     *
     * @param url The url string to fetch the data from.
     * @param callback The object who will be called when all
     *                 the information has been retrieved from the
     *                 http request.
     */
    public void download(String url, HttpAsyncCallback callback) {
      this.callback = callback;
      execute(url);
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      callback.finished(s);
    }
  }
}
