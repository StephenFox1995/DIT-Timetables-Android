package org.stephenfox.dittimetables.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.TimetableSession;

import java.util.ArrayList;

public class DatabaseSelectionHelper {

  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  public DatabaseSelectionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }


  public TimetableSession[] selectSessions(String courseCode) {
    ArrayList<TimetableSession> sessions = new ArrayList<>();
    String selection = "SELECT TimetableSession.session_name, " +
        "TimetableSession.start_time, " +
        "TimetableSession.end_time, " +
        "TimetableSession.session_master, " +
        "TimetableSession.location, " +
        "TimetableSession.session_name," +
        "TimetableSession.type, " +
        "TimetableSession.location, " +
        "SessionGroup.group_name " +
        "FROM TimetableSession " +
        "JOIN SessionGroup ON TimetableSession._id = SessionGroup.session_group_timetable_session_id " +
        "WHERE TimetableSession.timetable_session_timetable_day = ?";

    for (int i = 0; i < numberOfDaysForTimetable(courseCode); i ++) {
      Day d = Day.intToDay(i);
      String day = d.toString();

      Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{day});

      if (cursor.getCount() != 0) {
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          String sessionName = cursor.getString(cursor.getColumnIndex("session_name"));
          String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
          String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
          String sessionMaster = cursor.getString(cursor.getColumnIndex("session_master"));
          String location = cursor.getString(cursor.getColumnIndex("location"));
          String type = cursor.getString(cursor.getColumnIndex("type"));

          TimetableSession session = new TimetableSession(d,
              startTime,
              endTime,
              sessionName,
              new String[]{"a,b "},
              sessionMaster,
              location,
              type);
          sessions.add(session);
        }
      }
      cursor.close();
    }
    return sessions.toArray(new TimetableSession[sessions.size()]);
  }


  /**
   * A helper method to find the number of days a timetable is spread over.
   */
  private int numberOfDaysForTimetable(String courseCode) {
    String selection = "SELECT COUNT(*) FROM TimetableDay " +
        "JOIN TimetableWeek ON TimetableDay.timetable_day_timetable_week = TimetableWeek._id " +
        "JOIN Timetable ON TimetableWeek.timetable_week_timetable_course_code = Timetable.course_code " +
        "WHERE Timetable.course_code = ?";

    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{courseCode});
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
   * only allows for one timetbale to be saved in the database at a time.
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
    if (sqLiteDatabase == null) {
      Log.d("SF", "SQLitedb is null");
    } else {
      Log.d("SF", "SQLiteDB is not null");
    }
    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{courseCode});

    if (cursor.getCount() != 0) {
      cursor.close();
      return true;
    } else {
      cursor.close();
      return false;
    }
  }

}
