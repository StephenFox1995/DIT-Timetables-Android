package org.stephenfox.dittimetables.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableDay;
import org.stephenfox.dittimetables.timetable.TimetableSession;

import java.util.ArrayList;


public class DatabaseTransactionHelper {

  Timetable timetable;
  TimetableDatabase timetableDatabase;
  SQLiteDatabase sqLiteDatabase;

  private long currentTimetableWeekID;
  ArrayList<Long> currentTimetableDayIDs;
  ArrayList<Long> currentTimetableSessionIDs;


  public DatabaseTransactionHelper(TimetableDatabase timetableDatabase) {
    this.timetableDatabase = timetableDatabase;
    this.sqLiteDatabase = timetableDatabase.getSqLiteDatabase();
  }


  /**
   * Inserts a Timetable into the database.
   */
  public DatabaseTransactionStatus insertTimetable(Timetable timetable) {
    this.timetable = timetable;
    insertIntoTimetable();
    insertIntoTimetableWeek();
    insertIntoTimetableDay();
    insertSessions();
    insertSessionGroups();
    Log.d("Database", "Database completed");
    return DatabaseTransactionStatus.Success;
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


  private void insertSessions() {
    TimetableDay[] days = timetable.getTimetableWeek().getDays();
    currentTimetableSessionIDs = new ArrayList<>();
    ContentValues contentValues;

    int i = 0;

    for (TimetableDay day : days) {
      for (TimetableSession session : day.getSessions()) {
        contentValues = new ContentValues();

        contentValues.put(TimetableSchema.TimetableSession.COL_TIMETABLE_DAY_ID,
            currentTimetableDayIDs.get(i));
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_NAME,
            session.getSessionName());
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_START_TIME,
            session.getStartTime());
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_END_TIME,
            session.getEndTime());
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_MASTER,
            session.getSessionMaster());
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_LOCATION,
            session.getSessionLocation());
        contentValues.put(TimetableSchema.TimetableSession.COL_SESSION_TYPE,
            session.getSessionType());
        currentTimetableSessionIDs.add(
            sqLiteDatabase.insert(TimetableSchema.TimetableSession.TABLE_NAME, null, contentValues));
      }
      i++;
    }
  }


  private void insertSessionGroups() {
    TimetableDay[] days = timetable.getTimetableWeek().getDays();
    ArrayList<TimetableSession> sessions = new ArrayList<>();

    ContentValues contentValues;
    int i = 0;

    for (TimetableDay day : days) {
      for (TimetableSession session : day.getSessions()) {
        for (String group : session.getSessionGroups()) {
          contentValues = new ContentValues();
          contentValues.put(TimetableSchema.SessionGroup.COL_GROUP_NAME, group);
          contentValues.put(TimetableSchema.SessionGroup.COL_TIMETABLE_SESSION_ID,
              currentTimetableSessionIDs.get(i));
          sqLiteDatabase.insert(TimetableSchema.SessionGroup.TABLE_NAME, null, contentValues);
        }
        i++;
      }
    }
  }
}
