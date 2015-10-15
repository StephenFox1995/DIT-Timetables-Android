package org.stephenfox.dittimetables.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;


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
