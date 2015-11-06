package org.stephenfox.dittimetables.network;

import android.os.AsyncTask;


public class CustomAsyncTask extends AsyncTask {

  private AsyncCallback callback;
  private AsyncExecutable executable;
  private boolean shouldMessageCallback = false;


  public CustomAsyncTask() {
  }

  /**
   * Use this method to execute code on a background thread.
   *
   * This method provides the capability to execute code by
   * passing a #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncExecutable }
   * reference, which is the code that will be called on the background thread.
   *
   * A callback, is then called when the executable code has finished. If there's no need
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
  doCallbackTask(AsyncExecutable executable, AsyncCallback callback) {
    this.executable = executable;
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
    return executable.executeAsync();
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
  public interface AsyncCallback<T> {
    /**
     * Called when a work done on a background worker thread has finished.
     * @param data The data to be passed via a callback.
     */
    void finished(T data);

  }

  /**
   * Use this interface to perform work on a background worker thread.
   */
  public interface AsyncExecutable<T> {
    /**
     * This method is called when code is to be execute on a background worker thread.
     * @return T Generally one, would want to return data that has resulted from
     *           the work done on the background thread so it can be passed via a
     *           #{@link org.stephenfox.dittimetables.network.CustomAsyncTask.AsyncCallback }.
     */
    T executeAsync();
  }
}
