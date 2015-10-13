package org.stephenfox.dittimetables.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class CourseListAdapter extends BaseAdapter {

    private String[] courses;

    public CourseListAdapter(Context context, String[] courses) {
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
