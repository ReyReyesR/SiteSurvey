package com.smartmatic.sitesurvey.data;

import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

public class StartDateQuestion extends TextQuestion {

	public StartDateQuestion(String questionName, String questionLabel,
			String questionAnswer, String _fontSize, String _color,
			String _inputType, String _hint, String _repeat, String _innerFontSize) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color, _inputType, _hint, _repeat, _innerFontSize);
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
		
		return new StartDateQuestion(name, label, "", fontSize, color, inputType, hint, repeat, innerFontSize);
	}
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container,
			SurveyActivity activityRef, SurveyAdapter surveyAdapter,
			int parentId) {

		if(answer==null || answer.equals(""))
			answer = activityRef.startedDate.toString();
		return super.GetView(inflater, container, activityRef, surveyAdapter, parentId);
	}
	
	@Override
	public Question clone() {
		StartDateQuestion aq= new StartDateQuestion(name, label, "", 
				String.valueOf(fontSize), color, inputType, hint, dependency, String.valueOf(innerFontSize));

		return aq;
	}
}
