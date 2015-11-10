package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class RepeatQuestion extends Question implements Cloneable {
	
	public int dependentId;
	public String dependencyName;
	private Button button = null;
		
	
	public RepeatQuestion(String questionName, String questionLabel, String _fontSize, String _color,int depId, String depName) {
		super(questionName, questionLabel, "",_fontSize,_color);	
		space = 1;
		
		dependentId = depId;
		dependencyName = depName;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, final SurveyActivity activityRef, final SurveyAdapter surveyAdapter, final int parentId){
		
		View view =  inflater.inflate(R.layout.repeat_question,container, false);
		
		TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}	
    	
    	button = (Button)view.findViewById(R.id.button1);
    	((ViewGroup)view).removeView(button);
    	//button = new Button(view.getContext());
    	//button.setText("Answer it");
    	button.setTypeface(tf);
    	button.setBackground(view.getResources().getDrawable(R.drawable.blue_button));
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(v.getContext(), SurveyActivity.class);
				i.putExtra("PLACE_ID", parentId);
				i.putExtra("DEPENDENT_ID", dependentId);
				i.putExtra("DEPENDENCY_NAME", dependencyName);
				activityRef.startActivityForResult(i,1);		
				//survey.pages.add(getArguments().getInt(ARG_PAGE) +1, question );
				surveyAdapter.notifyDataSetChanged();
				
				button.setBackgroundColor(Color.parseColor("#C0C0C0"));
				button.setText(v.getContext().getText(R.string.answered));
			}
		} );
    	
    	((ViewGroup)view).addView(button);
    	
    	return view;
	}
	
	
	public static Question CreateFromXML(XmlPullParser parser){
		return null;
	}
	
	@Override
	public Question clone(){
		return new RepeatQuestion(name,label,String.valueOf(fontSize),color,dependentId, dependencyName);
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
