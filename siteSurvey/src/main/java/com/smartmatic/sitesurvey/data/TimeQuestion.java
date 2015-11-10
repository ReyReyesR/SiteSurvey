package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class TimeQuestion extends Question implements Cloneable {
		
	public TimeQuestion(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
		space = 5;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.time_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);

    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}
    	
    	TimePicker timePicker = (TimePicker)view.findViewById(R.id.timePicker1);
    	
    	if(!answer.equals("")){
    		String[] parts = answer.split(":");
    		timePicker.setCurrentHour(Integer.parseInt(parts[0]));
    		timePicker.setCurrentMinute(Integer.parseInt(parts[1]));
    	}
    	timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            	answer = (hourOfDay +":"+ minute);
            }
        });
    	
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		
		return new TimeQuestion(name, label, "", fontSize, color);
	}

	@Override
	public Question clone() {
		
		return new TimeQuestion(name, label, "", String.valueOf(fontSize), color);
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