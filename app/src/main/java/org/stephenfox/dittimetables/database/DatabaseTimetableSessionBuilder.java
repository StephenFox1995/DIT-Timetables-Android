package org.stephenfox.dittimetables.database;

import android.database.Cursor;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.TimetableSession;


/**
 * Use this class to build session that have been retrieved from the database.
 * */
public class DatabaseTimetableSessionBuilder {


  /**
   * A helper method to extract a TimetableSession from a Cursor.
   * @param cursor The cursor to extract the session from.
   * @return A Timetable session.
   **/
  public static TimetableSession extractSessionFromCursor(Cursor cursor, Day day) {
    String sessionName = null;
    String startTime = null;
    String endTime = null;
    String sessionMaster = null;
    String location = null;
    String type = null;
    String sessionGroup = null;

    try {
      sessionName = cursor.getString(cursor.getColumnIndexOrThrow("session_name"));
      startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
      endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
      sessionMaster = cursor.getString(cursor.getColumnIndexOrThrow("session_master"));
      location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
      type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
      sessionGroup = cursor.getString(cursor.getColumnIndexOrThrow("session_group"));
    } catch (IllegalArgumentException e) {

    } finally {
      return new TimetableSession(day,
          startTime,
          endTime,
          sessionName,
          new String[]{sessionGroup},
          sessionMaster,
          location,
          type);
    }
  }
}
