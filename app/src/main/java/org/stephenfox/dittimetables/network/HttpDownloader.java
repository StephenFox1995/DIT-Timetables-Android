package org.stephenfox.dittimetables.network;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {

  /**
   * Retrieves data from a url.
   *
   * Note: this method must be called on
   * a separate thread and not on the main ui thread.
   * Otherwise a NetworkOnMainThreadException will be thrown.
   */
  public String getHttpData(String urlString) {
    String dataString = null;

    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      if (conn.getResponseCode() == 200) {

        BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
          stringBuilder.append(line);
        }

        dataString = stringBuilder.toString();
        conn.disconnect();

        Log.v("12345::", dataString);

      }
    } catch (IOException e) {
      e.printStackTrace();

      Log.v("IOException", "FAILED");
    }
    return dataString;
  }


}
