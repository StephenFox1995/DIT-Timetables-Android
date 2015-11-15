package org.stephenfox.dittimetables.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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
   *
   * @param timetable The timetable to insert to the database.
   * @return A status of .Failed or .Success upon success or failure of the transaction
   */
  public DatabaseTransactionStatus insertTimetable(Timetable timetable) {
    this.timetable = timetable;

    ArrayList<Boolean> results = new ArrayList<>();
    results.add(insertIntoTimetable());
    results.add(insertIntoTimetableWeek());
    results.add(insertIntoTimetableDay());
    results.add(insertSessions());
    results.add(insertSessionGroups());

    return insertTransactionStatus(results.toArray(new Boolean[results.size()]));
  }


  /**
   * Performs a check to see if insertion transaction completed successfully or failed
   *
   * @param  results A result set of Booleans
   * @return A status of .Failed or .Success upon success or failure of the transaction
   * */
  private DatabaseTransactionStatus insertTransactionStatus(Boolean[] results) {
    for (Boolean result : results) {
      if (!result) {
        return DatabaseTransactionStatus.Failed;
      }
    }
    return DatabaseTransactionStatus.Success;
  }

  /**
   * Performs a check to see if an insertion into a specific
   * table was successful or not.
   *
   * @param result A result from the insertion.
   * @return A boolean value if the transaction was successful or not.
   */
  private boolean insertionStatus(long result) {
    return result != -1;
  }


  /**
   * Performs a check to see if an array of insertions into a
   * table was successful or not.
   *
   * @param results An array of result from the insertion.
   * @return A boolean value if the transaction was successful or not.
   */
  private boolean insertionStatus(Long[] results) {
    for (long aResult : results) {
      if (aResult == -1) {
        return false;
      }
    }
    return true;
  }


  private boolean insertIntoTimetable() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TimetableSchema.Timetable.KEY_TIMETABLE_ID, timetable.getCourseCode());
    long result = sqLiteDatabase.insert(TimetableSchema.Timetable.TABLE_NAME, null, contentValues);
    return insertionStatus(result);
  }

  private boolean insertIntoTimetableWeek() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(TimetableSchema.TimetableWeek.COL_TIMETABLE_ID, timetable.getCourseCode());
    currentTimetableWeekID =
        sqLiteDatabase.insert(TimetableSchema.TimetableWeek.TABLE_NAME, null, contentValues);
    return insertionStatus(currentTimetableWeekID);
  }


  private boolean insertIntoTimetableDay() {
    int numberOfDays = timetable.getTimetableWeek().getNumberOfDays();
    String[] dayNames = timetable.getTimetableWeek().getDayNames();
    currentTimetableDayIDs = new ArrayList<>(numberOfDays);

    ArrayList<Long> results = new ArrayList<>();

    for (int i = 0; i < numberOfDays; i++) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(TimetableSchema.TimetableDay.COL_TIMETABLE_WEEK_ID, currentTimetableWeekID);
      contentValues.put(TimetableSchema.TimetableDay.COL_DAY_NAME, dayNames[i]);

      long result =
          sqLiteDatabase.insert(TimetableSchema.TimetableDay.TABLE_NAME, null, contentValues);
      currentTimetableDayIDs.add(result);
      results.add(result);
    }
    return insertionStatus(results.toArray(new Long[results.size()]));
  }


  private boolean insertSessions() {
    TimetableDay[] days = timetable.getTimetableWeek().getDays();
    currentTimetableSessionIDs = new ArrayList<>();
    ContentValues contentValues;

    ArrayList<Long> results = new ArrayList<>();

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

        long result =
            sqLiteDatabase.insert(TimetableSchema.TimetableSession.TABLE_NAME, null, contentValues);
        currentTimetableSessionIDs.add(result);
        results.add(result);
      }
      i++;
    }
    return insertionStatus(results.toArray(new Long[results.size()]));
  }


  private boolean insertSessionGroups() {
    TimetableDay[] days = timetable.getTimetableWeek().getDays();

    ContentValues contentValues;
    ArrayList<Long> results = new ArrayList<>();

    int i = 0;
    for (TimetableDay day : days) {
      for (TimetableSession session : day.getSessions()) {
        for (String group : session.getSessionGroups()) {
          contentValues = new ContentValues();
          contentValues.put(TimetableSchema.SessionGroup.COL_GROUP_NAME, group);
          contentValues.put(TimetableSchema.SessionGroup.COL_TIMETABLE_SESSION_ID,
              currentTimetableSessionIDs.get(i));

          long result =
              sqLiteDatabase.insert(TimetableSchema.SessionGroup.TABLE_NAME, null, contentValues);
          results.add(result);
        }
        i++;
      }
    }
    return insertionStatus(results.toArray(new Long[results.size()]));
  }
}
