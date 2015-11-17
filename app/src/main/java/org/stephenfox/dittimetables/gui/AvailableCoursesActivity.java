package org.stephenfox.dittimetables.gui;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.network.CourseDownloader;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.NetworkManager;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;
import org.stephenfox.dittimetables.timetable.TimetableIDWrapper;

import java.util.ArrayList;
import java.util.HashMap;


public class AvailableCoursesActivity extends FragmentActivity implements
    AdapterView.OnItemClickListener,
    SaveCourseFragment.SaveCourseDelegate {

  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_available_courses);

    listView = (ListView)findViewById(R.id.listview);

    if (NetworkManager.hasInternetConnection(this)) {
      beginDownload();
    } else {
      TextView noConnection = (TextView)findViewById(R.id.no_connection);
      noConnection.setText("Could not connect to network!");
      // TODO: Add callback method to be notified if we get connection.
    }
  }


  // Downloads all the JSON data from the server.
  private void beginDownload() {
    CourseDownloader cDownloader = new CourseDownloader();
    cDownloader.downloadCourseNamesAndIdentifiers(new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        if (data != null) {
          addRelevantActionListeners();
          TimetableIDWrapper.setTimetableIdentifiersHash((String) data);
          ArrayList<String> courseTitles = formatDataForAdapter(TimetableIDWrapper.getHash());
          listView.setAdapter(new CourseListAdapter(getApplicationContext(), courseTitles));
          listView.setOnItemClickListener(AvailableCoursesActivity.this);
        }
      }
    });
  }


  /**
   * Sets up the relevant action listeners that
   * are needed when the course ids and codes are downloaded.
   * i.e long press to save a course.
   */
  void addRelevantActionListeners() {
    listView.setLongClickable(true);
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.courseCode);
        String courseCode = textView.getText().toString();
        Integer courseID = TimetableIDWrapper.getTimetableIDForCourseCode(courseCode);
        addSaveCourseFragmentToViewHierarchy(courseCode);
        return true;
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    TextView courseCodeTextView = (TextView)view.findViewById(R.id.courseCode);
    String courseCode = courseCodeTextView.getText().toString();
    Integer courseID = TimetableIDWrapper.getTimetableIDForCourseCode(courseCode);

    Intent timetableWeekActivityIntent = new Intent(this, TimetableWeekPagerActivity.class);

    timetableWeekActivityIntent.putExtra("courseCode", courseCode);
    timetableWeekActivityIntent.putExtra("courseID", Integer.toString(courseID));

    startActivity(timetableWeekActivityIntent);
  }



  private void addSaveCourseFragmentToViewHierarchy(String courseCode) {
    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

    int courseID = TimetableIDWrapper.getTimetableIDForCourseCode(courseCode);
    SaveCourseFragment fragment = SaveCourseFragment.newInstance(Integer.toString(courseID), courseCode);
    fragment.setDelegate(this);
    fragmentTransaction.add(R.id.save_course_placeholder, fragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }


  @Override
  public void fragmentHasSavedCourse(boolean isRemoving) {
    if (isRemoving) {
      TimetableDatabase database = new TimetableDatabase(this);
      String[] groups = database.getGroups();

      ChooseGroupFragment fragment = ChooseGroupFragment.newInstance(groups, true);
      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.save_course_placeholder, fragment);
      fragmentTransaction.addToBackStack(null);
      fragmentTransaction.commit();

      fragment.setGroupChosenCallback(new ChooseGroupFragment.ChooseGroupCallback() {
        @Override
        public void groupChosen(String group) {
          TimetablePreferences preferences = new TimetablePreferences(AvailableCoursesActivity.this);
          preferences.setCourseGroupPreference(group);
        }
      });
    }
  }



  /**
   * Formats the data retrieved from the server into
   * a format recognizable by CourseListAdapter.
   *
   * @param data A HashMap
   *
   * @return An ArrayList containing all the course codes in a
   *         format that can be given to the CourseListAdapter.
   */
  private ArrayList<String> formatDataForAdapter(HashMap<String, Integer> data) {
    ArrayList<String> courseCodes = new ArrayList<>();

    for (String key : data.keySet()) {
      courseCodes.add(key);
    }
    return courseCodes;
  }


  private class CourseListAdapter extends BaseAdapter {

    private ArrayList<String> courseTitles;
    LayoutInflater inflater = null;



    public CourseListAdapter(Context context, ArrayList<String> courseTitles) {
      this.courseTitles = courseTitles;
      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
      return courseTitles.size();
    }

    @Override
    public Object getItem(int position) {
      return courseTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View row = convertView;

      if (row == null) {
        row = inflater.inflate(R.layout.course_row, null);
      }

      TextView courseTitle = (TextView) row.findViewById(R.id.courseCode);
      courseTitle.setText(courseTitles.get(position));


      return row;
    }
  }
}
