package org.stephenfox.dittimetables.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.AsyncDownloader;
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.network.WeekDownloader;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.EmptySessionsArrayException;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableGenerator;
import org.stephenfox.dittimetables.timetable.TimetableSession;

import java.util.ArrayList;

/**
 * This class manages a set of fragments see
 * {@link org.stephenfox.dittimetables.gui.TimetableWeekPageFragment}
 * for details on each fragment.
 */
public class TimetableWeekPagerActivity extends FragmentActivity {

  private ViewPager pager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screen_slide);

    Intent d = getIntent();
    String url = d.getStringExtra("url");

    WeekDownloader weekDownloader = new WeekDownloader();
    weekDownloader.downloadWeekForCourse(url, new AsyncDownloader.HttpAsyncCallback() {
      @Override
      public void finished(String data) {

        try {
          ArrayList<TimetableSession> sessions = parseJson(data);
          Timetable timetable = createTimetable(sessions);

          pager = (ViewPager) findViewById(R.id.slide);
          pager.setAdapter(new SliderAdapter(getSupportFragmentManager(), timetable));

        } catch (EmptySessionsArrayException e) {
          // TODO: Make Toast to tell user course not available.
          // Dismiss this activity.
          Log.v("12345:", "EmptySessionArrayException.");
        }
      }
    });
  }


  /**
   * A helper method to parse the Json data.
   *
   * @param data The json data, in String object.
   *
   * @return ArrayList of TimetableSessions
   */
  private ArrayList<TimetableSession> parseJson(String data) {
    JsonParser jsonParser = new JsonParser();
    return jsonParser.parseSessionsForWeek(data);
  }


  /**
   * Helper method to create a Timetable.
   *
   * @param sessions An array of TimetableSession's for which
   *                 to create the Timetable.
   *
   * @return A new Timetable instance
   * @throws EmptySessionsArrayException
   */
  private Timetable createTimetable(ArrayList<TimetableSession> sessions)
      throws EmptySessionsArrayException {
    TimetableGenerator generator = new TimetableGenerator(sessions);
    Timetable timetable = generator.generateTimetable();

    return timetable;
  }


  private class SliderAdapter extends FragmentStatePagerAdapter {

    private Timetable timetable;

    public SliderAdapter(FragmentManager manager, Timetable timetable) {
      super(manager);
      this.timetable = timetable;
    }


    @Override
    public Fragment getItem(int position) {
      Log.d("12345:", "getItem called");
      Day dayForFragment = Day.intToDay(position);
      return new TimetableWeekPageFragment();//.newInstance(this.timetable.getTimetableDay(dayForFragment));
    }

    @Override
    public int getCount() {
      Log.d("12345:", "" + timetable.getDayCount());
      return timetable.getDayCount();
    }
  }
}
