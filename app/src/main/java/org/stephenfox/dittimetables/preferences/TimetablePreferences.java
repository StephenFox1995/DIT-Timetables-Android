package org.stephenfox.dittimetables.preferences;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * A helper class for preferences used by this app
 **/
public class TimetablePreferences {

  Context context;
  private static final String FILE = "sPreferences";

  public TimetablePreferences(Context context) {
    this.context = context;
  }


  public void setCourseGroupPreference(String courseGroup) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("chosenGroup", courseGroup);
    editor.apply();
  }

  public void setCourseCodePreference(String code) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("courseCode", code);
    editor.apply();
  }

  public String getCourseGroupPreference() {
    SharedPreferences preferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return preferences.getString("chosenGroup", null);
  }

  public String getCourseCodePreference() {
    SharedPreferences preferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return preferences.getString("courseCode", null);
  }
}
