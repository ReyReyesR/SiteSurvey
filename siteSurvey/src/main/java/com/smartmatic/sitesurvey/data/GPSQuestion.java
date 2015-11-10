package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

import com.smartmatic.sitesurvey.*;
import com.smartmatic.sitesurvey.core.LocationUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class GPSQuestion extends Question implements Cloneable {

	public String orientation = "vertical";
	public String latLabel = "";
	public String lonLabel = "";
	private LocationManager locationManager;
	private TextView textLat;
	private TextView textLon;
	private Location currentBestLocation;
	
	public GPSQuestion(String questionName, String questionLabel, String questionAnswer, 
			String _fontSize, String _color , String _orientation, String _latLabel, String _lonLabel) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color);

		if(_orientation!=null && !_orientation.equals("")) orientation = _orientation;
		
		space = (orientation.equals("horizontal")) ? 2 : 3;
		latLabel = _latLabel;
		lonLabel = _lonLabel;
	}
	
	@Override
	public View GetView(LayoutInflater inflater, ViewGroup container, SurveyActivity activityRef, SurveyAdapter surveyAdapter, int parentId){
		
		View view =  inflater.inflate(R.layout.gps_question,container, false);
    	TextView label = (TextView)view.findViewById(R.id.description);
    	label.setText(this.label);
    	label.setTextSize(fontSize);
    	
    	Typeface tf = Typeface.createFromAsset(activityRef.getAssets(), "fonts/OpenSans-Regular.ttf");
    	label.setTypeface(tf);
    	
    	try{
    		label.setTextColor(Color.parseColor(color));
    	} catch (Exception e) {}		

    	//SET LABELS
    	TextView textlatlabel = (TextView)view.findViewById(R.id.lattext);
    	textlatlabel.setText(latLabel);
    	textlatlabel.setTypeface(tf);
    	
    	TextView textLonlabel = (TextView)view.findViewById(R.id.lontext);
    	textLonlabel.setText(lonLabel);
    	textLonlabel.setTypeface(tf);
    	
    	//GET GPS POSITION
    	textLat = (TextView)view.findViewById(R.id.latitude);
    	textLon = (TextView)view.findViewById(R.id.longitude);
    	textLat.setTypeface(tf);
    	textLon.setTypeface(tf);
    	
    	if(locationManager==null){
	    	// Acquire a reference to the system Location Manager
	        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
	
	        // Define a listener that responds to location updates
	        LocationListener locationListener = new LocationListener() {
	            public void onLocationChanged(Location location) {
	            	
	            	if(LocationUtil.isBetterLocation(location, currentBestLocation)){
		            	// Called when a new location is found by the network location provider.
		            	textLat.setText(String.valueOf(location.getLatitude()));
		            	textLon.setText(String.valueOf(location.getLongitude()));
		            	answer = String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude());
		            	
		            	// Remove the listener you previously added
		            	locationManager.removeUpdates(this);
	            	}
	            }
	            public void onProviderEnabled(String provider) {}
	
	            public void onProviderDisabled(String provider) {}
	
				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}
	          };
	          

	          // Register the listener with the Location Manager to receive location updates
	          locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	          
	          
	          Criteria criteria = new Criteria();
	          String provider = locationManager.getBestProvider(criteria, false);
	          currentBestLocation = locationManager.getLastKnownLocation(provider);
    	}

    	
    	//SET ORIENTATION
    	LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
    	if(orientation.equals("horizontal"))
    		layout.setOrientation(LinearLayout.HORIZONTAL);
        else 
        	layout.setOrientation(LinearLayout.VERTICAL);
    	
    	//SET ANSWER
    	if(!answer.equals("")){
    		String[] parts = answer.split("/");
    		textLat.setText(parts[0]);
    		textLon.setText(parts[1]);
    	}
		return view;	
	}
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String color = parser.getAttributeValue(null, "font-color");
		String orientation = parser.getAttributeValue(null, "orientation");
		String latLabel = parser.getAttributeValue(null, "lat-label");
		String lonLabel = parser.getAttributeValue(null, "lon-label");
		
		return new GPSQuestion(name, label, "", fontSize, color, orientation, latLabel, lonLabel);
	}

	@Override
	public Question clone() {
		return new GPSQuestion(name, label, "", String.valueOf(fontSize), color, orientation, latLabel, lonLabel);
		
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
