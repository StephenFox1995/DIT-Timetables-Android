package org.stephenfox.dittimetables;


import android.util.Log;

import junit.framework.Assert;

import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.InvalidTimetableDataException;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableBuilder;
import org.stephenfox.dittimetables.timetable.TimetableSession;

public class TimetableBuilderTest extends ApplicationTest{

  public void testBuilderInstantiation() {
    MockSessions mockSessions = new MockSessions();

    try {
      TimetableBuilder builder = new TimetableBuilder(mockSessions.getSessions());
      Timetable timetable = builder.buildTimetable("DT228/3");
      Assert.assertNotNull(timetable);

      Log.println(0, "SF", timetable.toString());
    } catch (InvalidTimetableDataException e) {
      Log.e("SF", "Unable to build session");
    }
  }



  private class MockSessions {
    public TimetableSession[] getSessions() {
      return new TimetableSession[]{
          new TimetableSession
              (Day.Monday,
                  "11:00",
                  "12:00",
                  "Mobile Development",
                  new String[]{"DT228/3-B", "DT228/3-D"},
                  "Susan McKeever",
                  "KE-400-8",
                  "Lecture"),
          new TimetableSession(
              Day.Monday,
              "13:00",
              "15:00",
              "Database",
              new String[]{"DT228/3-B", "DT228/3-D"},
              "Patricia Byrne",
              "KE-400-8",
              "Lecture")};

    }

  }
}
