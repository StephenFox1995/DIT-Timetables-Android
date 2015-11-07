package org.stephenfox.dittimetables.timetable;


import java.util.ArrayList;

/**
 * Use this class to generate a data set for a timetable for a given week.
 */
public class TimetableGenerator {


  private ArrayList<TimetableSession> sessions;


  /**
   * Instantiates a new instance with an array of sessions
   *
   * @param sessions The session which will be used to generate a timetable.
   * @throws EmptySessionsArrayException
   */
  public TimetableGenerator(ArrayList<TimetableSession> sessions)
      throws EmptySessionsArrayException {
    if (sessions.size() == 0) {
      throw new
          EmptySessionsArrayException("Cannot generate Timetable, reason: sessions array empty");
    }
    this.sessions = sessions;
  }


  /**
   * Generates a new Timetable object from the TimetableWeek instance
   * that was constructed with this class.
   *
   * @return Timetable A Timetable for a given week.
   */
  public Timetable generateTimetable() {
    TimetableDay[] timetableDays = createDayArray();
    addSessionsToDays(timetableDays);

    TimetableDay[] days = checkAndRemoveEmptyDays(timetableDays);
    TimetableWeek timetableWeek = createWeek(days);

    return new Timetable(timetableWeek);
  }


  /**
   * Adds all the sessions an instance was initialised with an
   * adds them to the appropriate days.
   */
  private void addSessionsToDays(TimetableDay[] timetableDays) {
    for (TimetableSession session : sessions) {
      switch (session.getDay()) {
        case Monday:
          timetableDays[0].addSession(session);
          break;
        case Tuesday:
          timetableDays[1].addSession(session);
          break;
        case Wednesday:
          timetableDays[2].addSession(session);
          break;
        case Thursday:
          timetableDays[3].addSession(session);
          break;
        case Friday:
          timetableDays[4].addSession(session);
          break;
        case Saturday:
          timetableDays[5].addSession(session);
          break;
        case Sunday:
          timetableDays[6].addSession(session);
          break;
        default:
          break;
      }
    }
  }


  /**
   * Creates an new Timetable week object.
   *
   * @param days The days for the week.
   * @return TimetableWeek The TimetableWeek object created.
   */
  private TimetableWeek createWeek(TimetableDay[] days) {
    TimetableWeek timetableWeek = new TimetableWeek();

    for (TimetableDay day : days) {
      timetableWeek.addDay(day);
    }
    return timetableWeek;
  }


  /**
   * Creates an array of TimetableDay.
   *
   * @return A new TimetableDay array of the appropriate size.
   */
  private TimetableDay[] createDayArray() {
    TimetableDay[] timetableDays = new TimetableDay[7];

    for (int i = 0; i < timetableDays.length; i++) {
      timetableDays[i] = new TimetableDay(Day.intToDay(i));
    }
    return timetableDays;
  }


  /**
   * Checks to see if there are any empty days with in the week and
   * removes those days that have no sessions.
   *
   * @param days The days to check.
   *
   * @return TimetableDay[] A new array of days, only including the
   *                        days that have sessions.
   */
  private TimetableDay[] checkAndRemoveEmptyDays(TimetableDay[] days) {
    ArrayList<TimetableDay> _days = new ArrayList<>();

    for (TimetableDay day : days) {
      if (day.containsSessions()) {
        _days.add(day);
      }
    }
    return _days.toArray(new TimetableDay[_days.size()]);
  }
}
