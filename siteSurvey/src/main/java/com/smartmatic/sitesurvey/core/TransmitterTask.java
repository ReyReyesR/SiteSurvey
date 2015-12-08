package com.smartmatic.sitesurvey.core;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.FileReader;
import com.smartmatic.sitesurvey.FinishedListFragment;
import com.smartmatic.sitesurvey.ServiceHandler;
import com.smartmatic.sitesurvey.data.*;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * <p>
 *     This is an AsyncTask that handles the construction of the Answer object and the AnswerArray
 *     which is the JSON object containing a JSON array of JSON objects. After the final JSON object
 *     is created this function invokes the POST method to send the information o the corresponding
 *     service.
 * </p>
 *
 * @author Reynaldo
 */

public class TransmitterTask extends AsyncTask<Context, Void, Void> {

	String idForm = null;
	final  Handler myHandler = new Handler();
	public TransmitterTask() {
        
    }
	
    protected Void doInBackground(Context... args) {

        while (true){
        	try {
        	ArrayList<PollingStation> toDelete = new ArrayList<>();

        	for(PollingStation ps: FinishedListFragment.psArray){
        		Survey survey = ps.survey;


        		if(!ps.transmitted){
					Answer A= new Answer();

					AnswerArray answerArray=new AnswerArray();
					if(survey.pages!=null){
						for(ArrayList<Question> page: survey.pages){
							for(Question q: page){
								if(q.dependency.equals("") && q.answer!=null &&
										!q.answer.equals("")){
									JSONObject jsonAnswer = new JSONObject();
									if(q.getClass().toString().contains("MultiOption")){
										this.idForm=q.idForm;
										A.putFKSection(q.idSection);
										A.putFKQuestion(q.idQuestion);
										A.putFKAnswer(q.idAnswer);
										A.putIdPerson(4);
										A.putValue(q.answer);
										A.putCoordinatesX(10);
										A.putCoordinatesY(-66);

									} else {
										this.idForm=q.idForm;
										A.putFKSection(q.idSection);
										A.putFKQuestion(q.idQuestion);
										A.putFKAnswer(q.idAnswer);
										A.putIdPerson(4);
										A.putValue(q.answer);
										A.putCoordinatesX(10);
										A.putCoordinatesY(-66);
									}
									try {
										A.fillObject();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									try {
										jsonAnswer=A.returnObject();
									} catch (JSONException e) {
										e.printStackTrace();
									}
									answerArray.fillArray(jsonAnswer);

								}
							}
						}
					}
					//ADDING ALL THE DEPENDENCIES
					/*if(survey.dependentSurveys!=null){
						for(int k = 0; k< survey.dependentSurveys.size(); k++){
							Survey child = survey.dependentSurveys.get(k);
							for(ArrayList<Question> page: child.pages){
								for(Question q: page){
									if(q.answer!=null && !q.answer.equals(""))
										av.setProperty(0,new StringVector(q.name,q.answer));
								}
							}
						}
					}*/
					JSONArray answer=answerArray.getAnswerObject();
					ServiceHandler sh = new ServiceHandler();

					if(sh.postServiceCall((FileReader.getUrl(args[0],2)+idForm),answer))
						toDelete.add(ps);
        		}
			}
        	if(toDelete.size() > 0){
        		for(final PollingStation ps: toDelete){
        			myHandler.post(new Runnable() {

        				@Override
        				public void run() {
    	        			FinishedListFragment.SetAsTransmitted(ps);
        				}
        			});
        		}
        	}
			Thread.sleep(5000);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

}