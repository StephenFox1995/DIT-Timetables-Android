package org.stephenfox.dittimetables.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Timetable;


public class TimetableDatabase {


  private DatabaseTimetableHelper dbHelper;
  private Context context;
  private SQLiteDatabase sqLiteDatabase;



  public TimetableDatabase(Context context) {
    this.context = context;
  }


  /**
   * Opens the database for read/write.
   * @note Do no call on the main UI Thread as this may take some time to open.
   */
  public void open() {
    try {
      this.dbHelper = new DatabaseTimetableHelper(context);
      sqLiteDatabase = dbHelper.getReadableDatabase();
    } catch (SQLiteException e) {
      Log.e("SQLLiteException", "Error opening database.");
    }
  }


  public void close() {
    dbHelper.close();
  }

  public DatabaseTransactionStatus addTimetable(Timetable timetable) {
    DatabaseTransactionHelper transactionHelper = new DatabaseTransactionHelper(this);
    transactionHelper.insertTimetable(timetable);
    return DatabaseTransactionStatus.Success;
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
    }
  }
}
