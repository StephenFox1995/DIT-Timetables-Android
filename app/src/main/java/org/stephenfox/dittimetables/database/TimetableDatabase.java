package org.stephenfox.dittimetables.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableSession;


public class TimetableDatabase {

  private DatabaseTimetableHelper dbHelper;
  private Context context;
  private SQLiteDatabase sqLiteDatabase;


  public TimetableDatabase(Context context) {
    this.context = context;
  }


  /**
   * Opens the database for read/write.
   * @note Do not call on the main UI Thread as this may take some time to open.
   */
  public void open() {
    try {
      this.dbHelper = new DatabaseTimetableHelper(context);
      sqLiteDatabase = dbHelper.getReadableDatabase();
    } catch (SQLiteException e) {
      Log.e("SQLLiteException", "An error occurred with the database.");
    }
  }


  public void close() {
    dbHelper.close();
  }


  /**
   * Attempts to insert a #{@link Timetable} object into the database.
   *
   * @param timetable  The timetable to insert into the database.
   * @return A status of .Failed or .Success upon success or failure of the transaction.
   **/
  public DatabaseTransactionStatus addTimetable(Timetable timetable) {
    DatabaseTransactionHelper transactionHelper = new DatabaseTransactionHelper(this);
    return transactionHelper.insertTimetable(timetable);
  }

  
  public boolean timetableExists(String courseCode) {
    open();
    DatabaseSelectionHelper selectionHelper = new DatabaseSelectionHelper(this);
    boolean exists = selectionHelper.timetableAlreadyExists(courseCode);
    close();
    return exists;
  }


  public boolean canAddTimetableToDatabase() {
    open();
    DatabaseSelectionHelper selectionHelper = new DatabaseSelectionHelper(this);
    boolean allowed = selectionHelper.canAddTimetableToDatabase();
    close();
    return allowed;
  }


  public TimetableSession[] getSessions(String courseCode) {
    open();
    DatabaseSelectionHelper selectionHelper = new DatabaseSelectionHelper(this);
    TimetableSession[] sessions = selectionHelper.selectSessions(courseCode);
    close();
    return sessions;
  }

  public TimetableSession[] getSessionForGroup(String courseCode, String group) {
    open();
    DatabaseSelectionHelper selectionHelper = new DatabaseSelectionHelper(this);
    TimetableSession[] sessions = selectionHelper.selectSessionForGroup(courseCode, group);
    close();
    return sessions;
  }


  public String[] getGroups() {
    open();
    DatabaseSelectionHelper selectionHelper = new DatabaseSelectionHelper(this);
    String[] groups = selectionHelper.selectAllGroups();
    close();
    return groups;
  }


  public DatabaseTransactionStatus deleteTimetable() {
    open();
    DatabaseTransactionHelper transactionHelper = new DatabaseTransactionHelper(this);
    DatabaseTransactionStatus status =  transactionHelper.deleteTimetable();
    close();
    return status;
  }


  public Context getContext() {
    return context;
  }

  public SQLiteDatabase getSqLiteDatabase() {
    return this.sqLiteDatabase;
  }

  private static class DatabaseTimetableHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DatabaseTimetableHelper(Context context) {
      super(context, TimetableSchema.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(TimetableSchema.Timetable.CREATE_TABLE);
      db.execSQL(TimetableSchema.TimetableWeek.CREATE_TABLE);
      db.execSQL(TimetableSchema.TimetableDay.CREATE_TABLE);
      db.execSQL(TimetableSchema.TimetableSession.CREATE_TABLE);
      db.execSQL(TimetableSchema.SessionGroup.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(TimetableSchema.Timetable.DELETE_TABLE);
      db.execSQL(TimetableSchema.TimetableWeek.DELETE_TABLE);
      db.execSQL(TimetableSchema.TimetableDay.DELETE_TABLE);
      db.execSQL(TimetableSchema.TimetableSession.DELETE_TABLE);
      db.execSQL(TimetableSchema.SessionGroup.DELETE_TABLE);
      onCreate(db);
    }
  }
}
