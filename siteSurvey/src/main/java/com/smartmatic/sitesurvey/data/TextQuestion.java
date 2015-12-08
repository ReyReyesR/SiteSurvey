package com.smartmatic.sitesurvey.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
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

/**
 * <p>
 *     Text question object, is a particular case of Question object
 *     it is used when the type of answer is an open one.
 * </p>
 *
 */

public class TextQuestion extends Question implements Cloneable {

	public String inputType = "text"; //default
	public String hint = "Please enter your answer here"; //default
	private EditText editText;
	private TextWatcher watcher;
	float innerFontSize = 14;

	//Text Question Constructor
	public TextQuestion(String questionName, String questionLabel, String questionAnswer,
						String _fontSize, String _color, String _inputType, String _hint,
						String _repeat,  String _innerFontSize,int idSection,int idQuestion,
						int idAnswer,String idForm) {
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
	public View GetView(LayoutInflater inflater, ViewGroup container,
						final SurveyActivity activityRef, final SurveyAdapter surveyAdapter,
						int parentId){

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
									if((question.answer.equals("c115_1") /*Ninguno*/ &&
											Integer.parseInt(answer) > 0)
											|| (question.answer.equals("c115_2") /*1*/ &&
											Integer.parseInt(answer) > 1)
											|| (question.answer.equals("c115_3") /*2 a 3*/ &&
											Integer.parseInt(answer) > 3)){
										ShowAlertDialog(activityRef,
												activityRef.getText(R.string.Question_incongruence)
												+ " "+ label + " " +
														activityRef.getText(R.string.And) + " " +
														question.label);
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

	/**
	 * <p>
	 *     Creation of Text Question object from a JSON Object
	 * </p>
	 * @param json a JSONObject containing all the answers associated to a question.
	 * @param idForm an int containing the id of form.
	 * @param idSection an int containing the id of the section.
	 * @param idQuestion an int containing the id of the question.
	 * @return an object of the TextQuestion class.
	 * @throws JSONException if JSON object is null or empty.
	 */

	public static Question createFromJSON(JSONObject json, String idSection,
										  String idQuestion,String idForm) throws JSONException {

		String name = json.getString("name");
		String idAnswer = json.getString("PkForm");
		String inputType = "text";
		String hint = "Please enter your answer here";
		String label ="label";
		String fontSize ="14";
		String innerFontSize = "12";
		String color = "black";
		String repeat = "no";
		int i=Integer.parseInt(idSection);
		int j=Integer.parseInt(idQuestion);
		int k=Integer.parseInt(idAnswer);


		return new TextQuestion(name, label, "", fontSize, color, inputType, hint, repeat,
				innerFontSize, i, j,k,idForm);
	}

		@Override
	public Question clone() {
		TextQuestion tq= new TextQuestion(name, label, "", String.valueOf(fontSize), color,
				inputType, hint, dependency, String.valueOf(innerFontSize), idSection,
				idQuestion,idAnswer,idForm);
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