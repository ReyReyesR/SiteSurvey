package com.smartmatic.sitesurvey.data;

import org.xmlpull.v1.XmlPullParser;

public class AuthorQuestion extends TextQuestion  {

	public AuthorQuestion(String questionName, String questionLabel,
			String questionAnswer, String _fontSize, String _color,
			String _inputType, String _hint, String _repeat, String _innerFontSize) {
		super(questionName, questionLabel, questionAnswer, _fontSize, _color, _inputType, _hint, _repeat, _innerFontSize);
	}
	
	
	public static Question CreateFromXML(XmlPullParser parser){
		
		String name = parser.getAttributeValue(null, "name");
		String label = parser.getAttributeValue(null, "label");
		String inputType = parser.getAttributeValue(null, "inputType");
		String hint = parser.getAttributeValue(null, "hint");
		String fontSize = parser.getAttributeValue(null, "font-size");
		String innerFontSize = parser.getAttributeValue(null, "inner-font-size");
		String color = parser.getAttributeValue(null, "font-color");
		String repeat = parser.getAttributeValue(null, "repeat");
		
		return new AuthorQuestion(name, label, "admin", fontSize, color, inputType, hint, repeat, innerFontSize);
	}
	
	@Override
	public Question clone() {
		AuthorQuestion aq= new AuthorQuestion(name, label, answer, String.valueOf(fontSize), 
				color, inputType, hint, dependency, String.valueOf(innerFontSize));

		return aq;
	}
}
