package com.smartmatic.sitesurvey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.smartmatic.sitesurvey.AddNewDialogFragment.OnResultListener;
import com.smartmatic.sitesurvey.core.PollingStationParser;
import com.smartmatic.sitesurvey.core.SurveyAdapterBuilder;
import com.smartmatic.sitesurvey.core.SurveyParser;
import com.smartmatic.sitesurvey.core.TransmiterTask;
import com.smartmatic.sitesurvey.data.PollingStation;
import com.smartmatic.sitesurvey.data.Question;
import com.smartmatic.sitesurvey.data.Survey;

public class MainActivity extends Activity implements ActionBar.TabListener {

	private ProgressDialog pDialog;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
    JSONHandler JSONHandle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        JSONHandle= new JSONHandler();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				}
			});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		//Survey survey = new Survey(SurveyParser.GetQuestionary(getBaseContext()), SurveyParser.dependencies, SurveyParser.fullSize, SurveyParser.withKeyboardSize);
		//SurveyAdapterBuilder.setSurvey(survey);

		//PendingListFragment.setData(PollingStationParser.GetPollingStations(getApplicationContext()));

		new TransmiterTask().execute(getApplicationContext());
		GetJSON JSONFile = new GetJSON();
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
			JSONFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			JSONFile.execute();
	}

    /**
     * Created by Reynaldo on 10/11/2015.
     */

	private class GetJSON extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute(){
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

        protected Void doInBackground(Void... arg0) {

			String url = "http://192.168.1.114:12316/API/Form";

			try {
				// Simulate network access.
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return null;
			}

			// Getting JSON string from URL
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response*/
			String JSONString = sh.makeServiceCall(url, ServiceHandler.GET);
			Log.d("Response: ", "> " + JSONString);

            if (JSONHandle.PopulateStruct(JSONString)){

            }else{

            }

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
	    MenuItem menuItem = menu.findItem(R.id.action_search);


	    // When using the support library, the setOnActionExpandListener() method is
	    // static and accepts the MenuItem object as an argument
	    MenuItemCompat.setOnActionExpandListener(menuItem, new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Do something when collapsed
				return true;  // Return true to collapse action view
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Do something when expanded
				return true;  // Return true to expand action view
			}
	    });

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			Intent intent = new Intent();
	        intent.setClass(MainActivity.this, UserSettingActivity.class);
	        startActivityForResult(intent, 0);

	        return true;
		}
		else if (id == R.id.action_newPS){
			AddNewDialogFragment dialog = new AddNewDialogFragment();
			dialog.show(getFragmentManager(), "");

			dialog.SetOnResultListener(new OnResultListener() {

				@Override
				public void onResult(String title, String address, double longitude,
						double latitude) {

					PollingStation ps = new PollingStation();
					ps.title = title;
					ps.description = address;
                    /**
                     * Created by Reynaldo on 10/11/2015.
                     */

                    ps.lat = 10.5080572; //mSectionsPagerAdapter.mapFragment.getLatitude();
					ps.lon = -66.9102813; //mSectionsPagerAdapter.mapFragment.getLongitude();

					if(!PendingListFragment.Contains(ps)){
						PendingListFragment.AddNew(ps);
						Toast.makeText(getApplicationContext(), getText(R.string.MainActivity_added),
                                Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getApplicationContext(),
                                getText(R.string.MainActivity_alreadyExists),
                                Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());

		if(tab.getPosition() == 0 && mSectionsPagerAdapter.mapFragment!=null){
			mSectionsPagerAdapter.mapFragment.DrawMarkers();
		}
		if(tab.getPosition() == 3) {
			showSimplePopUp();

		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		SurveysMapFragment mapFragment;
		PendingListFragment pendingListFragment;
		FinishedListFragment finishedListFragment;
		FinishedListFragment jsonListFragment;
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).

			if(position == 0){
				if(mapFragment == null)
					mapFragment = new SurveysMapFragment();
				return  mapFragment;
			}
			else if(position == 1){
				if(pendingListFragment == null)
					pendingListFragment = new PendingListFragment();
				return  pendingListFragment;
			}
			else if(position == 2){
				if(finishedListFragment == null)
					finishedListFragment = new FinishedListFragment();
				return  finishedListFragment;
			}
			else{

				if(jsonListFragment == null)
					jsonListFragment = new FinishedListFragment();
				return  jsonListFragment;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section0);
			case 1:
				return getString(R.string.title_section1);
			case 2:
				return getString(R.string.title_section2);
			case 3:
				return getString(R.string.title_section3);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_main, container,false);
		}
	}

    /**
     * Created by Reynaldo on 10/11/2015.
     */
    private void showSimplePopUp() {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Formas");
		helpBuilder.setMessage("Name: " + JSONHandle.forms[0].TAG_NAME + "\nName: " +
                JSONHandle.forms[1].TAG_NAME + "\nName: " +
                JSONHandle.forms[2].TAG_NAME + "\nName: " +
                JSONHandle.forms[3].TAG_NAME + "\nName: " +
                JSONHandle.forms[4].TAG_NAME);
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}

}

