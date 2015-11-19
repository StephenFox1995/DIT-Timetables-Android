package org.stephenfox.dittimetables.gui;


import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.network.CourseAndServerIDsCache;
import org.stephenfox.dittimetables.network.CourseDownloader;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.NetworkManager;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;
import org.stephenfox.dittimetables.utilities.Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class AvailableCoursesActivity extends AppCompatActivity implements
    AdapterView.OnItemClickListener,
    SaveCourseFragment.CourseSavedCallback,
    SearchView.OnQueryTextListener {

  private ListView listView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_available_courses);

    listView = (ListView)findViewById(R.id.listview);

    if (TimetablePreferences.getTimetableSavedPreference(this)) {
      showAssistantActivity();
    }
    else {
      if (NetworkManager.hasInternetConnection(this)) {
        Log.d("SF", "We have a connection :)");
        beginDownload();
      } else {
        TextView noConnection = (TextView)findViewById(R.id.no_connection);
        noConnection.setText("Could not connect to network!");
        // TODO: Add callback method to be notified if we get connection.
      }
    }
  }


  private void showAssistantActivity() {
    Intent showTimetableAssistant = new Intent(this, DayAssistantActivity.class);
    startActivity(showTimetableAssistant);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.search, menu);

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
    SearchableInfo s = searchManager.getSearchableInfo(getComponentName());
    searchView.setSearchableInfo(s);
    searchView.setIconifiedByDefault(true);
    searchView.setOnQueryTextListener(this);
    return true;
  }


  @Override
  public boolean onQueryTextChange(String newText) {
    return performSearch(newText);
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return performSearch(query);
  }


  private boolean performSearch(String string) {
    // Don't perform search if somehow we've lost connection
    // as it will be useless.
    if (!NetworkManager.hasInternetConnection(this))
      return false;

    HashMap<String, Integer> coursesAndServersIDsHash = CourseAndServerIDsCache.getHash();
    Search courseSearch = new Search();
    String[] resultSet =
        courseSearch.performStringSearch(coursesAndServersIDsHash.keySet(), string, false);

    if (resultSet.length == 0) {
      return false;
    }

    listView.setAdapter(new CourseListAdapter(getApplicationContext(),
        new ArrayList<>(Arrays.asList(resultSet))));
    return true;
  }



  // Downloads all the JSON data from the server.
  private void beginDownload() {
    CourseDownloader cDownloader = new CourseDownloader();
    cDownloader.downloadCourseNamesAndIdentifiers(new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        if (data != null) {
          addRelevantActionListeners();
          CourseAndServerIDsCache.setTimetableIdentifiersHash((String) data);
          ArrayList<String> courseTitles = formatDataForAdapter(CourseAndServerIDsCache.getHash());
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
        addSaveCourseFragmentToViewHierarchy(courseCode);
        return true;
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    TextView courseCodeTextView = (TextView)view.findViewById(R.id.courseCode);
    String courseCode = courseCodeTextView.getText().toString();

    Intent timetableWeekActivityIntent = new Intent(this, TimetableWeekPagerActivity.class);
    timetableWeekActivityIntent.putExtra("courseCode", courseCode);
    startActivity(timetableWeekActivityIntent);
  }



  private void addSaveCourseFragmentToViewHierarchy(String courseCode) {
    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

    String courseID = CourseAndServerIDsCache.getTimetableIDForCourseCode(courseCode);
    SaveCourseFragment fragment = SaveCourseFragment.newInstance(courseID, courseCode);
    fragment.setCallback(this);
    fragmentTransaction.add(R.id.save_course_placeholder, fragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }



  @Override
  public void fragmentHasSavedCourse(final String courseCode, boolean isRemoving) {
    if (isRemoving) {
      TimetableDatabase database = new TimetableDatabase(this);
      String[] groups = database.getGroups();

      final ChooseGroupFragment fragment = ChooseGroupFragment.newInstance(courseCode, groups, true);
      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.save_course_placeholder, fragment);
      fragmentTransaction.addToBackStack(null);
      fragmentTransaction.commit();

      fragment.setGroupChosenCallback(new ChooseGroupFragment.ChooseGroupCallback() {
        @Override
        public void groupChosen(String courseCode, String group) {
          TimetablePreferences.setCourseGroupPreference(AvailableCoursesActivity.this, group);
          TimetablePreferences.setCourseCodePreference(AvailableCoursesActivity.this, courseCode);
          TimetablePreferences.setTimetableSavedPreference(AvailableCoursesActivity.this, true);
          getFragmentManager().beginTransaction().remove(fragment).commit();
          showAssistantActivity();
        }

        @Override
        public void choosingGroupCancelled() {
          getFragmentManager().beginTransaction().remove(fragment).commit();
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
