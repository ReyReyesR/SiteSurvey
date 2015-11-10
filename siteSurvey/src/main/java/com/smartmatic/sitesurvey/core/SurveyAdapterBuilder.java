package com.smartmatic.sitesurvey.core;

import java.text.SimpleDateFormat;
import java.util.*;

import com.smartmatic.sitesurvey.SurveyAdapter;
import com.smartmatic.sitesurvey.data.Survey;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;

public class SurveyAdapterBuilder {

	private static Survey baseSurvey;
	public static Hashtable<Integer,Survey> surveys = new  Hashtable<Integer,Survey>();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
	
	public static void setSurvey(Survey _survey){
		baseSurvey = _survey;
	}
		
	public static SurveyAdapter BuildAdapter(Context context, int psId, FragmentManager fragmentManager){
		//TODO:
		// INCLIR INFORMACION RELACIONADA AL SITIO 
		// RECUERDA QUE AQUI LO LLAMASTE PLACE PERO EN OTRO LADO LO LLAMASTE PS (POLLING STATION) -- REVISAR
		
		if(surveys.get(psId) == null){
			surveys.put(psId, new Survey(psId, baseSurvey.pages,baseSurvey.base_dependentSurveys, format.format(new Date())));
		}
		return new SurveyAdapter(context, fragmentManager,surveys.get(psId));
	}

	public static SurveyAdapter getDependentAdapter(Context context, int psId, int depId, String dependencyName, FragmentManager fragmentManager) {
		
		if(surveys.get(psId) != null){
			return new SurveyAdapter(context, fragmentManager, surveys.get(psId).getDependentSurvey(depId, dependencyName));
		}

		return null;
	}

}
