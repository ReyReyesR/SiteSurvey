package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.google.android.gms.ads.mediation.NetworkExtras;
import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.SurveyActivity;
import com.smartmatic.sitesurvey.SurveyAdapter;
import com.smartmatic.sitesurvey.core.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.*;

public class NetworkQuestion extends Question implements Cloneable {

	public String orientation = "vertical";
	public String nameLabel = "";
	public String connLabel = "";
	private TextView textName;
	private TextView textConn;
	
	public NetworkQuestion(String questionName, String questionLabel, String questionAnswer, 
			String _fontSize, String _color , String _orientation, String _nameLabel, String _connLabel) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);

		if(_orientation!=null && !_orientation.equals("")) orientation = _orientation;
		
		space = (orientation.equals("horizontal")) ? 2 : 3;
		nameLabel = _nameLabel;
		connLabel = _connLabel;
	}
	
	
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.network_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);
    	
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}		

    	//SET LABELS
    	TextView textlatlabel = (TextView)view.findViewById(R.id.name_label);
    	textlatlabel.setText(nameLabel);
    	textlatlabel.setTypeface(tf);
    	
    	TextView textLonlabel = (TextView)view.findViewById(R.id.connectivity_label);
    	textLonlabel.setText(connLabel);
    	textLonlabel.setTypeface(tf);
    	
    	//GET GPS POSITION
    	textName = (TextView)view.findViewById(R.id.name);
    	textConn = (TextView)view.findViewById(R.id.connectivity);
    	textName.setTypeface(tf);
    	textConn.setTypeface(tf);
    	
    	
    	
    	//SET ORIENTATION
    	LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
    	if(orientation.equals("horizontal"))
    		layout.setOrientation(LinearLayout.HORIZONTAL);
        else 
        	layout.setOrientation(LinearLayout.VERTICAL);
    	
    	//SET ANSWER
    	if(!answer.equals("")){
    		String[] parts = answer.split("/");
    		textName.setText(parts[0]);
    		textConn.setText(parts[1]);
    	}else{
    		TelephonyManager telephonyManager = (TelephonyManager)activityRef.getSystemService(Context.TELEPHONY_SERVICE);
    		textName.setText(telephonyManager.getSimOperatorName());
    		int phoneType = telephonyManager.getPhoneType();
    		textConn.setText(phoneType ==0? "None" : phoneType==1? "GSM": phoneType==2? "CDMA": "SIP");
    		
    		telephonyManager.getDataState(); 
    		
    	}
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		String orientation = parser.getAttributeValue(null, "orientation");
		String nameLabel = parser.getAttributeValue(null, "name-label");
		String connLabel = parser.getAttributeValue(null, "connectivity-label");
		
		return new NetworkQuestion(name, label, "", fontSize, color, orientation, nameLabel, connLabel);
	}

	@Override
	public Question clone() {
		return new NetworkQuestion(name, label, "", String.valueOf(fontSize), color, orientation, nameLabel, connLabel);
		
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
