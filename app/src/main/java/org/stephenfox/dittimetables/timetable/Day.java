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

}
