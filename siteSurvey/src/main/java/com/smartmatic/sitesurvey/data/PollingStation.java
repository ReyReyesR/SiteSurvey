package com.smartmatic.sitesurvey.data;

import android.graphics.Bitmap;

/**
 * <p>
 *     This class represents a Polling station.
 * </p>
 */
	public class PollingStation {
	
		public String title;
		public String description;
		public double lat;
		public double lon;
		public Bitmap image;
		public int id;
		public boolean transmitted = false;
		
		public Survey survey = null;
		
		public PollingStation() {
		}
		
		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			PollingStation ps = (PollingStation) o;
			return ps != null && lat == ps.lat && lon == ps.lon;
		}
}
