package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;

public class AvailableCoursesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_courses);


        setListAdapter(new CourseListAdapter(this, dummyCourses));

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent timetableWeekDisplayIntent = new Intent(this, TimetableWeekDisplay.class);
        timetableWeekDisplayIntent.putExtra("courseRequestURL", dummyCourses[position]);
        startActivity(timetableWeekDisplayIntent);
    }



    public class CourseListAdapter extends BaseAdapter {

        private String[] courses;
        LayoutInflater inflater = null;

        public CourseListAdapter(Context context, String[] courses) {
            this.courses = courses;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() { return courses.length; }

        @Override
        public Object getItem(int position) { return courses[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                row = inflater.inflate(R.layout.course_row, null);
            }

            TextView courseName = (TextView) row.findViewById(R.id.courseName);
            courseName.setText(courses[position]);

            return row;
        }
    }
}