package org.stephenfox.dittimetables.network;


import android.os.AsyncTask;
import android.util.Log;


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
    AsyncDownloader asyncDownloader = new AsyncDownloader(this);

    asyncDownloader.download(CourseDownloader.coursesURL, new HttpAsyncCallback() {
      public void finished(String s) {
        Log.v("12345::", s);
      }
    });
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
      return httpDownloader.getHttpData(params[0]);
    }

    public void download(String s, HttpAsyncCallback callback) {
      this.callback = callback;
      execute(s);

    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      callback.finished(s);
    }
  }


  /**
   * A private interface which allows us to
   * perform asynchronous callbacks when AsyncDownloader
   * has finished downloading all the data from the
   * server.
   */
  private interface HttpAsyncCallback {
    public void finished(String s);
  }


}
