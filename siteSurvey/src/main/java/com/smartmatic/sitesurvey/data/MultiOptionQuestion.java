package com.smartmatic.sitesurvey.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * <p>
 *     This is the MultipleOptionQuestion class is a specific type of extension of the Question
 *     class, this type of question allows one or more answers.
 * </p>
 */

public class MultiOptionQuestion extends Question implements Cloneable {

	private static final String ns = null;
	public List<Option> options;
	public String orientation = "horizontal";

	public MultiOptionQuestion(String questionName, String questionLabel, String questionAnswer,
							   String _fontSize, String _color, List<Option> _options, String _orientation,int idSection,int idQuestion,String idForm) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);
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
			int id=Integer.parseInt(option.IdAnswer);
			radioButton.setId(id);
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
					idAnswer=buttonView.getId();
					if(!isChecked){
						if(answer.contains(value)){
							answer = answer.replace(value + "/", "");

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

	/**
	 * <p>
	 *     This function constructs a new MultipleOptionQuestion using a JsonObject input.
	 * </p>
	 * *
	 * @param json a JSONObject containing all the answers associated to a question.
	 * @param idForm an int containing the id of form.
	 * @param idSection an int containing the id of the section.
	 * @param idQuestion an int containing the id of the question.
	 * @return SingleOptionQuestion This is  new object of the MultipleOptionQuestion which extends
	 * from the Question class, this object contains the Form,Section,Question and possible Answers
	 * id, including the value for each answer.
	 * @throws JSONException
	 */

	public static Question createFromJSON(JSONObject json, String idSection, String idQuestion,String idForm) throws JSONException {

		List<Option> qOptions = new ArrayList<Option>();
		String name = json.getString("name");
		String orientation = "horizontal";
		String label = ("label");
		String fontSize = ("14");
		String color = ("bl");
		int i=Integer.parseInt(idSection);
		int j=Integer.parseInt(idQuestion);


		if (json.has("Answers")){
			JSONArray answers = json.getJSONArray("Answers");


			for (int k = 0; k < answers.length(); k++)
			{
				JSONObject a = answers.getJSONObject(k);
				String idAnswer= a.getString("PkAnswer");
				String siName = a.getString("Name");
				String siLabel = a.getString("Number");
				qOptions.add(new Option(siName, siLabel,idForm,idAnswer,j,i));

			}
		}
		return new MultiOptionQuestion(name, label, "", fontSize, color, qOptions,orientation,i,j,idForm);
	}

	@Override
	public Question clone() {

		return new MultiOptionQuestion(name, label, "", String.valueOf(fontSize), color, options,orientation, idSection, idQuestion, idForm);
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