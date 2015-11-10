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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SingleOptionQuestion extends Question implements Cloneable {

	private static final String ns = null;
	public List<Option> options;
	public String orientation = "horizontal";
	private View view;

	public SingleOptionQuestion(String questionName,String questionAnswer,
								List<Option> _options, String _orientation) {
		super(questionName, questionAnswer,"");
		// TODO Auto-generated constructor stub
		options = _options;
		space = options.size() +1;
		
		if(_orientation!=null && !_orientation.equals("")) orientation = _orientation;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, final SurveyActivity activityRef, final SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.singleoption_question,container, false);
    	TextView Textlabel = (TextView)view.findViewById(R.id.description);
    	Textlabel.setText(this.label);
    	Textlabel.setTextSize(fontSize);

    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	Textlabel.setTypeface(tf);
    	try{
    		Textlabel.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}
    	
    	RadioGroup optionsGroup = (RadioGroup)view.findViewById(R.id.optionslayout);
        
        for (int k = 0; k < this.options.size(); k++) {
        	Option option = this.options.get(k);
        	
        	RadioButton radioButton = new RadioButton(view.getContext());
        	optionsGroup.addView(radioButton);
        	
        	radioButton.setText(option.Label);
        	radioButton.setTag(option.Name);
        	radioButton.setTypeface(tf);
        	
        	if(!answer.equals("") && option.Name.equals(answer)){
            	radioButton.setChecked(true);
            }else{
            //	radioButton.setChecked(false);
            }
        }   
        
        if(orientation.equals("horizontal"))
        	optionsGroup.setOrientation(LinearLayout.HORIZONTAL);
        else 
        	optionsGroup.setOrientation(LinearLayout.VERTICAL);
        
        optionsGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	if(checkedId != -1){
            		answer = ((RadioButton)group.findViewById(checkedId)).getTag().toString();
            		
            		
            		//LIVE VALIDATIONS
            		String knowsRead = "";
            		for (ArrayList<Question> page : surveyAdapter.survey.pages) {
            			for (Question question : page) {
            				
                    		if(name.equals("C115")){
								if(question.name.equals("C12")){
									if(question.answer.equals("c12_1") /*Masculino*/ && !answer.equals("c115_1") /*diferente de ninguno*/ ){
										ShowAlertDialog(activityRef,
											activityRef.getText(R.string.Question_incongruence) + " " 
											+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
									break;
								}
                    		}
                    		else if(name.equals("C110")){
								if(question.name.equals("C18")){
									knowsRead = question.answer;
								}else if(question.name.equals("C19")){
									if(question.answer.equals("true") && knowsRead.equals("true") 
											&& (answer.equals("c110_1") /*Ninguno*/ || answer.equals("c110_2")/*Prescolar*/)) {
										ShowAlertDialog(activityRef,
											activityRef.getText(R.string.Question_incongruence) + " " 
											+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
									else if((!question.answer.equals("true") || !knowsRead.equals("true")) 
											&& !answer.equals("c110_1") /*Ninguno*/ && !answer.equals("c110_2")/*Prescolar*/) {
										ShowAlertDialog(activityRef,
											activityRef.getText(R.string.Question_incongruence) + " " 
											+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
									break;
								}
                    		}
						}
            		}
            	}
            }
        });
        

		return view;
	}
	
	private void ShowAlertDialog(Context context, String message){
	
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		        	   dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		
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

		return new SingleOptionQuestion(name,"", qOptions, orientation);
	}

	@Override
	public Question clone() {
		
		return new SingleOptionQuestion(name, "", options,orientation);
	}

	@Override
	public void setAnsweredListener(AnsweredListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		RadioGroup optionsGroup = (RadioGroup)view.findViewById(R.id.optionslayout);
		((ViewGroup)view).removeView(optionsGroup);
	}
}