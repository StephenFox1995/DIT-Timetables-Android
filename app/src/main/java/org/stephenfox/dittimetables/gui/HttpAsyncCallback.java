package org.stephenfox.dittimetables.gui;

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
  public void finished(String data);
}
