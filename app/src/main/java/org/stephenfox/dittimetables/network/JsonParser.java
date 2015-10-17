package org.stephenfox.dittimetables.network;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {


  public void parseJSON(String data) {

    try {
      JSONObject jsonObject = new JSONObject(data);
      String j = jsonObject.getString("courseName");
      Log.v("12345::", j);
    } catch (JSONException e) {

    }
  }
}
