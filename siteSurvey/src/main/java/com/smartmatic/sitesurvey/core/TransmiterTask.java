package com.smartmatic.sitesurvey.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.kobjects.util.Util;

import com.smartmatic.sitesurvey.FinishedListFragment;
import com.smartmatic.sitesurvey.LoginActivity;
import com.smartmatic.sitesurvey.PendingListFragment;
import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.data.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

public class TransmiterTask extends AsyncTask<Context, Void, Void> {

	final  Handler myHandler = new Handler();
	public TransmiterTask() {
        
    }
	
    protected Void doInBackground(Context... args) {
    	
		//WebServiceClient.SendDummySurvey();
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
		
        while (true){
        	try {
        	ArrayList<PollingStation> toDelete = new ArrayList<PollingStation>();
        	
        	for(PollingStation ps: FinishedListFragment.psArray){
        		//Survey survey = SurveyAdapterBuilder.surveys.get(ps.id);
        		Survey survey = ps.survey;
        		        		
        		if(ps.transmitted == false){
					AnswersVector av = new AnswersVector();
					if(survey.pages!=null){
						for(ArrayList<Question> page: survey.pages){
							for(Question q: page){
								if(q.dependency.equals("") && q.answer!=null && !q.answer.equals("")){
									
									if(q.getClass().toString().contains("MultiOption")){
										String answers[] = q.answer.split("/");
										for(String answer: answers){
											String values[] = answer.split("-");
											av.setProperty(0,new StringVector(values[0], values[1]));
										}
									}
									else
										av.setProperty(0,new StringVector(q.name,q.answer));
								}
							}
						}
					}
					//ADDING ALL THE DEPENDENCIES
					if(survey.dependentSurveys!=null){
						for(int k = 0; k< survey.dependentSurveys.size(); k++){
							Survey child = survey.dependentSurveys.get(k);
							for(ArrayList<Question> page: child.pages){
								for(Question q: page){
									if(q.answer!=null && !q.answer.equals(""))
										av.setProperty(0,new StringVector(q.name,q.answer));
								}
							}
						}
					}
					//String prueba = ps.survey.startDate;
					if(WebServiceClient.SendSurvey(args[0], new DataSurvey(LoginActivity.login,ps.survey.startDate, format.format(new Date()), survey.parentId, av)) == true){
						toDelete.add(ps);						
						
					}
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
        	
			Thread.sleep(10000);
		
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }


	protected void onPostExecute(Bitmap result) {
    }
}