package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class YesNoQuestion extends Question implements Cloneable{
		
	public YesNoQuestion(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
		space = 2;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.yesno_question,container, false);
		Switch control = (Switch)view.findViewById(R.id.switch1);
		
		((ViewGroup)view).removeView(control);
		control = new Switch(view.getContext());
    	
		control.setText(this.label);
		control.setTextSize(fontSize);
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	control.setTypeface(tf);
    	try{
    		control.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}		
    	
    	if(!answer.equals("")){
    		if(answer.equals("true")){
    			control.setChecked(true);
    		}
    		else{
            	control.setChecked(false);
    		}
    	}else{
        	control.setChecked(false);
    	}
    	
    	control.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				answer = (String.valueOf(isChecked)); 
			}
		});
    	
    	((ViewGroup)view).addView(control);

		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		
		return new YesNoQuestion(name, label, "", fontSize, color);
	}

	@Override
	public Question clone() {
		return new YesNoQuestion(name, label, "", String.valueOf(fontSize), color);
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
