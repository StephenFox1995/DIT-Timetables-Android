package org.stephenfox.dittimetables.timetable;


import java.util.ArrayList;

/**
 * Use this class to generate a data set for a timetable for a given week.
 */
public class TimetableGenerator {

  private static final int DAYS_OF_WEEK = 7;

  private ArrayList<TimetableDay> timetableDays;
  private TimetableWeek timetableWeek;
  private ArrayList<TimetableSession> sessions;


  /**
   * Instantiates a new instance with an array of sessions
   */
  public TimetableGenerator(ArrayList<TimetableSession> sessions) {
    this.sessions = sessions;
  }


  /**
   * Generates a new Timetable Object */
  public Timetable generateTimetable() {
    this.addSessionsToDays(sessions);
    this.addDaysToWeek(timetableDays);
    return new Timetable(timetableWeek);
  }


  /**
   * Adds all the sessions an instance was initialised with an
   * adds them to the appropriate days.
   */
  private void addSessionsToDays(ArrayList<TimetableSession> sessions) {
    this.setupDays();

    for (TimetableSession session : sessions) {

      switch (session.getDay()) {
        case Monday:
          timetableDays.get(0).addSession(session);
          break;
        case Tuesday:
          timetableDays.get(1).addSession(session);
          break;
        case Wednesday:
          timetableDays.get(2).addSession(session);
          break;
        case Thursday:
          timetableDays.get(3).addSession(session);
          break;
        case Friday:
          timetableDays.get(4).addSession(session);
          break;
        case Saturday:
          timetableDays.get(5).addSession(session);
          break;
        case Sunday:
          timetableDays.get(6).addSession(session);
          break;
        default:
          break;
      }
    }
  }


  /**
   * Constructs a TimetableWeek */
  private void addDaysToWeek(ArrayList<TimetableDay> days) {
    timetableWeek = new TimetableWeek();

    for (TimetableDay day : days) {
      timetableWeek.addDay(day);
    }
  }


  // Sets up an array of 7 days.
  private void setupDays() {
    timetableDays = new ArrayList<>(TimetableGenerator.DAYS_OF_WEEK);

    for (int i = 0; i < TimetableGenerator.DAYS_OF_WEEK; i++) {
      timetableDays.add(new TimetableDay(Day.intToDay(i)));
    }
  }



  public ArrayList<TimetableDay> getTimetableDays() {
    return this.timetableDays;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
