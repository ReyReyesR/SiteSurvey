package com.smartmatic.sitesurvey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.smartmatic.sitesurvey.core.TransmiterAlertTask;
import com.smartmatic.sitesurvey.core.WebServiceClient;
import com.smartmatic.sitesurvey.data.DataAlert;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class UserSettingActivity extends Activity {
	

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onCreate(savedInstanceState);
  
  getFragmentManager().beginTransaction().replace(android.R.id.content,
                new UserSettingFragment()).commit();
 }
 
 public class UserSettingFragment extends PreferenceFragment {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
	
		Preference pref = findPreference("manualLocation_preference");        
	      pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
	          @Override
	          public boolean onPreferenceChange(Preference preference, Object newValue) {
	        	  String in_out = "Out";
	        	  
	        	  if ((Boolean)newValue == false)
	        		  in_out = "In";
	        	  
	        	  new TransmiterAlertTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getApplicationContext(),in_out);
	        	  return true;
	          }
	      });
	
	}
 }
}