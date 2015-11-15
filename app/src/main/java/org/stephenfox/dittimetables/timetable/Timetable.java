package org.stephenfox.dittimetables.timetable;


/**
 * A class that manages a Timetable.
 */
public class Timetable {

  private TimetableWeek timetableWeek;
  private String courseCode;


  public Timetable(TimetableWeek timetableWeek, String courseCode) {
    this.timetableWeek = timetableWeek;
    this.courseCode = courseCode;
  }



  public void setTimetableWeek(TimetableWeek timetableWeek) {
    this.timetableWeek = timetableWeek;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }

  public TimetableWeek getTimetableWeek() {
    return timetableWeek;
  }

  public TimetableDay getTimetableDay(Day day) { return timetableWeek.getDay(day); }

  public int getDayCount() { return timetableWeek.getNumberOfDays(); }

  public String getCourseCode() {
    return courseCode;
  }

  @Override
  public String toString() {
    return this.timetableWeek.toString();
  }
}
