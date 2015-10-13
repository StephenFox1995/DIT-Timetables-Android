package org.stephenfox.dittimetables.network;


import android.os.AsyncTask;

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

    public static final int STATUS_CODE_OK = 200;

    public String getHttpData(String urlString) {
        String dataStream;

        try {
            URL courseURL = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) courseURL.openConnection();

            if (urlConnection.getResponseCode() == STATUS_CODE_OK) {
                BufferedInputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                dataStream = stringBuilder.toString();

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataStream;
    }

}
