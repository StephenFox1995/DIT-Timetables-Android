package org.stephenfox.dittimetables.timetable;


import android.os.Parcel;


/**
 * This class provides information for a single session
 * for a courses timetable. For example a session could be a
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

  private TimetableSession(Parcel source) {
    this.startTime = source.readString();
    this.endTime = source.readString();
    this.sessionName = source.readString();
    this.sessionGroups = source.createStringArray();
    this.sessionMaster = source.readString();
    this.sessionLocation = source.readString();
    this.sessionType = source.readString();
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


}
