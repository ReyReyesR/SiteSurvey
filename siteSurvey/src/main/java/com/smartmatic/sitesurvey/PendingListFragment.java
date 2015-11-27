package com.smartmatic.sitesurvey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.smartmatic.sitesurvey.core.DownloadImageTask;
import com.smartmatic.sitesurvey.core.LocationUtil;
import com.smartmatic.sitesurvey.data.PollingStation;

import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PendingListFragment extends ListFragment{
	
	private static PSAdapter psAdapter = null;
	private static Boolean manualLocation = false; // debe ser el check de preferences.xml
	private static double lon = 10.488558; // latitud de archivo de config
	private static double lat = -66.93399; // longiud de archivo de config
	private static 	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

	public static String startDate;

	// Colombia lat = 4.647758; lon = -74.101735;
	// Venezuela lat = 10.488558, lon = -66.933990
	
	public static ArrayList<PollingStation> psArray = new ArrayList<PollingStation>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
				
		psAdapter = new PSAdapter(getActivity());
		setListAdapter(psAdapter);
	}
		
	public static void setData(ArrayList<PollingStation> _psArray){
		if(psArray.size() == 0 && _psArray!=null)
			psArray = _psArray;
		if(psAdapter!=null)  psAdapter.notifyDataSetChanged();
	}
	
	public static void addNew(PollingStation ps){
		ps.id = PendingListFragment.psArray.size();
		psArray.add(ps);
		if(psAdapter!=null)  psAdapter.notifyDataSetChanged();
	}
	public static void remove(PollingStation ps){
		psArray.remove(ps);

		if(psAdapter!=null)  psAdapter.notifyDataSetChanged();
	}
	public static PollingStation get(int psId){
		for(PollingStation ps : psArray){
			if(ps.id == psId)
				return ps;
		}
		return null;
	}
	public static boolean contains(PollingStation ps){
		return psArray.contains(ps);
	}

	public static class PSAdapter extends BaseAdapter{
	
		private static Context context;
		
		PSAdapter(Context c){
			context = c;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return psArray.size();
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return psArray.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return psArray.get(position).id;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub}
			
			View view = null;
			if(convertView == null){
				//Make a new view
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				view = inflater.inflate(R.layout.row, null);
			}
			else{
				//Use convertView if it is available
				view = convertView;
			}
			
			view.setOnClickListener(new CustomOnClickListener(position) {
				
				@Override
				public void onClick(View v) {
					startSurvey(position);
				}
			});
			
			ImageButton mapButton = (ImageButton)view.findViewById(R.id.mapButton);
			
			if(psArray.get(position).image != null){
				mapButton.setImageBitmap(psArray.get(position).image);
			}
			else{
			// show The Image
			new DownloadImageTask(mapButton,psArray.get(position))
			            .execute("http://maps.google.com/maps/api/staticmap?center="
			            		+ psArray.get(position).lat + ","
								+ psArray.get(position).lon +"&zoom=15"
								+ "&size=50x50"
								+ "&sensor=true_or_false"
								+ "&markers=size:small%7color:red%7C" + psArray.get(position).lat + "," + psArray.get(position).lon);
			}
			
			mapButton.setOnClickListener(new CustomOnClickListener(position) {
				
				@Override
				public void onClick(View v) {
					startDrive(position);
				}
			});
			
			//ImageView img = (ImageView) view.findViewById(R.id.image);
			//img.setImageDrawable(context.getResources().getDrawable(psArray.get(position).image));
			
			TextView tTitle = (TextView) view.findViewById(R.id.title);
			tTitle.setText(psArray.get(position).title);
			
			TextView tDescription = (TextView) view.findViewById(R.id.description);
			tDescription.setText(psArray.get(position).description);
			
			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
			tTitle.setTypeface(tf);
			tDescription.setTypeface(tf);
			
			return view;
		}

		public static void startSurvey(final int position) {
			final Toast t= Toast.makeText(context, context.getString(R.string.PSAdapter_validating), Toast.LENGTH_LONG);
			t.show();

			final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, false);

	        final Location currentLocation = locationManager.getLastKnownLocation(provider);

	        LocationListener locationListener = new LocationListener() {
	            public void onLocationChanged(Location location) {

            		int ps_id= psArray.get(position).id;
        			Location placeLocation = new Location("");
        			placeLocation.setLatitude(psArray.get(position).lat);
        			placeLocation.setLongitude(psArray.get(position).lon);
        			float distance = 0;
        			// Si fue modificada variables lat y lon omitir
        			if ((PreferenceManager.getDefaultSharedPreferences(context).getBoolean("manualLocation_preference", false))){

        				Location manualLocation = new Location("");
        				manualLocation.setLatitude(lat);
        				manualLocation.setLongitude(lon);
            			if(LocationUtil.isBetterLocation(placeLocation, manualLocation)){

							distance= location.distanceTo(placeLocation);
    	            	}else{

    	            		distance= manualLocation.distanceTo(placeLocation);
    	            	}            			
        			}
        			else {	        				        			
	        			if(LocationUtil.isBetterLocation(location, currentLocation)){
	        				distance= location.distanceTo(placeLocation);
		            	}else{
		            		distance = currentLocation.distanceTo(placeLocation);

		            	}
        			}
        			if(distance < 100){		// tengo posicion(encuesta) y valido la posicion(lat y lon)
        				t.cancel(); 
        				Intent i = new Intent(context, SurveyActivity.class);
        				i.putExtra("PLACE_ID", ps_id); 
        				startDate =  format.format(new Date());
        				context.startActivity(i);
        			}
        			else{
        				Toast.makeText(context, context.getString(R.string.PSAdapter_notClose), Toast.LENGTH_LONG).show();
        			}
            		
	            	// Remove the listener you previously added
	            	locationManager.removeUpdates(this);
	            }
	            public void onProviderEnabled(String provider) {}	
	            public void onProviderDisabled(String provider) {}
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {}
	          };
	          

	          // Register the listener with the Location Manager to receive location updates
	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
		
		public static void startDrive(int position){
			// TODO Auto-generated method stub
			String uri = String.format(Locale.ENGLISH, 
					//"geo:%f,%f (%s)",
					"http://maps.google.com/maps?&daddr=%f,%f (%s)",
					psArray.get(position).lat,
					psArray.get(position).lon, 
					psArray.get(position).title);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"); 
			
			try
	        {
			 context.startActivity(intent);
	        }
	        catch(ActivityNotFoundException ex)
	        {
	            try
	            {
	                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
	                context.startActivity(unrestrictedIntent);
	            }
	            catch(ActivityNotFoundException innerEx)
	            {
	            }
	        }
		}
	}
}
