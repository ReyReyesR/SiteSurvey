package com.smartmatic.sitesurvey;

import com.smartmatic.sitesurvey.core.LocationUtil;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewDialogFragment extends DialogFragment {
	
	public OnResultListener resultListener;
	private LocationManager locationManager;
	private Location currentBestLocation;
	private View view;
	
	public interface OnResultListener 
    {
        void onResult(String title, String address, double longitude, double latitude);
    }
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_newps, null);
        builder.setView(view);
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        ((EditText)view.findViewById(R.id.title)).setTypeface(tf);
        ((EditText)view.findViewById(R.id.address)).setTypeface(tf);
        
        builder.setPositiveButton(getText(R.string.add), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   if(!((EditText)view.findViewById(R.id.title)).getText().toString().equals("")){
                		   if(resultListener!=null){
                			   locationManager = null;
       							resultListener.onResult(
       								((EditText)view.findViewById(R.id.title)).getText().toString(),
       								((EditText)view.findViewById(R.id.address)).getText().toString(),
       								0,
       								0);
                		   }
                	   }
                	   else{
                		   Toast.makeText(getActivity(), getActivity().getText(R.string.canceled), Toast.LENGTH_SHORT).show();
                	   }
                   }
               })
               .setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   Toast.makeText(getActivity(), getActivity().getText(R.string.canceled), Toast.LENGTH_SHORT).show();
                   }
               });
        
        if(locationManager==null){
	    	// Acquire a reference to the system Location Manager
	        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
	
	        // Define a listener that responds to location updates
	        LocationListener locationListener = new LocationListener() {
	            public void onLocationChanged(Location location) {
	            	if(LocationUtil.isBetterLocation(location, currentBestLocation)){
		            	// Called when a new location is found by the network location provider.
		            	((TextView)view.findViewById(R.id.lat)).setText(String.valueOf(location.getLatitude()));
		            	((TextView)view.findViewById(R.id.lon)).setText(String.valueOf(location.getLongitude()));
		            	
		            	// Remove the listener you previously added
		            	locationManager.removeUpdates(this);
	            	}
	            }
	            public void onProviderEnabled(String provider) {}
	
	            public void onProviderDisabled(String provider) {}
	
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {}
	          };
	          

	          // Register the listener with the Location Manager to receive location updates
	          locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	          
	          Criteria criteria = new Criteria();
	          String provider = locationManager.getBestProvider(criteria, false);
	          currentBestLocation = locationManager.getLastKnownLocation(provider);
    	}
        
        
        // Create the AlertDialog object and return it
        return  builder.create();
    }
    
    public void SetOnResultListener(OnResultListener l){
    	resultListener = l;
    }
}