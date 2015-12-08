package com.smartmatic.sitesurvey.data;

/**
 * <p>
 *     This class contains the options representing each possible answer of a question.
 * </p>
 */

public class Option {
	public String 	Name;
	public String 	Label;
	public String 	IdAnswer;
	public String 	IdForm;
	public int	 	IdQuestion;
	public int		IdSection;

	public Option(String name, String label, String idForm, String idAnswer, int idQuestion,
				  int idSection){
		this.Name = 		name;
		this.Label = 		label;
		this.IdAnswer =		idAnswer;
		this.IdForm =		idForm;
		this.IdQuestion = 	idQuestion;
		this.IdSection =	idSection;

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Label;
	}
}
