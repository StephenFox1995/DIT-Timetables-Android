package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.AsyncDownloader;
import org.stephenfox.dittimetables.network.CourseDownloader;
import org.stephenfox.dittimetables.network.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AvailableCoursesActivity extends ListActivity {

  private HashMap<Integer, String> courseIdentifiersTitlesHash;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_available_courses);

    CourseDownloader cDownloader = new CourseDownloader();
    cDownloader.downloadCourseNamesAndIdentifiers(new AsyncDownloader.HttpAsyncCallback() {
      @Override
      public void finished(String data) {
        if (data != null) {
          Log.d("DITTimetables", "Course names and identifiers successfully downloaded." + data);

          // TODO: Never parse if data is not sufficient.
          courseIdentifiersTitlesHash = beginJSONParsing(data);
          ArrayList<String> courseTitles = formatDataForAdapter(courseIdentifiersTitlesHash);
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
   *         The key is the course id and the value is the course title.
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
    ArrayList<String> courseNames = new ArrayList<>();

    for (Integer key : data.keySet()) {
      courseNames.add(data.get(key));
    }
    return courseNames;
  }


  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    TextView textView = (TextView)v.findViewById(R.id.courseTitle);
    String courseTitle = textView.getText().toString();

    Log.d("Course title", courseTitle);
    Integer key = (Integer)getKeyFromValue(courseIdentifiersTitlesHash, courseTitle);

    Intent timetableWeekActivityIntent = new Intent(this, TimetableWeekPagerActivity.class);
    timetableWeekActivityIntent.putExtra("url", constructURLForCourseWeek(key));
    startActivity(timetableWeekActivityIntent);
  }


  private String constructURLForCourseWeek(Integer id) {
    return "http://timothybarnard.org/timetables/classes.php?courseID=" + id + "&semester=1";
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

      TextView courseTitle = (TextView) row.findViewById(R.id.courseTitle);
      courseTitle.setText(courseTitles.get(position));


      return row;
    }
  }
}
