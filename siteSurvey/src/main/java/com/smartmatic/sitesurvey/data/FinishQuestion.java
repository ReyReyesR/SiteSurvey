package com.smartmatic.sitesurvey.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.*;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;


public class FinishQuestion extends Question implements Cloneable {

	int pId;
	String depName;
		
	public FinishQuestion(int FkSection, int FkQuestion, int FkAnswer, String Value) {
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
    	depName = activityRef.depName;
    	
    	
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//If this is not a dependency
				if(depName == null){
					
					PollingStation ps = PendingListFragment.get(pId);
					if(ps!=null){
						FinishedListFragment.AddNew(ps);
						PendingListFragment.remove(ps);
					}
				}
				activityRef.finish();
			}
		} );
		return view;	
	}
	
	public static Question createFromJSON(JSONObject json)throws JSONException {
		
		return new FinishQuestion(0,0,0,null);
	}
	
	@Override
	public Question clone(){
		return new FinishQuestion(idSection,idQuestion,idAnswer,answer);
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

