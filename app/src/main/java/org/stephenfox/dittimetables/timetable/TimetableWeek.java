package org.stephenfox.dittimetables.timetable;


import java.util.ArrayList;

public class TimetableWeek {

  private ArrayList<TimetableDay> days;


  /**
   * Instantiates a new instance with an array of timetable days
   *
   * @param days The days for the week.
   */
  public TimetableWeek(ArrayList<TimetableDay> days) {
    this.days = days;
  }


  public TimetableWeek() {}

  /**
   * Returns the names of all the days that have sessions.
   * If a day does not have any sessions then it will not be included in the array.
   *
   * @return The names of the days that contain session
   */
  public String[] getDayNames() {
    ArrayList<String> dayNames = new ArrayList<>();

    for (int i = 0; i < getNumberOfDays(); i++) {
      if (days.get(i).containsSessions()) {
        dayNames.add(days.get(i).getDayName());
      }
    }
    return dayNames.toArray(new String[dayNames.size()]);
  }


  public void addDay(TimetableDay day) {
    if (this.days == null) {
      this.days = new ArrayList<>();
    }
    this.days.add(day);
  }


  public int getNumberOfDays() { return days.size(); }


  public TimetableDay[] getDays() {
    return days.toArray(new TimetableDay[days.size()]);
  }


  /**
   * Returns a Day in the week.
   *
   * @param day The day to return.
   * @return The day.
   */
  public TimetableDay getDay(Day day) {
    return this.days.get(day.toInt());
  }



  @Override
  public String toString() {
    String returnString = getNumberOfDays() + "\n";
    for (TimetableDay day : days) {
      returnString += day.toString();
    }
    return returnString;
  }
}
