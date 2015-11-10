package com.smartmatic.sitesurvey.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerQuestion extends Question implements Cloneable {

	private static final String ns = null;
	public List<Option> options;
	
	public SpinnerQuestion(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color, List<Option> _options) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
		// TODO Auto-generated constructor stub
		options = _options;
		space = 2;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.spinner_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);

    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}
    	
    	Spinner spinner = (Spinner)view.findViewById(R.id.spinner1);
        ArrayAdapter<Option> adapter = new ArrayAdapter<Option>(view.getContext(),android.R.layout.simple_spinner_item, options);
        spinner.setAdapter(adapter);
        
        if(!answer.equals("")){
    		for(int i=0; i<options.size(); i++){
    			if(options.get(i).Name == answer){
    	        	spinner.setSelection(i);
    	        	break;
    			}
    		}
        }else{
        	spinner.setSelection(0);
        }
        
        if(spinner.getOnItemSelectedListener() == null){
	        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					answer = (options.get(position).Name);
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					answer = ("");
				}
	        	
	        });
        }
    	
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		List<Option> qOptions = new ArrayList<Option>();
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		
		try{
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, ns, "options");
			while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String tagName = parser.getName();
	            // Starts by looking for the entry tag
	            if (tagName.equals("option")) {
	            	 String siName = parser.getAttributeValue(null, "name");
	            	 String siLabel = parser.getAttributeValue(null, "label");
	            	 qOptions.add(new Option(siName, siLabel));
	            } 
	            parser.nextTag();
	        }
			parser.require(XmlPullParser.END_TAG, ns, "options");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		return new SpinnerQuestion(name, label, "", fontSize, color, qOptions);
	}

	@Override
	public Question clone() {

		return new SpinnerQuestion(name, label, "", String.valueOf(fontSize), color, options);
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