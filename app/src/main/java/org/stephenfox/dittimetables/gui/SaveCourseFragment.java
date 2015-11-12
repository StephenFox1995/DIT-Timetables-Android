package org.stephenfox.dittimetables.gui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.stephenfox.dittimetables.R;

public class SaveCourseFragment extends Fragment {

  private Button saveCourseButton;
  private Button cancelActionButton;

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

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

  }

  public Button getCancelActionButton() {
    if (this.cancelActionButton == null) {
      //this.cancelActionButton = (Button)getActivity().findViewById(R.id.cancel_button);
    }
    return cancelActionButton;
  }

  public Button getSaveCourseButton() {
    if (saveCourseButton == null) {
      //this.saveCourseButton = (Button)getActivity().findViewById(R.id.save_course_button);
    }
    return saveCourseButton;
  }
}
