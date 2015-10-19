package org.stephenfox.dittimetables.timetable;

public enum Day {
  Monday,
  Tuesday,
  Wednesday,
  Thursday,
  Friday,
  Saturday,
  Sunday;


  public static Day intToDay(int i) {
    switch (i) {
      case 0: return Monday;
      case 1: return Tuesday;
      case 2: return Wednesday;
      case 3: return Thursday;
      case 4: return Friday;
      case 5: return Saturday;
      case 6: return Sunday;
      default: return null;
    }
  }


  @Override
  public String toString() {
    switch (this) {
      case Monday: return "Monday";
      case Tuesday: return "Tuesday";
      case Wednesday: return "Wednesday";
      case Thursday: return "Thursday";
      case Friday: return "Friday";
      case Saturday: return "Saturday";
      case Sunday: return "Sunday";
      default: return null;
    }
  }


  /**
   * An integer representation for a Day.
   *
   * @return int The int value for a day.
   */
  public int toInt() {
    switch (this) {
      case Monday: return 0;
      case Tuesday: return 1;
      case Wednesday: return 2;
      case Thursday: return 3;
      case Friday: return 4;
      case Saturday: return 5;
      case Sunday: return 6;
      default: return -1;
    }
  }
}
