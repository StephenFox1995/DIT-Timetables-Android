package org.stephenfox.dittimetables.time;


import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Time {

  /**
   * The current day of the week.
   *
   * @return String A string representing the day e.g. Saturday, Sunday etc.
   */
  public static String getCurrentDay() {
    return "Monday";
    //SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
  }


  /**
   * The current time.
   *
   * @return String A string representing the current time in the format HH:mm:ss. e.g 18:39
   */
  public static String getCurrentTime() {
    return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
  }

}
