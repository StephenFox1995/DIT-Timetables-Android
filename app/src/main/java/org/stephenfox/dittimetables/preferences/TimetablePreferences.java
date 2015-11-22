package org.stephenfox.dittimetables.preferences;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * A helper class for preferences used by this app
 **/
public class TimetablePreferences {

  Context context;
  private static final String FILE = "sPreferences";


  /**
   * Use this preference should be set if the user has timetable to their
   * device.
   * @param status true of false depending on whether they have saved a timetable.
   **/
  public static void setTimetableSavedPreference(Context context, boolean status) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean("timetableSaved", status);
    editor.apply();
  }


  /**
   * Use this method to check whether a timetable has been saved
   * to the user's device.
   * @return True: Theres a timetable saved to the user's device.
   **/
  public static boolean getTimetableSavedPreference(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean("timetableSaved", false);
  }


  /**
   * Sets the course group (e.g. DT228/3-B) the user belongs to.
   * @param courseGroup The course group the user belongs to.
   **/
  public static void setCourseGroupPreference(Context context, String courseGroup) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("chosenGroup", courseGroup);
    editor.apply();
  }


  /**
   * Sets the code of the user's course.
   * @param code The course code.
   **/
  public static void setCourseCodePreference(Context context, String code) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("courseCode", code);
    editor.apply();
  }


  /**
   * Returns the course group the user's belongs to if
   * it was set.
   * @return The course group if set, otherwise null.
   * */
  public static String getCourseGroupPreference(Context context) {
    SharedPreferences preferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return preferences.getString("chosenGroup", null);
  }

  /**
   * Returns the course code of the user.
   * @return The course code of the user's course if set, otherwise null.
   */
  public static String getCourseCodePreference(Context context) {
    SharedPreferences preferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return preferences.getString("courseCode", null);
  }



  public static void removeAllPreferences(Context context) {
    SharedPreferences preferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    preferences.edit().remove("courseCode").commit();
    preferences.edit().remove("chosenGroup").commit();
    preferences.edit().remove("timetableSaved").commit();
  }
}
