package org.stephenfox.dittimetables.gui;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.database.TimetableSchema;
import org.stephenfox.dittimetables.network.CourseDownloader;
import org.stephenfox.dittimetables.network.CustomAsyncTask;
import org.stephenfox.dittimetables.network.JsonParser;
import org.stephenfox.dittimetables.network.NetworkManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AvailableCoursesActivity extends ListActivity {

  private HashMap<Integer, String> courseIdentifiersToCourseCodesHash;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_available_courses);

    Log.d("SF", "Scheam" + TimetableSchema.Timetable.CREATE_TABLE);

    if (NetworkManager.hasInternetConnection(this)) {
      beginDownload();
    } else {
      TextView noConnection = (TextView)findViewById(R.id.no_connection);
      noConnection.setText("Could not connect to network!");
      // TODO: Add callback method to be notified if we get connection.
    }
  }


  /**
   * Sets up the relevant action listeners that
   * are needed when the course ids and codes are downloaded.
   * i.e long press to save a course.
   */
  void addRelevantActionListeners() {
    this.getListView().setLongClickable(true);
    this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.courseCode);
        String courseCode = textView.getText().toString();
        Integer courseID = (Integer) getKeyFromValue(courseIdentifiersToCourseCodesHash, courseCode);
        addSaveCourseFragmentToViewHierarchy(courseID);
        return true;
      }
    });
  }


  private void beginDownload() {
    CourseDownloader cDownloader = new CourseDownloader();
    cDownloader.downloadCourseNamesAndIdentifiers(new CustomAsyncTask.AsyncCallback() {
      @Override
      public void finished(Object data) {
        if (data != null) {
          addRelevantActionListeners();
          String courses = (String) data;
          // TODO: Never parse if data is not sufficient.
          courseIdentifiersToCourseCodesHash = beginJSONParsing(courses);
          ArrayList<String> courseTitles = formatDataForAdapter(courseIdentifiersToCourseCodesHash);
          setListAdapter(new CourseListAdapter(getApplicationContext(), courseTitles));
        }
      }
    });
  }


  /**
   * Begins the parsing of the Json data retrieved from
   * the http request.
   *
   * @param data The JSON data.
   *
   * @return A HashMap with a key value pair of <Integer, String>
   *         The key is the course id and the value is the course code.
   */
  private HashMap<Integer, String> beginJSONParsing(String data) {
    JsonParser jsonParser = new JsonParser();
    return jsonParser.parseTitlesAndIdentifiers(data);
  }


  /**
   * Formats the data retrieved from the server into
   * a format recognizable by CourseListAdapter.
   *
   * @param data A HashMap
   *
   * @return An ArrayList containing all the course titles in a
   *         format that can be given to the CourseListAdapter.
   */
  private ArrayList<String> formatDataForAdapter(HashMap<Integer, String> data) {
    ArrayList<String> courseCodes = new ArrayList<>();

    for (Integer key : data.keySet()) {
      courseCodes.add(data.get(key));
    }
    return courseCodes;
  }



  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    TextView courseCodeTextView = (TextView)v.findViewById(R.id.courseCode);
    String courseCode = courseCodeTextView.getText().toString();

    Integer courseID = (Integer)getKeyFromValue(courseIdentifiersToCourseCodesHash, courseCode);

    Intent timetableWeekActivityIntent = new Intent(this, TimetableWeekPagerActivity.class);
    timetableWeekActivityIntent.putExtra("courseCode", courseCode);
    timetableWeekActivityIntent.putExtra("courseID", Integer.toString(courseID));

    startActivity(timetableWeekActivityIntent);
  }


  private void addSaveCourseFragmentToViewHierarchy(int courseID) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    String _courseID = Integer.toString(courseID);
    String courseCode = courseIdentifiersToCourseCodesHash.get(courseID);
    SaveCourseFragment fragment = SaveCourseFragment.newInstance(_courseID, courseCode);

    fragmentTransaction.add(R.id.save_course_placeholder, fragment);
    fragmentTransaction.commit();
  }




  /**
   * Returns the key from using a value in a hashmap.
   * Seems slightly odd but needed in this case.
   * See:
   *  {@link #"http://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value"}
   */
  private Object getKeyFromValue(Map hm, Object value) {
    for (Object o : hm.keySet()) {
      if (hm.get(o).equals(value)) {
        return o;
      }
    }
    return null;
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
