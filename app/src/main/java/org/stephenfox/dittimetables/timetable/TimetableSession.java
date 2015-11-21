package org.stephenfox.dittimetables.timetable;


import android.util.Log;

import org.stephenfox.dittimetables.time.Time;
import org.stephenfox.dittimetables.utilities.Utilities;

/**
 * This class provides information for a single session
 * for a course's timetable. For example a session could be a
 * lab, lecture etc.
 */
public class TimetableSession {


  public TimetableSession(Day day, String startTime, String endTime, String sessionName,
                          String[] sessionGroups, String sessionMaster, String sessionLocation,
                          String sessionType) {
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
    this.sessionName = sessionName;
    this.sessionGroups = sessionGroups;
    this.sessionMaster = sessionMaster;
    this.sessionLocation = sessionLocation;
    this.sessionType = sessionType;
  }


  /**
   * Use this method to decide whether or not a session is active i.e
   * it is currently being held at this moment in time.
   */
  public boolean isActive() {
    String sEndTime = Utilities.stringWithReplacedIndex(getEndTime(), '.', 2);
    String sStartTime = Utilities.stringWithReplacedIndex(getStartTime(), '.', 2);
    String sCurrentTime = Utilities.stringWithReplacedIndex(Time.getCurrentTime(), '.', 2);

    float startTime = Float.parseFloat(sStartTime);
    float endTime = Float.parseFloat(sEndTime);
    float currentTime = Float.parseFloat(sCurrentTime);

    Day today = Day.stringToDay(Time.getCurrentDay());
    return isActiveForDay(today) && startTime <= currentTime && endTime > currentTime;
  }


  public boolean isActiveForDay(Day day) {
    return this.day == day;
  }


  /**
   * Determine the current status of a session. i.e if it is active or has finished for
   * the day etc.
   * @return The status.
   **/
  public SessionStatus timeStatus() {
    String sCurrentTime = Utilities.stringWithReplacedIndex(Time.getCurrentTime(), '.', 2);
    String sStartTime = Utilities.stringWithReplacedIndex(getStartTime(), '.', 2);

    float startTime = Float.parseFloat(sStartTime);
    float currentTime = Float.parseFloat(sCurrentTime);

    if (!isActiveForDay(Day.stringToDay(Time.getCurrentDay()))) {
      return SessionStatus.UnAssociatedDay;
    }
    else if (isActive()) {
      Log.d("SF", "SessionsStatus.Active: " + toString());
      return SessionStatus.Active;
    }
    else if (currentTime < startTime) {
      Log.d("SF", "SessionsStatus.Finished: " + toString());
      return SessionStatus.Later;
    }
    else {
      Log.d("SF", "SessionsStatus.Later: " + toString());
      return SessionStatus.Finished;
    }
  }


  /**
   * The day for which this session will be held
   */
  private Day day;



  /**
   * The start time for a session.
   * Must be in the following format xx:xx e.g. 10:00
   */
  private String startTime;


  /**
   * The end time for a session.
   * Must be in format of xx:xx e.g. 10:00
   */
  private String endTime;


  /**
   * The name of the session e.g. Mobile Software Development etc.
   */
  private String sessionName;


  /**
   * An array containing all the groups for which
   * this session is applicable to.
   */
  private String[] sessionGroups;


  /**
   * The name of the lecturer, lab assistant etc.
   */
  private String sessionMaster;


  /**
   * The location of the session e.g. room number etc.
   */
  private String sessionLocation;


  /**
   * The type of session e.g. Lecture, Lab, Tutorial etc.
   */
  private String sessionType;

  public Day getDay() { return day; }

  public String getStartTime() { return startTime; }

  public String getEndTime() { return endTime; }

  public String getSessionName() { return sessionName; }

  public String[] getSessionGroups() { return sessionGroups; }

  public String getSessionMaster() { return sessionMaster; }

  public String getSessionLocation() { return sessionLocation; }

  public String getSessionType() { return sessionType; }


  @Override
  public String toString() {
    String sessionGroups = "";
    for (String groups : getSessionGroups()) {
      sessionGroups += groups;
    }

    return "\nDay: " + getDay().toString() +
        "\nSession: " + getSessionName() +
        "\nSession master: " + getSessionMaster() +
        "\nSession start: " + getStartTime() +
        "\nSession end: " + getEndTime() +
        "\nSession groups: " + sessionGroups +
        "\nSession location: " + getSessionLocation();
  }
}
