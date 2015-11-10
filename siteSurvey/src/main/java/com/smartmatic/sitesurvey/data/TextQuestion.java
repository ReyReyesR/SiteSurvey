package com.smartmatic.sitesurvey.data;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class TextQuestion extends Question implements Cloneable {

	public String inputType = "text"; //default
	public String hint = "Please enter your answer here"; //default
	private EditText editText;
	private TextWatcher watcher;
	float innerFontSize = 14;
		
	public TextQuestion(String questionName, String questionLabel, String questionAnswer, 
			String _fontSize, String _color, String _inputType, String _hint, String _repeat,  String _innerFontSize) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);

		if (_innerFontSize != null && _innerFontSize != "")
			innerFontSize = Float.parseFloat(_innerFontSize);
		
		inputType = _inputType;
		hint = _hint;
		if(_repeat!=null) dependency = _repeat; 
		
		watcher = new TextWatcher(){
            public void afterTextChanged(Editable s) {
            	answer = s.toString();
            	if(listener!=null) listener.onAnswered(answer);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        };
    			
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, final SurveyActivity activityRef, final SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.text_question,container, false);
    	TextView textLabel = (TextView)view.findViewById(R.id.description);
    	textLabel.setText(this.label);
    	textLabel.setTextSize(fontSize);
    	
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	textLabel.setTypeface(tf);
        
    	try{
    		textLabel.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}		

    	//editText = (EditText)view.findViewById(R.id.editText);
    	//((ViewGroup)view).removeView(editText);
    	   	
    	editText = new EditText(view.getContext());
    	
    	if(inputType == null){
    		editText.setInputType(InputType.TYPE_NULL);
    	}
    	else if(inputType.equals("text")){
    		editText.setInputType(InputType.TYPE_CLASS_TEXT);
    	}
    	else if(inputType.equals("numeric")) {
    		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    	}
    	
    	editText.setHint(hint);
    	
    	watcher = new TextWatcher(){
            public void afterTextChanged(Editable s) {
            	answer = s.toString();
            	if(listener!=null) listener.onAnswered(answer);
            	
            	//LIVE VALIDATIONS
        		for (ArrayList<Question> page : surveyAdapter.survey.pages) {
        			for (Question question : page) {
        				
                		if(name.equals("C116")){
							if(question.name.equals("C115")){
								if(!answer.equals("")){
									if((question.answer.equals("c115_1") /*Ninguno*/ && Integer.parseInt(answer) > 0)
										|| (question.answer.equals("c115_2") /*1*/ && Integer.parseInt(answer) > 1)
										|| (question.answer.equals("c115_3") /*2 a 3*/ && Integer.parseInt(answer) > 3)){
										ShowAlertDialog(activityRef,
											activityRef.getText(R.string.Question_incongruence) + " " 
											+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
								}
								break;
							}
                		}
					}
        		}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        };
    	

    	editText.removeTextChangedListener(watcher);
    	editText.setText(answer);
    	editText.addTextChangedListener(watcher);
    	
    	editText.setTypeface(tf);
    	editText.setTextSize(innerFontSize);
    	
    	((ViewGroup)view).addView(editText);
    	
    	
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
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String inputType = parser.getAttributeValue(null, "inputType");
		String hint = parser.getAttributeValue(null, "hint");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String innerFontSize = parser.getAttributeValue(null, "inner-font-size");
		String color = parser.getAttributeValue(null, "font-color");
		String repeat = parser.getAttributeValue(null, "repeat");
		
		return new TextQuestion(name, label, "", fontSize, color, inputType, hint, repeat, innerFontSize);
	}

	@Override
	public Question clone() {
		TextQuestion tq= new TextQuestion(name, label, "", String.valueOf(fontSize), color, inputType, hint, dependency, String.valueOf(innerFontSize));
		tq.page = page;
		return tq;
	}

	@Override
	public void setAnsweredListener(AnsweredListener l) {
		listener = l;
	}

	@Override
	public void onDestroy() {
		editText.removeTextChangedListener(watcher);
	}
}
