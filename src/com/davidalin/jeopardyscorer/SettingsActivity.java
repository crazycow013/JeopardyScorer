package com.davidalin.jeopardyscorer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    System.out.println("how we doing?");
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
    System.out.println("error?");
  }
}