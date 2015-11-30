package com.smartmatic.sitesurvey;

import java.util.ArrayList;

import com.smartmatic.sitesurvey.core.SurveyAdapterBuilder;
import com.smartmatic.sitesurvey.data.PollingStation;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * <p>
 *     This class represents the list of finished poll locations on the app, a list of polling
 *     station names with a miniature picture of the location on Google maps is displayed. An
 *     asynchronous task is invoked from the start of the app, this task runs in the background.
 *     Once an item is entered in the finished list, every x seconds the AsyncTask tries to transmit
 *     the Answer object to the service. On success the Finished survey is deleted from the list,
 *     otherwise it is displayed on the finished list until it is transmitted.
 * </p>
 *
 * @author Reynaldo
 */

public class FinishedListFragment extends ListFragment{
	
	private static PSAdapter psAdapter = null;	
	public static ArrayList<PollingStation> psArray = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
				
		psAdapter = new PSAdapter(getActivity());
		setListAdapter(psAdapter);
	}
		
	public static void setData(ArrayList<PollingStation> _psArray){
		if(_psArray!=null)
			psArray = _psArray;
	}
	
	public static void AddNew(PollingStation ps){
		ps.survey = SurveyAdapterBuilder.surveys.get(ps.id);
		SurveyAdapterBuilder.surveys.remove(ps.id);
		psArray.add(ps);
		
		if(psAdapter!=null)  psAdapter.notifyDataSetChanged();
	}
	
	public static void SetAsTransmitted(PollingStation ps){
		ps.transmitted = true;
		if(psAdapter!=null)  psAdapter.notifyDataSetChanged();
	}


	public static class PSAdapter extends BaseAdapter{
	
		private Context context;
		
		PSAdapter(Context c){
			context = c;
		}
		
		@Override
		public int getCount() {
			return psArray.size();
		}
	
		@Override
		public Object getItem(int position) {
			return psArray.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return psArray.get(position).id;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view;
			if(convertView == null){
				//Make a new view
				LayoutInflater inflater = (LayoutInflater)context.
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				view = inflater.inflate(R.layout.row_finished, null);
			}else{
				//Use convertView if it is available
				view = convertView;
			}
			PollingStation ps = psArray.get(position);
			
			TextView tTitle = (TextView) view.findViewById(R.id.title);
			tTitle.setText(ps.title);
			
			TextView tDescription = (TextView) view.findViewById(R.id.description);
			tDescription.setText(ps.description);
			
			Typeface tf = Typeface.createFromAsset(context.getAssets(),
					"fonts/OpenSans-Regular.ttf");
			tTitle.setTypeface(tf);
			tDescription.setTypeface(tf);
			
			if(ps.transmitted){
				ImageView image = (ImageView) view.findViewById(R.id.statusImage);
				image.setBackgroundResource(R.drawable.transmitted);
				image.setScaleType(ScaleType.CENTER_INSIDE);
			}
			
			return view;
		}
	}
}
