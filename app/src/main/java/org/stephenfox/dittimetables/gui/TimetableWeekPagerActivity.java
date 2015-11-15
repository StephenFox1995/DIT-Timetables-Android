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
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableSession;
import org.stephenfox.dittimetables.timetable.TimetableSourceRetriever;

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
    setContentView(R.layout.timetable_week_pager_activity);

    Intent d = getIntent();
    String courseCode = d.getStringExtra("courseCode");
    String courseID = d.getStringExtra("courseID");

    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    sourceRetriever.fetchTimetable(courseCode, courseID,
        new TimetableSourceRetriever.TimetableRetrieverCallback() {
          @Override
          public void timetableRetrieved(Timetable timetable) {
            if (timetable != null) {
              setup(timetable);
            } else {
              Toast.makeText(getApplicationContext(),
                  "No timetable available for this course", Toast.LENGTH_SHORT).show();
              TimetableWeekPagerActivity.this.finish();
            }
          }
        });
  }


  /**
   * Sets up the activity, with the appropriate timetable.
   *
   * @param timetable The timetable to set up the activity with.
   */
  void setup(Timetable timetable) {
    pager = (ViewPager) findViewById(R.id.slide);
    pager.setAdapter(new SliderAdapter(getSupportFragmentManager(), timetable));
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
    return jsonParser.parseSessionsForTimetable(data);
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
      return timetable.getTimetableWeek().getNumberOfDays();
    }
  }
}
