package org.stephenfox.dittimetables.network;


import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {

  public String getHttpData(String urlString) {
    String data = null;

    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      if (conn.getResponseCode() == 200) {

      } else {

      }


    } catch (IOException e) {
      e.printStackTrace();

      Log.v("IOExceptionShite", "FAILED");
    }
    return data;
  }


}
