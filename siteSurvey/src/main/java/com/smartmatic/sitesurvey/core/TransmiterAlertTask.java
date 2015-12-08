package com.smartmatic.sitesurvey.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.smartmatic.sitesurvey.LoginActivity;
import com.smartmatic.sitesurvey.data.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;

public class TransmiterAlertTask extends AsyncTask<Object, Void, Void> {
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
	
	final  Handler myHandler = new Handler();
	public TransmiterAlertTask() {
        
    }
	
    protected Void doInBackground(Object... args) {
    	WebServiceClient.SendAlert((Context)args[0], new DataAlert(dateFormat.format(new Date()),
				"Border", LoginActivity.login,(String)args[1]));

    	return null;
    }


	protected void onPostExecute(Bitmap result) {
    }
}