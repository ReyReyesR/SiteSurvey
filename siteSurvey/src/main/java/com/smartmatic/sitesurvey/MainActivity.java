package com.smartmatic.sitesurvey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.smartmatic.sitesurvey.AddNewDialogFragment.OnResultListener;

import com.smartmatic.sitesurvey.core.PollingStationParser;
import com.smartmatic.sitesurvey.core.SurveyAdapterBuilder;
import com.smartmatic.sitesurvey.core.SurveyParser;
import com.smartmatic.sitesurvey.core.TransmitterTask;
import com.smartmatic.sitesurvey.data.PollingStation;
import com.smartmatic.sitesurvey.data.Question;
import com.smartmatic.sitesurvey.data.Survey;

import java.io.IOException;
import java.util.ArrayList;

/**<p>
 * The Main Activity of siteSurvey has four primary objectives
 * First it obtains a JSON object with survey information that is used to create surveys
 * Second initializes and sets the new surveys
 * Third initializes the polling station locations where surveys are done
 * Fourth it creates a Fragment view of the map, new and complete surveys
 *
 * @author Reynaldo
 * </p>
 */

public class MainActivity extends Activity implements ActionBar.TabListener {

    private ProgressDialog pDialog;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    String JSONString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            /*Create a tab with text corresponding to the page title defined by the adapter. Also
            specify this Activity object, which implements the TabListener interface, as the
            callback (listener) for when this tab is selected.*/
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        new TransmitterTask().execute(getApplicationContext());
        //Object of function that gets JSON Objects
        getJSON JSONFile = new getJSON();
        //Checks for Build Version for Async tasks
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
            JSONFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            JSONFile.execute();
    }

    /**<p>
     * This function is an AsyncTask that does 3 processes, first blocks the UI thread and creates a
     * worker thread. second the worker thread calls a function that establishes a connection with a
     * host and obtains a JSON object and last the worer thread ends and the function that
     * initializes Survey is called.
     * </p>
     *
     * @author Reynaldo
     * @see AsyncTask
     */

    private class getJSON extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute(){
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return null;
            }
            // Getting JSON string from URL
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting a JSON in response
            try {
                JSONString = sh.getServiceCall(FileReader.getUrl(getApplicationContext(),1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + JSONString);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            //Creation of a Survey with the JSON Object
            ArrayList<Question> question = SurveyParser.getQuestionary(JSONString);
            Survey survey = new Survey(question, SurveyParser.dependencies, SurveyParser.fullSize, SurveyParser.withKeyboardSize);
            SurveyAdapterBuilder.setSurvey(survey);
            PendingListFragment.setData(PollingStationParser.getPollingStations(getApplicationContext()));
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
                    ps.lat =mSectionsPagerAdapter.mapFragment.getLatitude(); //10.5080572
                    ps.lon =mSectionsPagerAdapter.mapFragment.getLongitude(); //-66.9102813

                    if(!PendingListFragment.contains(ps)){
                        PendingListFragment.addNew(ps);
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
        //FinishedListFragment jsonListFragment;
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
            else {
                if(finishedListFragment == null)
                    finishedListFragment = new FinishedListFragment();
                return  finishedListFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        @Override
        //Fragment Tab title
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_section0);
                case 1:
                    return getString(R.string.title_section1);
                case 2:
                    return getString(R.string.title_section2);
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
}
