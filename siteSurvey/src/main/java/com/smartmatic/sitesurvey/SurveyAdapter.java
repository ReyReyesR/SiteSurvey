package com.smartmatic.sitesurvey;

import java.util.*;
import com.smartmatic.sitesurvey.data.Question;
import com.smartmatic.sitesurvey.data.RepeatQuestion;
import com.smartmatic.sitesurvey.data.Survey;
import com.smartmatic.sitesurvey.data.Question.AnsweredListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SurveyAdapter extends FragmentPagerAdapter {

	private SurveyActivity context = null;
	public Survey survey = null;
	public SurveyAdapter(Context _context, FragmentManager fm, Survey _survey) {
		super(fm);
		context = (SurveyActivity)_context;

		survey=_survey;
		SetDependencies(survey);
		SurveyFragment.SetSurvey(survey, this);
	}
	
	
	private  void SetDependencies(final Survey survey){
		
		final SurveyAdapter sa= this;
		
		for(ArrayList<Question> page: survey.pages){
			for(final Question question: page){
				if(!question.dependency.equals("")){
					question.setAnsweredListener(new AnsweredListener() {
						
						@Override
						public void onAnswered(String answer) {
							try{								
								if((answer.equals("") || answer.equals("0")) && question.repeated > 0){
									question.repeated = 0;
									survey.pages.remove(question.page+1);
									sa.notifyDataSetChanged();
									context.mViewPager.setAdapter(sa);
									context.mViewPager.setCurrentItem(question.page);
								}
								else if (!answer.equals("") && !answer.equals("0")){
									int repeat = Integer.parseInt(answer);
									
									if(question.repeated!= repeat){
										if(question.repeated > 0){
											question.repeated = 0;
											survey.pages.remove(question.page+1);
											sa.notifyDataSetChanged();
											context.mViewPager.setAdapter(sa);
											context.mViewPager.setCurrentItem(question.page);
										}
										
										ArrayList<Question> questions = new ArrayList<Question>();
										
										for(int i=0; i< repeat ; i++){
											questions.add(new RepeatQuestion(""+i,context.getText(R.string.classroom) +" "+ (i+1), "14", "", i, question.dependency));
										}
										question.repeated = repeat;
										survey.pages.add(question.page+1, questions);
										sa.notifyDataSetChanged();
										context.mViewPager.setAdapter(sa);
										context.mViewPager.setCurrentItem(question.page);
									}
								}
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}
	
	
	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below)
		return SurveyFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return SurveyFragment.GetPagesCount();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}
