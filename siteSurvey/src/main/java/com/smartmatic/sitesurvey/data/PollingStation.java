package com.smartmatic.sitesurvey.data;

//import com.google.android.gms.internal.l.a;

import android.graphics.Bitmap;

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
			PollingStation ps = (PollingStation)o;
			if(ps!=null)
				return lat == ps.lat && lon == ps.lon;
			return false;
		}
}
