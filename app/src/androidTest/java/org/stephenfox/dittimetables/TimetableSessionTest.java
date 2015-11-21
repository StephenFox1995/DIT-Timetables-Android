package org.stephenfox.dittimetables;


import junit.framework.Assert;

import org.stephenfox.dittimetables.time.Time;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.SessionStatus;
import org.stephenfox.dittimetables.timetable.TimetableSession;

public class TimetableSessionTest extends ApplicationTest {

  // Use this, as to not hardcode the day.
  Day currentDay = Day.stringToDay(Time.getCurrentDay());

  // Given
  TimetableSession testSession = new TimetableSession(currentDay,
      "11:00",
      "12:00",
      "Mobile Software Development",
      new String[]{"DT228-3/A", "DT228-3/B", "DT228-3/C", "DT228-3/D"},
      "Susan McKeever",
      "KE-1-008",
      "Lecture");


  public void testSessionIsForDay() {
    Assert.assertEquals(true, testSession.isActiveForDay(currentDay));
  }


  public void testSessionIsActive() {
    Assert.assertEquals(true, testSession.isActive(11.30f));
  }

  public void testSessionTimeStatusIsActive() {
    Assert.assertEquals(SessionStatus.Active, testSession.timeStatus(11.30f));
  }

  public void testSessionTimeStatusIsFinished() {
    Assert.assertEquals(SessionStatus.Finished, testSession.timeStatus(12.30f));
  }

  public void testSessionTimeStatusIsLater() {
    Assert.assertEquals(SessionStatus.Later, testSession.timeStatus(9.00f));
  }

  public void testSessionIsActiveForDay() {
    Assert.assertEquals(true, testSession.isActiveForDay(currentDay));
  }



}


