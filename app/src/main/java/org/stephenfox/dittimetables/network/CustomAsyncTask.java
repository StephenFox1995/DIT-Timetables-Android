package org.stephenfox.dittimetables.network;

import android.os.AsyncTask;


public class CustomAsyncTask extends AsyncTask {

  private AsyncCallback callback;
  private AsyncExecutableForCallback executableWithCallback;
  private AsyncExecutable executable;
  private boolean shouldMessageCallback = false;


  public CustomAsyncTask() {
  }

  /**
   * Use this method to execute code on a background thread.
   *
   * This method provides the capability to execute code by
   * passing a
   *  #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncExecutableForCallback }
   * reference, which is the code that will be called on the background thread.
   *
   * A callback, is then called when the executableWithCallback code has finished. If there's no need
   * for a callback then use
   * #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncCallback }
   *
   * @param executable A reference to a AsyncExecutable interface where the code
   *                   to be executed on a background thread will be executed.
   *
   * @param callback A reference to a AsyncCallback interface where any data that results from
   *                 the background thread will be passed e.g. http data etc.
   *
   **/
  public void
  doCallbackTask(AsyncExecutableForCallback executable, AsyncCallback callback) {
    this.executableWithCallback = executable;
    this.callback = callback;
    this.shouldMessageCallback = true;
    execute();
  }

  /**
   * This method will perform work on a background worker thread via a reference to
   * #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncExecutable}.
   *
   * @param executable A reference to a AsyncExecutable where work will be performed
   *                   on a background worker thread.
   **/
  public void doTask(AsyncExecutable executable) {
    this.executable = executable;
    this.callback = null;
    this.shouldMessageCallback = false;
    execute();
  }


  @Override
  protected Object doInBackground(Object[] params) {
    if (shouldMessageCallback) {
      return executableWithCallback.executeAsync();
    } else {
      return executable.execute();
    }
  }


  @Override
  protected void onPostExecute(Object o) {
    if (shouldMessageCallback) {
      callback.finished(o);
    }
  }


  /**
   * Use this interface to perform
   * asynchronous callbacks.
   */
  public interface AsyncCallback {
    /**
     * Called when a work done on a background worker thread has finished.
     * @param data The data to be passed via a callback.
     */
    void finished(Object data);

  }


  /**
   * Use this interface to perform work on a background worker thread that will then
   * pass the result via a callback
   */
  public interface AsyncExecutableForCallback {
    /**
     * This method is called to execute code on a background worker thread.
     * @return Object To return data that has resulted from
     *           the work done on the background thread so it can be passed via a
     *           #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncCallback }.
     *           For example if one wanted to get some data from a http request to a server on the
     *           background thread, they could return the result of that, which will be passed via
     *           a callback.
     */
    Object executeAsync();
  }


  public interface AsyncExecutable {
    /**
     * This method is called to execute code on a background thread
     */
    Object execute();
  }

}
