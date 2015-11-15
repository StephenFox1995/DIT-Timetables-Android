package org.stephenfox.dittimetables.network;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.TimetableSession;
import java.util.ArrayList;
import java.util.HashMap;


public class JsonParser {


  /**
   * Parses a JSON string for names and
   * identifiers for a course into JSON data.
   *
   * @return A hashmap containing the courses titles and identifiers.
   *         Note: The hashmap has a key of identifier and value
   *         of course name.
   */
  public HashMap<Integer, String> parseTitlesAndIdentifiers(String data) {
    HashMap<Integer, String> courseIdentifiersAndTitlesHash = new HashMap<>();

    try {
      JSONObject jsonObject = new JSONObject(data);
      JSONArray jsonArray = jsonObject.getJSONArray("objects");

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject o = jsonArray.getJSONObject(i);

        Integer courseId = o.getInt("id");
        String courseName = o.getString("courseName");

        courseIdentifiersAndTitlesHash.put(courseId, courseName);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    return courseIdentifiersAndTitlesHash;
  }


  /**
   * Parses a JSON string into an array of timetables sessions.
   *
   * @param data The string to parse.
   * @return An ArrayList of type TimetableSession.
   */
  public ArrayList<TimetableSession> parseSessionsForTimetable(String data) {

    ArrayList<TimetableSession> timetableSessions = new ArrayList<>();

    try {
      JSONObject jsonObject = new JSONObject(data);
      JSONArray jsonArray = jsonObject.getJSONArray("objects");

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject o = jsonArray.getJSONObject(i);

        Day day =  Day.intToDay(o.getInt("day"));
        String startTime = o.getString("start_time");
        String endTime = o.getString("end_time");
        String moduleName = o.getString("module_name");
        String roomNumber = o.getString("room_number");
        String[] groups = parseSubGroups(o.getString("sub_groups"));
        String teacher = o.getString("teacher");
        String type = o.getString("type");

        TimetableSession session = new TimetableSession(
            day, startTime, endTime, moduleName, groups, teacher, roomNumber, type);

        timetableSessions.add(session);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return timetableSessions;
  }


  private String[] parseSubGroups(String groups) { return groups.split(","); }


  public String parseCourseID(String data) {
    String courseID = "";

    try {
      JSONObject jsonObject = new JSONObject(data);
      JSONArray jsonArray = jsonObject.getJSONArray("objects");

      for (int i = 0; i < 1; i++) {
        JSONObject o = jsonArray.getJSONObject(i);
        courseID = o.getString("course_ID");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return courseID;
  }
}
