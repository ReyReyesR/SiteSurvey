package com.smartmatic.sitesurvey.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MultiOptionQuestion extends Question implements Cloneable {

	private static final String ns = null;
	public List<Option> options;
	public String orientation = "horizontal";

	public MultiOptionQuestion(String questionName,String questionAnswer,
			 List<Option> _options, String _orientation) {
		super(questionName, questionAnswer, "");
		// TODO Auto-generated constructor stub
		options = _options;
		space = options.size() +1;

		if(_orientation!= null && !_orientation.equals("")) orientation = _orientation;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.multioptions_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}
    	
    	LinearLayout optionsLayout = (LinearLayout)view.findViewById(R.id.optionslayout);
        for (int k = 0; k < options.size(); k++) {
        	Option option = options.get(k);
        	
        	CheckBox radioButton = new CheckBox(view.getContext());
        	radioButton.setText(option.Label);
        	radioButton.setTag(option.Name);
        	radioButton.setTypeface(tf);
        	
        	if(!answer.equals("") && answer.contains(String.valueOf(k))){
            	radioButton.setChecked(true);
            }
        	else{
        		radioButton.setChecked(false);
        	}
        	
        	radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					String value = buttonView.getTag().toString() +"-"+buttonView.getText() ;
					if(!isChecked){
						if(answer.contains(value)){
							answer = answer.replace(value+"/", "");
						}
					}else{
						answer += value+"/";
					}
				}
			});
        	
        	optionsLayout.addView(radioButton);
        }
        
        if(orientation.equals("horizontal"))
        	optionsLayout.setOrientation(LinearLayout.HORIZONTAL);
        else 
        	optionsLayout.setOrientation(LinearLayout.VERTICAL);
    	
		return view;	
	}
	
	public static Question createFromJSON(JSONObject json) throws JSONException {
		
		List<Option> qOptions = new ArrayList<Option>();
		String name = json.getString("name");
		String orientation = "horizontal";

		if (json.has("Answers")){
			JSONArray answers = json.getJSONArray("Answers");


			for (int k = 0; k < answers.length(); k++)
			{
				JSONObject a = answers.getJSONObject(k);
				String siName = a.getString("Name");
				String siLabel = a.getString("Number");
				qOptions.add(new Option(siName, siLabel));



			}
		}
		return new MultiOptionQuestion(name,"", qOptions, orientation);
	}

	@Override
	public Question clone() {

		return new MultiOptionQuestion(name, "", options, orientation);
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