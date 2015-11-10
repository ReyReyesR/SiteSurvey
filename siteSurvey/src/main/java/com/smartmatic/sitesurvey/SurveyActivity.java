package com.smartmatic.sitesurvey;

import java.util.ArrayList;
import java.util.Date;

import com.smartmatic.sitesurvey.core.SurveyAdapterBuilder;
import com.smartmatic.sitesurvey.data.Question;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent; 
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SurveyActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SurveyAdapter mSurveyAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	public ViewPager mViewPager;
	public int pId;
	public int depId;
	public String depName;
	int lastPosition = 0;
	public static Date startedDate = new Date();
	
	public ArrayList<OnResultListener> resultListeners = new ArrayList<SurveyActivity.OnResultListener>();
	
	public interface OnResultListener 
    {
        void onResult(int requestCode, int resultCode, Intent data);
    }
	
	private class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
	    @Override
	    public void onPageSelected(int position) {
	    	try{
		    	for (Question q: mSurveyAdapter.survey.pages.get(position)){
		    		if(q.getClass().toString().contains("Text")){
		    			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		    			return;
		    		}
		    	}
		    	
		    	int pageToHide = 0;
		    	if(position > lastPosition){
		    		pageToHide= (lastPosition == 0)? 1:2;
		    	}
		    	((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
			    	hideSoftInputFromWindow(mViewPager.getChildAt(pageToHide).getApplicationWindowToken(),0);
		    	
		    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	    	
	    	}
	    	catch(Exception e){
	    	}
	    	
	    	super.onPageSelected(position);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		Bundle b = getIntent().getExtras();
		pId = b.getInt("PLACE_ID");
		depName = (String) b.getCharSequence("DEPENDENCY_NAME");
		depId =  b.getInt("DEPENDENT_ID");
		
		if(depName!= null){
			mSurveyAdapter = SurveyAdapterBuilder.getDependentAdapter(this, pId, depId, depName,getFragmentManager());
		}else{
			mSurveyAdapter = SurveyAdapterBuilder.BuildAdapter(this, pId, getFragmentManager());
		}
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);		
		mViewPager.setAdapter(mSurveyAdapter);
		

		//mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		
		//Bind the title indicator to the adapter
		CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titles);
		titleIndicator.setViewPager(mViewPager);
		titleIndicator.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.survey, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
			if(depName!=null && !depName.equals("")){
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result","Child");
				setResult(1,returnIntent);
				finish();
			}
		}catch(Exception e){}
	}
	//TODO:
	//PENDIENTE
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
			if(data==null){
				mSurveyAdapter = SurveyAdapterBuilder.BuildAdapter(this, pId, getFragmentManager());
				mSurveyAdapter.notifyDataSetChanged();
			}
			else{
				if(resultListeners.size() > 0)
					for(OnResultListener l: resultListeners){
						l.onResult(requestCode, resultCode, data);
					}
			}
		}catch(Exception e){}
    }
	
	public void addOnResultListener(OnResultListener l) {
		resultListeners.add(l);
	}
	
}
