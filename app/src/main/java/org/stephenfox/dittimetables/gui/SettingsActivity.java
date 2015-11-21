package org.stephenfox.dittimetables.gui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.stephenfox.dittimetables.R;
import org.stephenfox.dittimetables.database.TimetableDatabase;
import org.stephenfox.dittimetables.preferences.TimetablePreferences;

public class SettingsActivity extends AppCompatActivity {

  Button removeTimetableButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);
    setTitle("Settings");

    removeTimetableButton = (Button)findViewById(R.id.delete_saved_timetable_button);
    removeTimetableButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Remove Timetable");
        builder.setItems(new CharSequence[]{"Remove", "Cancel"},
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                  case 0:
                    removeTimetableFromDatabase();
                    removeTimetablePreferences();
                    displayTimetableRemovedToast();
                    break;
                  default:
                    break;
                }
              }
            });
        AlertDialog removeTimetableAlert = builder.create();
        removeTimetableAlert.show();
      }
    });
  }


  /**
   * Deletes a timetable from the database.
   * */
  private void removeTimetableFromDatabase() {
    TimetableDatabase database = new TimetableDatabase(this);
    database.deleteTimetable();
  }


  /**
   * Removes the preferences for a timetable.
   * */
  private void removeTimetablePreferences() {
    TimetablePreferences.removeAllPreferences(this);
  }

  private void displayTimetableRemovedToast() {
    Toast.makeText(this,
        "Timetable successfully removed from device!", Toast.LENGTH_SHORT).show();
  }
}
