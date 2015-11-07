package org.stephenfox.dittimetables.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableDay;

import java.util.ArrayList;


public class DatabaseTransactionHelper {

  Timetable timetable;
  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  private long currentTimetableWeekID;
  ArrayList<Long> currentTimetableDayIDs;


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
    insertIntoTimetableWeek();
    insertIntoTimetableDay();
  }


  private void insertIntoTimetable() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TimetableSchema.Timetable.KEY_TIMETABLE_ID, timetable.getCourseID());
    sqLiteDatabase.insert(TimetableSchema.Timetable.TABLE_NAME, null, contentValues);
  }

  private void insertIntoTimetableWeek() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TimetableSchema.TimetableWeek.COL_TIMETABLE_ID, timetable.getCourseID());
    currentTimetableWeekID =
        sqLiteDatabase.insert(TimetableSchema.TimetableWeek.TABLE_NAME, null, contentValues);
  }


  private void insertIntoTimetableDay() {
    int numberOfDays = timetable.getTimetableWeek().getNumberOfDays();
    String[] dayNames = timetable.getTimetableWeek().getDayNames();
    currentTimetableDayIDs = new ArrayList<>(numberOfDays);


    for (int i = 0; i < numberOfDays; i++) {
      TimetableDay day = timetable.getTimetableDay(Day.intToDay(i));

      ContentValues contentValues = new ContentValues();
      contentValues.put(TimetableSchema.TimetableDay.COL_TIMETABLE_WEEK_ID, currentTimetableWeekID);
      contentValues.put(TimetableSchema.TimetableDay.COL_DAY_NAME, dayNames[i]);
      currentTimetableDayIDs.add(
          sqLiteDatabase.insert(TimetableSchema.TimetableDay.TABLE_NAME, null, contentValues));
    }
  }
}
