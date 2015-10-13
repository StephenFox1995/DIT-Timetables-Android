package org.stephenfox.dittimetables.gui;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.network.Common;
import org.stephenfox.dittimetables.network.HttpDownloader;

public class AvailableCoursesActivity extends ListActivity {

    String[] dummyCourses = {"DT228", "DT333"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_courses);

        setListAdapter(new CourseListAdapter(this, dummyCourses));

        //HttpDownloader downloader = new HttpDownloader();
        //downloader.getHttpData(Common.coursesURL);


    }

}
