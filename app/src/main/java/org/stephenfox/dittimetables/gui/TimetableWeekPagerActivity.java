package org.stephenfox.dittimetables.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
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
public class TimetableWeekPagerActivity extends AppCompatActivity {

  private ViewPager pager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screen_slide);

    Intent d = getIntent();
    String url = d.getStringExtra("url");

    WeekDownloader weekDownloader = new WeekDownloader();
    weekDownloader.downloadWeekForCourse(url, new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        setup((String)data);
      }
    });
  }


  /**
   * Sets up the activity.
   * Note: Once the http data is downloaded from {@link #onCreate(Bundle)}
   * this method must be invoked to set this activity up correctly.
   *
   * @param data The http data.
   */
  void setup(String data) {
    try {
      ArrayList<TimetableSession> sessions = parseJson(data);
      Timetable timetable = createTimetable(sessions);

      pager = (ViewPager) findViewById(R.id.slide);
      pager.setAdapter(new SliderAdapter(getSupportFragmentManager(), timetable));

    } catch (EmptySessionsArrayException e) {
      Toast.makeText(getApplicationContext(),
          "No timetable available for this course", Toast.LENGTH_SHORT).show();
      this.finish();
    }
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
    return generator.generateTimetable();
  }


  private class SliderAdapter extends FragmentStatePagerAdapter {

    private Timetable timetable;


    public SliderAdapter(FragmentManager manager, Timetable timetable) {
      super(manager);
      this.timetable = timetable;
    }


    @Override
    public Fragment getItem(int position) {
      Day dayForFragment = Day.intToDay(position);
      return new TimetableWeekPageFragment().newInstance(this.timetable.getTimetableDay(dayForFragment));
    }

    @Override
    public int getCount() {
      //TODO: Don't always return 7. Default should be 5, then add more days if needed.
      return 5;
    }
  }
}
