package org.stephenfox.dittimetables.gui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.stephenfox.dittimetables.R;

import java.util.ArrayList;
import java.util.Arrays;


public class ChooseGroupFragment extends ListFragment {

  String[] groups;
  boolean allSelectionEnabled;
  private ChooseGroupCallback groupChosenCallback;

  /**
   * Creates a new ChooseFragment instance.
   *
   * @param groups The groups to choose from.
   * @param allSelectionEnabled Determines whether or not the ALL row will be present on the list.
   *                            Please note: If the timetable does not contain groups, all will be
   *                            included in the list regardless of the value of allSelectionEnabled.
   *
   * @return A new instance of ChooseGroupFragment.
   */
  public static ChooseGroupFragment newInstance(String[] groups, boolean allSelectionEnabled) {
    Bundle args = new Bundle();
    args.putStringArray("groups", groups);
    args.putBoolean("allSelectionEnabled", allSelectionEnabled);
    ChooseGroupFragment fragment = new ChooseGroupFragment();
    fragment.setArguments(args);
    return fragment;
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
    this.allSelectionEnabled = args.getBoolean("allSelectionEnabled");

    setupList();

    // Become listener for clicks on ChooseCourseFragment
    getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selection = ((TextView) view).getText().toString();
        groupChosenCallback.groupChosen(selection);
      }
    });
  }


  private void setupList() {
    String[] listSelection = shouldIncludeAllSelectionItem();
    ArrayAdapter<String> arrayAdapter =
        new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.choose_group_row, listSelection);
    setListAdapter(arrayAdapter);
  }


  /**
   * Determines if the list selection should include the option to select
   * all groups. Note if there are no groups for a timetable, the all selection
   * will be present by default.
   *
   * @return A new string array of all the items the list should include
   *         depending on whether or not all selection is included.
   **/
  private String[] shouldIncludeAllSelectionItem() {
    if (groups == null) {
      return new String[] {"All"};
    }

    ArrayList<String> listSelection = new ArrayList<>(Arrays.asList(groups));
    if (allSelectionEnabled) {
      listSelection.add("All");
      return listSelection.toArray(new String[listSelection.size()]);
    }
    return listSelection.toArray(new String[listSelection.size()]);
  }

  public void setGroupChosenCallback(ChooseGroupCallback groupChosenCallback) {
    this.groupChosenCallback = groupChosenCallback;
  }

  interface ChooseGroupCallback {
    void groupChosen(String group);
  }
}
