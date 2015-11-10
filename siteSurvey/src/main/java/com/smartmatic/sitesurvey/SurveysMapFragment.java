package com.smartmatic.sitesurvey;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.smartmatic.sitesurvey.data.PollingStation;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class SurveysMapFragment extends MapFragment   {
	
    private GoogleMap map;
	private  Location location;
     
    private static double lat = 10.5080572; // latitud de archivo de config
	private static double lon = -66.9102813; // longiud de archivo de config
	
	// Colombia lat = 4.647758; lon = -74.101735; 
	// Venezuela lat = 10.488558, lon = -66.933990
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }
	
	private void setMarker(LatLng position, String titulo, String info) {
		  // Agregamos marcadores para indicar sitios de intereses.
		  map.addMarker(new MarkerOptions()
		       .position(position)
		       .title(titulo)  //Agrega un titulo al marcador
		       .snippet(info)   //Agrega informacion detalle relacionada con el marcador
		       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //Color del marcador
		}
	
    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = getMap();
        }
        if (map != null) {
            map.setMyLocationEnabled(true);
            location = map.getMyLocation();
         // Si Localizacion manual esta activa (Leer del XML el pto fuera de la geo cerca) TRUE PROVISIONAL
            if (true){
               map.animateCamera(CameraUpdateFactory.newLatLngZoom(
        			new LatLng(lat,lon),17));
            }
            else if (location != null) {
            	map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            			new LatLng(lat,lon),17));

            }else{
            	//map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            			//new LatLng(PendingListFragment.psArray.get(0).lat, PendingListFragment.psArray.get(0).lon),13));
            	
            }
            
            map.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker arg0) {
					
					int position = 0;
					for(PollingStation ps : PendingListFragment.psArray){
						if(ps.title.equals(arg0.getTitle())){
							MarkerDialogFragment dialog = new MarkerDialogFragment(position);
							dialog.show(getFragmentManager(), "");
						}
						position++;
					}
					return true;
				}
			});
            
        }

        DrawMarkers();
    }

	public double getLatitude(){
		return location.getLatitude();
	}

	public double getLongitude(){

		return location.getLongitude();
	}


	public void DrawMarkers(){
    	if (map != null) {
	    	map.clear();
	    	if (PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("manualLocation_preference", false)){ 
	            setMarker(new LatLng(lat,lon),null,null);
	        }
	    	//circle
	    	/*Circle circle = map.addCircle(new CircleOptions()
	         .center(new LatLng(PendingListFragment.psArray.get(0).lat, PendingListFragment.psArray.get(0).lon))
	         .radius(500)
	         .strokeColor(Color.BLUE)
	         .fillColor(Color.argb(60, 0, 0, 100)));*/
	    	/*
	    	map.addPolygon(new PolygonOptions()
	        .add(new LatLng(x, y), new LatLng(x, y-2), new LatLng(x+3, y-2), new LatLng(x, y))
	        .strokeColor(Color.RED)
	        .fillColor(Color.BLUE));
	    	*/
	    	
	    	for(PollingStation ps : PendingListFragment.psArray){
	    		map.addMarker(new MarkerOptions()
	                .title(ps.title)
	                .snippet(ps.description)
	                .position(new LatLng(ps.lat, ps.lon)));
	    	}
	    	
   	 	}
    }
}
