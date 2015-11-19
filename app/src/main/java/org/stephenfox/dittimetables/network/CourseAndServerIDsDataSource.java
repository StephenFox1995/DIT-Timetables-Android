package org.stephenfox.dittimetables.network;


import java.util.HashMap;

/**
 * This class acts as a temporary cache or store for the application
 * for the server keys, which provide the correct keys to download timetable
 * from the server.
 **/
public class CourseAndServerIDsDataSource {

  private static HashMap<String, Integer> timetableIdentifiersHash;

  public static HashMap<String, Integer> getHash() {
    return timetableIdentifiersHash;
  }

  public static void setTimetableIdentifiersHash(String jsonData) {
    JsonParser parser = new JsonParser();
    CourseAndServerIDsDataSource.timetableIdentifiersHash = parser.parseTimetableIdentifiers(jsonData);
  }


  public static String getTimetableIDForCourseCode(String courseCode) {
    return Integer.toString(timetableIdentifiersHash.get(courseCode));
  }

}
