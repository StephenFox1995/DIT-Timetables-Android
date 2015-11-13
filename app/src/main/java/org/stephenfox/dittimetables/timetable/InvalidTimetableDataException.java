package org.stephenfox.dittimetables.timetable;


public class InvalidTimetableDataException extends Exception {
  public InvalidTimetableDataException(String detailMessage) {
    super(detailMessage);
  }
}
