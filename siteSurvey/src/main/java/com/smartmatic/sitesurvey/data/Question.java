package com.smartmatic.sitesurvey.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 *     This is the base class for all the different Question classes.
 * </p>
 */

public abstract class Question implements Cloneable{

	public String name;
	public String label;
	public String answer;
	public float fontSize = 16;
	public String color;
	public String dependency = "";
	public int repeated = 0;
	public int page = 0;
	public AnsweredListener listener;
	public int idSection;
	public int idQuestion;
	public int idAnswer;
	public String idForm;
	
	public interface AnsweredListener 
    {
        void onAnswered(String answer);
    }
	
	///Vertical slots used by the control
	public int space = 2;
	
	public Question(String questionName, String questionLabel, String questionAnswer) {
		name = questionName;
		label= questionLabel;
		answer = questionAnswer;
	}
	
	
	public Question(String questionName, String questionLabel, String questionAnswer, String _fontSize, String _color) {
		this(questionName, questionLabel, questionAnswer);
		
		if (_fontSize != null && _fontSize != "")
			fontSize = Float.parseFloat(_fontSize);
		
		color = _color;
	}
	
	public abstract void setAnsweredListener(AnsweredListener l);
	
	public abstract View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId);
	
	public abstract Question clone();
	
	public static Question createFromJSON(JSONObject json, String idSection, String idQuestion) throws JSONException {
		return null;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (!(other instanceof Question))return false;
	    Question otherQuestion = (Question)other;
		return otherQuestion.name == this.name;

	}


	public abstract void onDestroy();
}

