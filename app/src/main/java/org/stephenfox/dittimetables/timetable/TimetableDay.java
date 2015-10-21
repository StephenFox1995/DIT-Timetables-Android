package org.stephenfox.dittimetables.timetable;


import java.util.ArrayList;

public class TimetableDay {

  private ArrayList<TimetableSession> sessions;
  private Day day;

  public  TimetableDay(Day day) {
    this.day = day;
  }

  public TimetableDay(ArrayList<TimetableSession> sessions) {
    this.sessions = sessions;
  }


  /**
   * Returns a boolean if an instance contains any TimetableSessions.
   *
   * @return boolean True If there is sessions associated with an instance.
   */
  public boolean containsSessions() { return this.getSessions() != null; };


  /**
   * Adds a session to a day.
   *
   * @param session The session to add.
   */
  public void addSession(TimetableSession session) {
    if (this.sessions == null) {
      this.sessions = new ArrayList<>();
    }
    this.sessions.add(session);
  }

  public ArrayList<TimetableSession> getSessions() { return sessions; }

  public void setSessions(ArrayList<TimetableSession> sessions) { this.sessions = sessions; }

  public Day getDay() { return day; }

  public void setDay(Day day) { this.day = day; }


  @Override
  public String toString() {
    String s = "Day: " + getDay().toString();
    if (this.sessions != null) {
      for (TimetableSession session: this.sessions) {
        s = s + session.toString();
      }
      return s;
    } else {
      return s + "\n No sessions.\n";
    }
  }
}
