package org.stephenfox.dittimetables.timetable;


public class TimetableDay {

  private TimetableSession[] sessions;
  private Day day;



  public TimetableDay(TimetableSession[] sessions) {
    this.sessions = sessions;
  }



  public TimetableSession[] getSessions() { return sessions; }

  public Day getDay() { return day; }

  public void setDay(Day day) { this.day = day; }
}
