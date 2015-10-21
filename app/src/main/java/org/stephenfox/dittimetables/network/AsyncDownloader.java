package org.stephenfox.dittimetables.network;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

/**
 * Use to class to wrap any class that extends HttpDownloader.
 * so it performs all requests on a worker thread and not on the
 * main ui thread.
 */
public class AsyncDownloader extends AsyncTask<String, Void, String> {

  private HttpDownloader httpDownloader;
  private HttpAsyncCallback callback;private Runnable block;


  public <T extends  HttpDownloader>AsyncDownloader(T httpDownloader) {
    this.httpDownloader = httpDownloader;
  }

  @Override
  protected String doInBackground(String... params) {
    if(Looper.myLooper() == Looper.getMainLooper()) {
      Log.w("Async:", "Were on main thread.");
    } else {
      Log.w("Async", "Not on the main thread.");
    }
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


  /**
   * Use this interface to perform
   * asynchronous callbacks, typically from
   * a http request.
   */
  public interface HttpAsyncCallback {

    /**
     * Called when a http request has finished.
     * @param data The data retrieved from the http request.
     */
    void finished(String data);
  }


}
