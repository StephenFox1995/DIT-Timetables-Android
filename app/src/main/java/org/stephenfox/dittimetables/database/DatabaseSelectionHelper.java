package org.stephenfox.dittimetables.database;


import android.database.sqlite.SQLiteDatabase;

public class DatabaseSelectionHelper {

  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  public DatabaseSelectionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }

}
