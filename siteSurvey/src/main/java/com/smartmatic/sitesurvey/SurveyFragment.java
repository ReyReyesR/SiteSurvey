package com.smartmatic.sitesurvey;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.smartmatic.sitesurvey.R;
import com.smartmatic.sitesurvey.data.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyFragment extends Fragment {

	private static String ARG_PAGE = "page_number";
	
	private static Survey survey;
	private static SurveyAdapter surveyAdapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SurveyFragment newInstance(int pageNumber) {
		SurveyFragment fragment = new SurveyFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public static void SetSurvey(Survey _survey, SurveyAdapter _surveyAdapter) {
		survey = _survey;
		surveyAdapter = _surveyAdapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Carga un layout base donde seran agregadas las preguntas
		View rootView = inflater.inflate(R.layout.fragment_survey,container, false);
		ViewGroup containerView = (ViewGroup)rootView.findViewById(R.id.container);
		containerView.removeAllViews();
		
		if(getArguments().getInt(ARG_PAGE) < survey.pages.size()){
			ArrayList<Question> page = survey.pages.get(getArguments().getInt(ARG_PAGE));
			int i=0;
			for (Question question : page){	
				
				//Question view
				View qView = question.GetView(inflater, containerView, (SurveyActivity)getActivity(), surveyAdapter, survey.parentId);
				if(qView!=null){
					if(i==0 && page.size() >1){
						qView.setPadding(qView.getPaddingLeft(), 0, qView.getPaddingRight(), qView.getPaddingBottom());
					}
					
				    containerView.addView(qView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				}
			    i++;
			}
		} 
		return rootView;
	}
	
	
	public static int GetPagesCount(){
		return survey.pages.size();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		surveyAdapter.notifyDataSetChanged();
    }
}
