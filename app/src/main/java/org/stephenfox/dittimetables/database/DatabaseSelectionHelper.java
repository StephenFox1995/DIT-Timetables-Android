package org.stephenfox.dittimetables.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Day;

public class DatabaseSelectionHelper {

  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  public DatabaseSelectionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }



  /**
   * Select all the session of a course from the database
   * for a given day.
   *
   * @param day The day to which the sessions are held on.
   **/
  public void selectSessionDetails(Day day) {
    String selection = "SELECT * FROM TimetableSession \n" +
        "JOIN TimetableDay ON TimetableSession.timetable_session_timetable_day = TimetableDay._id\n" +
        "WHERE TimetableDay.day_name = ?;";
    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[]{day.toString()});
  }


  public boolean timetableAlreadyExists(String courseCode) {
    timetableDatabase.open();
    if (timetableDatabase == null) {
      Log.d("SF", "timetable database is null.");
    }
    String selection = "SELECT * FROM Timetable \n" +
        "WHERE Timetable._id = ?;";
    Cursor cursor = sqLiteDatabase.rawQuery(selection, new String[] {courseCode});

    if (cursor == null && cursor.getCount() == 0)
      Log.d("SF", "Cursor was null");

     return true;

  }

}
