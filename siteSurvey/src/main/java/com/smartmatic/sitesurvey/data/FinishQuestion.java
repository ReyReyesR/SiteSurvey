package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class FinishQuestion extends Question implements Cloneable {
	
	public String backColor;
	int pId;
	//int depId;
	String depName;
		
	public FinishQuestion() {
		super("", "", "");	
		space = 1;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, final SurveyActivity activityRef, final SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.finish_button,container, false);
    	Button button = (Button)view.findViewById(R.id.button1);
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	button.setTypeface(tf);
    	pId = activityRef.pId;
    	//depId = ((SurveyActivity)activityRef).depId;
    	depName = activityRef.depName;
    	
    	
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//If this is not a dependency
				if(depName == null){
					
					PollingStation ps = PendingListFragment.Get(pId);
					if(ps!=null){
						FinishedListFragment.AddNew(ps);
						PendingListFragment.Remove(ps);
					}
				}
				activityRef.finish();
			}
		} );
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		return new FinishQuestion();
	}
	
	@Override
	public Question clone(){
		return new FinishQuestion();
	}

	@Override
	public void setAnsweredListener(AnsweredListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}
