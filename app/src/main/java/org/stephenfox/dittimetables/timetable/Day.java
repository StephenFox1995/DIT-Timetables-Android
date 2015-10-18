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
}
