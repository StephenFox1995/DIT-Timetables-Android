package org.stephenfox.dittimetables.gui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.stephenfox.dittimetables.R;

public class ChooseGroupFragment extends ListFragment {

  String[] groups;

  public static ChooseGroupFragment newInstance(String[] groups) {
    Bundle args = new Bundle();
    args.putStringArray("groups", groups);
    return new ChooseGroupFragment();
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.choose_group_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Bundle args = getArguments();
    this.groups = args.getStringArray("groups");

    ArrayAdapter<String> arrayAdapter =
        new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.course_row, groups);
    setListAdapter(arrayAdapter);
  }
}
