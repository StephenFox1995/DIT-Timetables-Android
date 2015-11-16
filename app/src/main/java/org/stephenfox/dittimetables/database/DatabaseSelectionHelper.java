package org.stephenfox.dittimetables.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.TimetableSession;

import java.util.ArrayList;

public class DatabaseSelectionHelper {

  String SELECT_ALL_SESSIONS = "SELECT TimetableSession.session_name, " +
      "TimetableSession.start_time, " +
      "TimetableSession.end_time, " +
      "TimetableSession.session_master, " +
      "TimetableSession.location, " +
      "TimetableSession.session_name, " +
      "TimetableSession.type, " +
      "TimetableSession.location, " +
      "SessionGroup.group_name " +
      "FROM TimetableSession " +
      "JOIN SessionGroup ON TimetableSession._id = SessionGroup.session_group_timetable_session_id " +
      "WHERE TimetableSession.timetable_session_timetable_day = ?";

  String SELECT_NUMBER_OF_DAYS = "SELECT COUNT(*) FROM TimetableDay " +
      "JOIN TimetableWeek ON TimetableDay.timetable_day_timetable_week = TimetableWeek._id " +
      "JOIN Timetable ON TimetableWeek.timetable_week_timetable_course_code = Timetable.course_code " +
      "WHERE Timetable.course_code = ?";

  String SELECT_ALL_GROUPS = "SELECT DISTINCT SessionGroup.group_name " +
      "FROM SessionGroup " +
      "JOIN TimetableSession ON SessionGroup.session_group_timetable_session_id = TimetableSession._id " +
      "WHERE SessionGroup.group_name != \"\"";


  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  public DatabaseSelectionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }

  public TimetableSession[] selectSessions(String courseCode) {
    ArrayList<TimetableSession> sessions = new ArrayList<>();

    for (int i = 0; i < numberOfDaysForTimetable(courseCode); i ++) {
      Day d = Day.intToDay(i);
      String day = d.toString();

      Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL_SESSIONS, new String[]{day});

      if (cursor.getCount() != 0) {
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          sessions.add(extractSessionFromCursor(cursor, d));
        }
      }
      cursor.close();
    }
    return sessions.toArray(new TimetableSession[sessions.size()]);
  }


  /**
   * A helper method to extract a TimetableSession from a Cursor.
   * @param cursor The cursor to extract the session from.
   * @return A Timetable session.
   **/
  private TimetableSession extractSessionFromCursor(Cursor cursor, Day day) {
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



  /**
   * A helper method to find the number of days a timetable is spread over.
   */
  private int numberOfDaysForTimetable(String courseCode) {
    Cursor cursor = sqLiteDatabase.rawQuery(SELECT_NUMBER_OF_DAYS, new String[]{courseCode});
    if (cursor.getCount() != 0) {
      cursor.moveToFirst();
      int value = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
      cursor.close();
      return value;
    }
    else {
      cursor.close();
      return 0;
    }
  }
  

  /**
   * Use this method to see if another timetable can be added to the database as the app
   * only allows for one timetable to be saved in the database at a time.
   *
   * @return True if no timetable is already saved to the user's device.
   *         False if not allowed add timetable
   */
  public boolean canAddTimetableToDatabase() {
    String selection = "SELECT COUNT(*) FROM Timetable";
    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{});
    int value = 0;
    if (cursor.getCount() != 0) {
      cursor.moveToFirst();
      value = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
      cursor.close();
    }
    return !(value > 0);
  }


  /**
   * Use this to find out if a timetable for a course already exists in the database.
   *
   * @param courseCode The courseCode of the timetable.
   * @return True if the timetable exists, false if it does not exist
   */
  public boolean timetableAlreadyExists(String courseCode) {
    String selection = "SELECT * FROM Timetable WHERE Timetable.course_code = ?";
    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{courseCode});

    if (cursor.getCount() != 0) {
      cursor.close();
      return true;
    } else {
      cursor.close();
      return false;
    }
  }

  /**
   * Returns all the possible groups from a timetable.
   * @return All the groups for a timetable.
   * */
  public String[] selectAllGroups() {
    Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL_GROUPS, new String[]{});

    String[] groups = new String[cursor.getCount()];

    if (cursor.getCount() != 0) {
      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        groups[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("group_name"));
      }
    }
    cursor.close();
    return groups;
  }

}
