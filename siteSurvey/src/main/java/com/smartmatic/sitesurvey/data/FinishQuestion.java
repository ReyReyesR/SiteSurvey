package com.smartmatic.sitesurvey.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;
import com.smartmatic.sitesurvey.core.PollingStationParser;
import com.smartmatic.sitesurvey.core.SurveyAdapterBuilder;
import com.smartmatic.sitesurvey.core.SurveyParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;

public class FinishQuestion extends Question implements Cloneable {
	
	public String backColor;
	int pId;
	//int depId;
	String depName;
		
	public FinishQuestion(int FkSection, int FkQuestion, int FkAnswer, String Value) {
		super("", "", "");
		space = 1;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, final SurveyActivity activityRef, final SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.finish_button,container, false);
    	Button button = (Button)view.findViewById(R.id.button1);
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	button.setTypeface(tf);
    	pId = activityRef.pId;
    	//depId = ((SurveyActivity)activityRef).depId;
    	depName = activityRef.depName;
    	
    	
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//If this is not a dependency
				if(depName == null){
					
					PollingStation ps = PendingListFragment.get(pId);
					if(ps!=null){
						FinishedListFragment.AddNew(ps);
						PendingListFragment.remove(ps);
						//postJSON JSONFile = new postJSON();
						//Checks for Build Version for Async tasks
						//if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
						//	JSONFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						//else
						//	JSONFile.execute();
					}
				}
				activityRef.finish();
			}
		} );
		return view;	
	}
	
	public static Question createFromJSON(JSONObject json)throws JSONException {
		
		return new FinishQuestion(0,0,0,null);
	}
	
	@Override
	public Question clone(){
		return new FinishQuestion(idSection,idQuestion,idAnswer,answer);
	}

	@Override
	public void setAnsweredListener(AnsweredListener l) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
/*	private class postJSON extends AsyncTask<Void, Void, Void> {
		private Context mContext;

		public postJSON(Context context){
			mContext = context;
		}

		protected void onPreExecute(){

		}

		protected Void doInBackground(Void... arg0) {
			//Url that connects to the service that provides the JSON
			String url = "";
			idForm="1089";
			url = FileReader.getUrl(mContext,2)+idForm;
			String response = null;
			ServiceHandler sh = new ServiceHandler();
			Answer A=new Answer();
			JSONArray json = null;
			A.putFKSection(1133);
			A.putFKQuestion(1227);
			A.putFKAnswer(1313);
			A.putIdPerson(4);
			A.putValue("Masculino");
			A.putCoordinatesX(10);
			A.putCoordinatesY(-66);
			try {
				A.fillObject();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				json=A.returnObject();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				response = sh.postServiceCall(url, json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("Response: ", "> " + response);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}
	}*/}

