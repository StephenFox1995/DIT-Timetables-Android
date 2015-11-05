package org.stephenfox.dittimetables.database;


public class TimetableSchema {

  private TimetableSchema() {}


  public static final class Timetable {
    public static final String TABLE_NAME = "Timetable";
    public static final String KEY_TIMETABLE_ID = "_id";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
        "(" + KEY_TIMETABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT );";

  }


  public static final class TimetableWeek {
    public static final String TABLE_NAME = "TimetableWeek";
    public static final String KEY_TIMETABLE_WEEK_ID = "_id";
    public static final String COL_TIMETABLE_ID = "timetable_week_timetable_id";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
        + "(" + KEY_TIMETABLE_WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_TIMETABLE_ID + " INTEGER, "
        + "FOREIGN KEY (" + COL_TIMETABLE_ID + ") REFERENCES "
        + Timetable.TABLE_NAME + "(" + Timetable.KEY_TIMETABLE_ID + ")"
        + ");";
  }


  public static final class TimetableDay {
    public static final String TABLE_NAME = "TimetableDay";
    public static final String KEY_TIMETABLE_DAY_ID = "_id";
    public static final String COL_TIMETABLE_WEEK_ID = "timetable_day_timetable_week";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
        + "(" + KEY_TIMETABLE_DAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_TIMETABLE_WEEK_ID + " INTEGER, "
        + "FOREIGN KEY (" + COL_TIMETABLE_WEEK_ID + ") REFERENCES "
        + TimetableWeek.TABLE_NAME + "(" + TimetableWeek.KEY_TIMETABLE_WEEK_ID + ")"
        + ");";
  }
  



}
