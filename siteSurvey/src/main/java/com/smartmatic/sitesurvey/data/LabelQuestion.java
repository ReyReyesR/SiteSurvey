package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

public class LabelQuestion extends Question implements Cloneable {
	
	public String backColor;
		
	public LabelQuestion(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color, String _backColor) {
		super(questionName, questionLabel, questionAnswer,  _fontSize, _color);	
		backColor = _backColor;
		space = 1;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.label_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);

    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	
    	Window window= activityRef.getWindow();
    	
    	Point size = new Point();
    	window.getWindowManager().getDefaultDisplay().getSize(size);
    	label.setWidth(size.x);
    	try{
    		label.setTextColor(Color.parseColor("#696969"));
    	} catch (Exception e) {}
    	
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		String backColor = parser.getAttributeValue(null, "background-color");
		
		return new LabelQuestion(name, label, "", fontSize, color, backColor);
	}
	
	@Override
	public Question clone(){
		return new LabelQuestion(name, label, "", String.valueOf(fontSize), color, backColor);
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
