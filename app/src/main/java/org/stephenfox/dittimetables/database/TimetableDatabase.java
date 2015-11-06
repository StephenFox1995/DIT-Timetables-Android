package org.stephenfox.dittimetables.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.stephenfox.dittimetables.timetable.Timetable;


public class TimetableDatabase {

  public static final String DATABASE_NAME = "timetable.db";


  private DatabaseTimetableHelper dbHelper;
  private Context context;
  private SQLiteDatabase sqLiteDatabase;



  public TimetableDatabase(Context context) {
    this.context = context;
  }


  // TODO(stephen fox): Don't call getReadableDatabase on MAIN UI THREAD
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

  public void addTimetable(Timetable timetable) {

  }



  private static class DatabaseTimetableHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DatabaseTimetableHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.d("Database", "Database created!");
      db.execSQL(TimetableSchema.Timetable.CREATE_TABLE
          + TimetableSchema.TimetableWeek.CREATE_TABLE
          + TimetableSchema.TimetableDay.CREATE_TABLE
          + TimetableSchema.TimetableSession.CREATE_TABLE
          + TimetableSchema.SessionGroup.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }
}
