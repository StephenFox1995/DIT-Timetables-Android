package org.stephenfox.dittimetables.network;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpDownloader {

    public String getHttpData(String urlString) {
        String data = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if (conn.getResponseCode() == 200) {

            } else {

            }


        }
        catch (IOException e) {
            e.printStackTrace();

            Log.v("IOExceptionShite", "FAILED");
        }
        return data;
    }


}
