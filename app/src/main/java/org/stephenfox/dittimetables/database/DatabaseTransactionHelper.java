package org.stephenfox.dittimetables.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Timetable;


public class DatabaseTransactionHelper {

  Timetable timetable;
  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  public DatabaseTransactionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }


  /**
   * Inserts a Timetable into the database.
   */
  public void insertTimetable(Timetable timetable) {
    this.timetable = timetable;
    insertIntoTimetable();
  }


  private void insertIntoTimetable() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TimetableSchema.Timetable.KEY_TIMETABLE_ID, timetable.getCourseID());

    sqLiteDatabase.insert(TimetableSchema.Timetable.TABLE_NAME, null, contentValues);
    Log.d("DB", "inserted");
  }
}
