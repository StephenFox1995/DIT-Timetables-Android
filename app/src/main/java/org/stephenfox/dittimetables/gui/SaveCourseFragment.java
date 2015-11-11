package org.stephenfox.dittimetables.gui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.stephenfox.dittimetables.R;

public class SaveCourseFragment extends Fragment {


  public static SaveCourseFragment newInstance() {
    SaveCourseFragment fragment = new SaveCourseFragment();
    fragment.setArguments(new Bundle());
    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.save_course_fragment, container, false);
  }
}
