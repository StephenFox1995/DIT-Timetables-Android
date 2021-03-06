package org.stephenfox.dittimetables.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.timetable.Day;
import org.stephenfox.dittimetables.timetable.Timetable;
import org.stephenfox.dittimetables.timetable.TimetableSourceRetriever;

/**
 * This class manages a set of fragments see
 * {@link org.stephenfox.dittimetables.gui.TimetableWeekPageFragment}
 * for details on each fragment.
 */
public class TimetableWeekPagerActivity extends AppCompatActivity implements
    ViewPager.OnPageChangeListener {

  private ViewPager pager;
  private Timetable timetable;
  private ProgressDialog progressDialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timetable_week_pager_activity);

    Intent d = getIntent();
    String courseCode = d.getStringExtra("courseCode");


    TimetableSourceRetriever sourceRetriever = new TimetableSourceRetriever(this);
    showProgressDialog();
    sourceRetriever.fetchTimetable(courseCode,
        new TimetableSourceRetriever.TimetableRetrieverCallback() {
          @Override
          public void timetableRetrieved(Timetable timetable) {
            removeProgressDialog();
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


  private void showProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setTitle("Loading...");
      progressDialog.setMessage("Grabbing information...");
      progressDialog.setCancelable(false);
    }
    progressDialog.show();
  }

  private void removeProgressDialog() {
    if (progressDialog == null) {
      return;
    }
    progressDialog.dismiss();
  }




  /**
   * Sets up the activity, with the appropriate timetable.
   *
   * @param timetable The timetable to set up the activity with.
   */
  void setup(Timetable timetable) {
    this.timetable = timetable;
    pager = (ViewPager) findViewById(R.id.slide);
    pager.setAdapter(new SliderAdapter(getSupportFragmentManager(), timetable));
    pager.addOnPageChangeListener(this);
    onPageSelected(0);
  }


  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageSelected(int position) {
    Day selectedDay = Day.intToDay(position);

    if (timetable.containsDay(selectedDay)) {
      updateActionBarWithDayWithTitle(timetable.getTimetableDay(selectedDay).getDayName());
    }
  }

  private void updateActionBarWithDayWithTitle(String title) {
    setTitle(title);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
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
      return
          new TimetableWeekPageFragment().newInstance(this.timetable.getTimetableDay(dayForFragment));
    }

    @Override
    public int getCount() {
      return timetable.getTimetableWeek().getNumberOfDays();
    }
  }
}
