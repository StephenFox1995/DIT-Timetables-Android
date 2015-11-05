package org.stephenfox.dittimetables.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stephenfox on 03/11/15.
 */
public class TimetableDatabase {

  public static final String DATABASE_NAME = "timetable.db";
  public static final float DATABASE_VERSION = 0.0f;


  private DatabaseTimetableHelper dbHelper;
  private Context context;

  public TimetableDatabase(Context context) {
    this.context = context;
    this.dbHelper = new DatabaseTimetableHelper(context);
  }



  private static class DatabaseTimetableHelper extends SQLiteOpenHelper {


    public DatabaseTimetableHelper(Context context) {
      super(context, "", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }

}
