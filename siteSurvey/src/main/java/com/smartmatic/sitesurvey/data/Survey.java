package com.smartmatic.sitesurvey.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;


public class Survey {

	public ArrayList<ArrayList<Question>> pages = new ArrayList<ArrayList<Question>>();
	public Hashtable<String, Survey> base_dependentSurveys = new Hashtable<String, Survey>();
	public Hashtable<Integer, Survey> dependentSurveys = new Hashtable<Integer, Survey>();
	public int fullSize = 12;
	public int withKeyboardSize = 5;
	public int parentId = 0;
	public String startDate;
	int idSection,idQuestion,idAnswer;
	String value;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

	public Survey(ArrayList<Question> _questions, Hashtable<String, ArrayList<Question>> _dependentQuestions, int _fullSize, int _withKeyboardSize) {

		fullSize = _fullSize;
		withKeyboardSize = _withKeyboardSize;

		ArrayList<Question> page = new ArrayList<Question>();
		int usedSpace=0;
		boolean containsText = false;
		int pageCount=0;

		for(int i=0; i< _questions.size(); i++)
		{
			Question q = _questions.get(i);
			usedSpace+=q.space;
			containsText |= q.getClass().toString().contains("Text");
			if((containsText && usedSpace > withKeyboardSize) || (!containsText && usedSpace > fullSize)) {
				pageCount++;
				Question last = page.get(page.size()-1);
				if(last.getClass().toString().contains("Label")){
					page.remove(page.size()-1);
					pages.add(page);
					page = new ArrayList<Question>();
					page.add(last.clone());
					usedSpace=last.space + q.space;
				}
				else{
					pages.add(page);
					page = new ArrayList<Question>();
					usedSpace=q.space;
				}

				containsText = q.getClass().toString().contains("Text");
			}
			q.page = pageCount;
			page.add(q.clone());


		}
		if(page.size()>0)
			pages.add(page);

		page = new ArrayList<Question>();
		page.add(new FinishQuestion(idSection,idQuestion,idAnswer,value));
		pages.add(page);

		if(_dependentQuestions!= null){
			for(String depName: _dependentQuestions.keySet()){
				Survey depSurvey = new Survey(_dependentQuestions.get(depName),null,_fullSize,_withKeyboardSize);
				base_dependentSurveys.put(depName, depSurvey);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public Survey(int _parentId, ArrayList<ArrayList<Question>> _pages , Hashtable<String, Survey> _base_dependentSurveys, String _startDate) {

		parentId = _parentId;
		startDate = _startDate;

		if(_base_dependentSurveys!=null) {
			base_dependentSurveys = (Hashtable<String, Survey>)_base_dependentSurveys.clone();
		}

		pages = new ArrayList<ArrayList<Question>>();
		for(ArrayList<Question> _page: _pages){
			ArrayList<Question> page = new ArrayList<Question>();
			for(Question question: _page){
				page.add(question.clone());
			}
			pages.add(page);
		}

	}

	public Survey getDependentSurvey(int depId, String dependencyName) {
		if(dependentSurveys.get(depId) == null){
			dependentSurveys.put(depId, new Survey(depId, base_dependentSurveys.get(dependencyName).pages, null,format.format(new Date())));
		}
		return dependentSurveys.get(depId);
	}
}