package org.stephenfox.dittimetables.network;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class JsonParser {

  private HashMap<Integer, String> courseIdentifiersAndTitles;

  /**
   * Parses a string for names and
   * identifiers for a course into JSON data.
   *
   * @return A hashmap containing the courses titles and identifiers.
   *         Note: The hashmap has a key of identifier and value
   *         of course name.
   */
  public HashMap<Integer, String> parseTitlesAndIdentifiers(String data) {

    try {
      this.courseIdentifiersAndTitles = new HashMap<>();

      JSONObject jsonObject = new JSONObject(data);
      JSONArray jsonArray = jsonObject.getJSONArray("objects");

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject o = jsonArray.getJSONObject(i);

        Integer courseId = o.getInt("id");
        String courseName = o.getString("courseName");

        courseIdentifiersAndTitles.put(courseId, courseName);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return courseIdentifiersAndTitles;
  }
}
