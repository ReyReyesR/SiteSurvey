package com.smartmatic.sitesurvey.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.xmlpull.v1.*;

import com.smartmatic.sitesurvey.data.*;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

public class SurveyParser {

    private static final String ns = null;
    private static final String configFile = "Configuration.xml";
    public static int fullSize = 12;
    public static int withKeyboardSize = 5;
    public static Hashtable<String, ArrayList<Question>> dependencies;
    
    public static ArrayList<Question> GetQuestionary(Context context) {
    	try {
			return parse(getConfigFile(context));
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private static InputStream getConfigFile(Context context) {
        if(isExternalStorageReadable()){
	    	// Get the directory for the app's private files 
	        File file = new File(context.getExternalFilesDir(null), configFile);
	        
	        
	        try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return null;
        
    }
    
    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
    
	public static ArrayList<Question> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFile(parser);
        } finally {
            in.close();
        }
    }
	
	private static ArrayList<Question> readFile(XmlPullParser parser) throws XmlPullParserException, IOException {
		dependencies = new Hashtable<String, ArrayList<Question>>();

        parser.require(XmlPullParser.START_TAG, ns, "dynamic_items");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String id = parser.getAttributeValue(null, "id");
            // Starts by looking for the entry tag
            if (name.equals("questions")) {
            	dependencies.put(id, readQuestions(parser));
            } 
        }
        parser.require(XmlPullParser.END_TAG, ns, "dynamic_items");
        parser.nextTag();
		
		ArrayList<Question> questions = new ArrayList<Question>();

        parser.require(XmlPullParser.START_TAG, ns, "questionary");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("questions")) {
            	questions = readQuestions(parser);
            } 
        }
        parser.require(XmlPullParser.END_TAG, ns, "questionary");
        return questions;
    }
	
	private static ArrayList<Question> readQuestions(XmlPullParser parser) throws XmlPullParserException, IOException {
		ArrayList<Question> questions = new ArrayList<Question>();

        parser.require(XmlPullParser.START_TAG, ns, "questions");
        try{
        	fullSize = Integer.parseInt(parser.getAttributeValue(null, "full-size"));
	        withKeyboardSize= Integer.parseInt(parser.getAttributeValue(null, "with-keyboard-size"));
        }
        catch(Exception e){        }
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("question")) {
            	questions.add(readQuestion(parser));
            } 
        }
        parser.require(XmlPullParser.END_TAG, ns, "questions");
        return questions;
    }
	
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private static Question readQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "question");

        Question question = null;
        
        String tag = parser.getName();
        String questionType = parser.getAttributeValue(null, "type");
        if (tag.equals("question")) {
            if(questionType.equals("label")){
            	question = LabelQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("text")){
            	question = TextQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("singleOption")){
            	question = SingleOptionQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("multiOption")){
            	question = MultiOptionQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("combo")){
            	question = SpinnerQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("time")){
            	question = TimeQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("date")){
            	question = DateQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("yesNo")){
            	question = YesNoQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("gps")){
            	question = GPSQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("photo")){
            	question = PhotoQuestion.CreateFromXML(parser);
            } 
            else if(questionType.equals("network")){
            	question = NetworkQuestion.CreateFromXML(parser);
            }  
            else if(questionType.equals("author")){
            	question = AuthorQuestion.CreateFromXML(parser);
            } 
            else if(questionType.equals("startDate")){
            	question = StartDateQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("finishDate")){
            	question = FinishDateQuestion.CreateFromXML(parser);
            }
            else if(questionType.equals("address")){
            	question = AuthorQuestion.CreateFromXML(parser);
            }
        }
    	parser.nextTag();
       
        return question;
    }
	
}
