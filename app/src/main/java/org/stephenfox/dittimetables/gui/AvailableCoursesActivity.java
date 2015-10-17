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
import org.stephenfox.dittimetables.network.CourseDownloader;
import org.stephenfox.dittimetables.network.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

public class AvailableCoursesActivity extends ListActivity {

  private HashMap<Integer, String> courseIdentifiersAndNames;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_available_courses);

    CourseDownloader cDownloader = new CourseDownloader();

    cDownloader.downloadCourseNamesAndIdentifiers(new HttpAsyncCallback() {
      @Override
      public void finished(String data) {
        if (data != null) {
          Log.v("DITTimetables", "Course names and identifiers successfully downloaded." + data);

          // TODO: Never parse if data is not sufficient.
          beginJSONParsing(data);
        }
      }
    });
  }


  private void beginJSONParsing(String data) {
    JsonParser jsonParser = new JsonParser();
    this.courseIdentifiersAndNames = jsonParser.parseTitlesAndIdentifiers(data);
    formatDataForAdapter(courseIdentifiersAndNames);
  }


  private void formatDataForAdapter(HashMap<Integer, String> data) {
    ArrayList<String> courseNames = new ArrayList<>();

    for (Integer key : data.keySet()) {
      courseNames.add(data.get(key));
    }

    setListAdapter(new CourseListAdapter(this, courseNames) );
  }


  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Intent timetableWeekActivityIntent = new Intent(this, TimetableWeekPagerActivity.class);
    startActivity(timetableWeekActivityIntent);
  }




  public class CourseListAdapter extends BaseAdapter {

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

      TextView courseName = (TextView) row.findViewById(R.id.courseName);
      courseName.setText(courseTitles.get(position));

      return row;
    }
  }
}
