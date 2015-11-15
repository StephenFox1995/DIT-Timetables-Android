package org.stephenfox.dittimetables.database;


public class TimetableSchema {

  public static final String DATABASE_NAME = "timetable.db";
  private TimetableSchema() {}


  public static final class Timetable {
    public static final String TABLE_NAME = "Timetable";
    public static final String KEY_TIMETABLE_ID = "_id";
    public static final String KEY_TIMETABLE_COURSE_CODE = "course_code";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
        "(" + KEY_TIMETABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + KEY_TIMETABLE_COURSE_CODE + " TEXT NOT NULL );";
  }


  public static final class TimetableWeek {
    public static final String TABLE_NAME = "TimetableWeek";
    public static final String KEY_TIMETABLE_WEEK_ID = "_id";
    public static final String COL_TIMETABLE_ID = "timetable_week_timetable_id";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME
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
    public static final String COL_DAY_NAME = "day_name";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME
        + "(" + KEY_TIMETABLE_DAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_TIMETABLE_WEEK_ID + " INTEGER, "
        + COL_DAY_NAME + " TEXT,"
        + "FOREIGN KEY (" + COL_TIMETABLE_WEEK_ID + ") REFERENCES "
        + TimetableWeek.TABLE_NAME + "(" + TimetableWeek.KEY_TIMETABLE_WEEK_ID + ")"
        + ");";
  }


  public static final class TimetableSession {
    public static final String TABLE_NAME = "TimetableSession";
    public static final String KEY_TIMETABLE_SESSION_ID = "_id";
    public static final String COL_TIMETABLE_DAY_ID = "timetable_session_timetable_day";
    public static final String COL_SESSION_MASTER = "session_master";
    public static final String COL_SESSION_NAME = "session_name";
    public static final String COL_SESSION_START_TIME = "start_time";
    public static final String COL_SESSION_END_TIME = "end_time";
    public static final String COL_SESSION_LOCATION = "location";
    public static final String COL_SESSION_TYPE = "type";


    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME
        + "(" + KEY_TIMETABLE_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_SESSION_NAME + " TEXT, "
        + COL_SESSION_START_TIME + " TEXT, "
        + COL_SESSION_END_TIME + " TEXT, "
        + COL_SESSION_MASTER + " TEXT, "
        + COL_SESSION_LOCATION + " TEXT, "
        + COL_SESSION_TYPE + " TEXT, "
        + COL_TIMETABLE_DAY_ID + " INTEGER, "
        + "FOREIGN KEY (" + COL_TIMETABLE_DAY_ID + ") REFERENCES "
        + TimetableDay.TABLE_NAME + "(" + TimetableDay.KEY_TIMETABLE_DAY_ID + ")"
        + ");";
  }


  public static final class SessionGroup {
    public static final String TABLE_NAME = "SessionGroup";
    public static final String KEY_SESSION_GROUP_ID = "_id";
    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_TIMETABLE_SESSION_ID = "session_group_timetable_session_id";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME
        + "(" + KEY_SESSION_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_GROUP_NAME + " TEXT, "
        + COL_TIMETABLE_SESSION_ID + " INTEGER, "
        + "FOREIGN KEY (" + COL_TIMETABLE_SESSION_ID + ") REFERENCES "
        + TimetableSession.TABLE_NAME + "(" + TimetableSession.KEY_TIMETABLE_SESSION_ID + ")"
        + ")";
  }
}
