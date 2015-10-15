package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.Common;
import org.stephenfox.dittimetables.network.HttpDownloader;

public class AvailableCoursesActivity extends ListActivity {

    String[] dummyCourses = {"DT228", "DT333", "DT444", "DT442"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_courses);

        setListAdapter(new CourseListAdapter(this, dummyCourses));

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent timetableWeekDisplayIntent = new Intent(AvailableCoursesActivity.this, TimetableWeekDisplay.class);

        //timetableWeekDisplayIntent.putExtra("courseRequestURL", dummyCourses[position]);

        startActivity(timetableWeekDisplayIntent);
    }
}
