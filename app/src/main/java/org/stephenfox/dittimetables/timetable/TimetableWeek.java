package org.stephenfox.dittimetables.timetable;


public class TimetableWeek {


  private TimetableDay[] days;


  public TimetableWeek(TimetableDay[] days) {
    this.days = days;
    this.numberOfDays = days.length;
  }


//  public TimetableDay getDay(Day day) {
//
//  }

  /**
   * The number of days the timetable is spread over.
   */
  private int numberOfDays;


  public int getNumberOfDays() { return numberOfDays; }
}
