package com.smartmatic.sitesurvey.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * <p>
 *     This is the SingleOptionQuestion class is a specific type of extension of the Question class,
 *     this type of question only allows one and only one answer.
 * </p>
 */

public class SingleOptionQuestion extends Question implements Cloneable {

	public List<Option> options;
	public String orientation = "horizontal";
	private View view;

	public SingleOptionQuestion(String questionName, String questionLabel, String questionAnswer,
								String _fontSize, String _color, List<Option> _options, String _orientation,int idSection,int idQuestion,String idForm) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
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

			int idAnswer=Integer.parseInt(option.IdAnswer);
			int idQuestion=option.IdQuestion;
			int idSection=option.IdSection;
			int idForm=Integer.parseInt(option.IdForm);

			radioButton.setText(option.Label);
			radioButton.setTag(option.Name);

			radioButton.setId(idAnswer);
			radioButton.setNextFocusDownId(idQuestion);
			radioButton.setNextFocusUpId(idSection);
			radioButton.setNextFocusLeftId(idForm);
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
					answer 		= group.findViewById(checkedId).getTag().toString();
					idAnswer 	= group.findViewById(checkedId).getId();
					idQuestion	= group.findViewById(checkedId).getNextFocusDownId();
					idSection	= group.findViewById(checkedId).getNextFocusUpId();
					idForm		= Integer.toString(group.findViewById(checkedId).getNextFocusLeftId());



					//LIVE VALIDATIONS
					/*String knowsRead = "";
					for (ArrayList<Question> page : surveyAdapter.survey.pages) {
						for (Question question : page) {

							if(name.equals("C115")){
								if(question.name.equals("C12")){
									if(question.answer.equals("c12_1") *//*Masculino*//* && !answer.equals("c115_1") *//*diferente de ninguno*//* ){
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
											&& (answer.equals("c110_1") *//*Ninguno*//* || answer.equals("c110_2")*//*Prescolar*//*)) {
										ShowAlertDialog(activityRef,
												activityRef.getText(R.string.Question_incongruence) + " "
														+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
									else if((!question.answer.equals("true") || !knowsRead.equals("true"))
											&& !answer.equals("c110_1") *//*Ninguno*//* && !answer.equals("c110_2")*//*Prescolar*//*) {
										ShowAlertDialog(activityRef,
												activityRef.getText(R.string.Question_incongruence) + " "
														+ label + " " + activityRef.getText(R.string.And) + " " + question.label);
									}
									break;
								}
							}
						}
					}*/
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
		alert.show();List<Option> qOptions = new ArrayList<Option>();


	}

	/**
	 * <p>
	 *     This function constructs a new SingleOptionQuestion using a JsonObject input.
	 * </p>
	 * *
	 * @param json a JSONObject containing all the answers associated to a question.
	 * @param idForm an int containing the id of form.
	 * @param idSection an int containing the id of the section.
	 * @param idQuestion an int cntaining the id of the question.
	 * @return SingleOptionQuestion This is  new object of the SingleOptionQuestion which extends
	 * from the Question class, this object contains the Form,Section,Question and possible Answers
	 * id, including the value for each answer.
	 * @throws JSONException
	 */

	public static Question createFromJSON(JSONObject json, String idForm, String idSection, String idQuestion) throws JSONException {

		List<Option> qOptions = new ArrayList<Option>();
		String label = json.getString("Title");
		String fontSize = ("14");
		String color = ("black");
		String name = json.getString("Title");
		String orientation = "horizontal";
		int j=Integer.parseInt(idSection);
		int i=Integer.parseInt(idQuestion);

		if (json.has("Answers")){
			JSONArray answers = json.getJSONArray("Answers");

			for (int k = 0; k < answers.length(); k++)
			{
				JSONObject a = answers.getJSONObject(k);
				String idAnswer= a.getString("PkAnswer");
				String siName = a.getString("Name");
				String siLabel = a.getString("Name");

				qOptions.add(new Option(siName, siLabel,idForm,idAnswer,i,j));
			}
		}
		return new SingleOptionQuestion(name, label, "", fontSize, color, qOptions,orientation,i,j,idForm);
	}

	@Override
	public Question clone() {

		return new SingleOptionQuestion(name, label, "", String.valueOf(fontSize), color, options,orientation,idSection,idQuestion,idForm);
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