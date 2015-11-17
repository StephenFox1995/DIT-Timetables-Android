package org.stephenfox.dittimetables.timetable;


import org.stephenfox.dittimetables.network.JsonParser;

import java.util.HashMap;


public class TimetableIDWrapper {

  private static HashMap<String, Integer> timetableIdentifiersHash;

  public static HashMap<String, Integer> getHash() {
    return timetableIdentifiersHash;
  }

  public static void setTimetableIdentifiersHash(String jsonData) {
    JsonParser parser = new JsonParser();
    TimetableIDWrapper.timetableIdentifiersHash = parser.parseTimetableIdentifiers(jsonData);
  }

  public static int getTimetableIDForCourseCode(String courseCode) {
    return timetableIdentifiersHash.get(courseCode);
  }
}
