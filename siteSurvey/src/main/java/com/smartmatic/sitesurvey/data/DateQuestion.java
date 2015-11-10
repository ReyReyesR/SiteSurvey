package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class DateQuestion extends Question implements Cloneable {
	
	public DateQuestion(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
		space = 5;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.date_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);

        Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
        label.setTypeface(tf);
        
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}	
    	
    	DatePicker datePicker = (DatePicker)view.findViewById(R.id.datePicker1);
    	
    	if(!answer.equals("")){
    		String[] parts = answer.split("/");
    		datePicker.init(Integer.parseInt(parts[2]),
    				Integer.parseInt(parts[1]), 
    				Integer.parseInt(parts[0]), onDateChangedListener);
    	}else{
    		Time today = new Time(Time.getCurrentTimezone());
    		today.setToNow();
    		datePicker.init(today.year, today.month, today.monthDay, onDateChangedListener);
    	}
    	
		return view;	
	}
	
	private DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
		
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			answer = dayOfMonth + "/" + monthOfYear + "/" + year;
		}
	};
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
				
		return new DateQuestion(name, label, "", fontSize, color);
	}

	@Override
	public Question clone() {
		return new DateQuestion(name, label, "", String.valueOf(fontSize), color);
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