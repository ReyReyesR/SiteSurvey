package com.smartmatic.sitesurvey.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private static final String DATA = "Data";
    private static final String TAG_SECTIONS = "Sections";
    private static final String FORM_ID = "PKForm";
    private static final String SECTION_ID = "PKSection";
    private static final String QUESTION_ID = "PKQestion";
    private static final String ANSWER_ID = "PKAnswer";
    private static final String TAG_SECTION_NAME = "Name";
    private static final String TAG_QUESTION = "Questions";
    private static final String TAG_QUESTION_CREATION = "DateCreation";
    private static final String TAG_TITLE = "Title";
    private static final String TAG_ANSWER = "Answers";
    private static final String TAG_ANSWER_NAME = "Name";
    private static final String TAG_ANSWER_TYPE = "Type";
    private static final String TAG_NUMBER = "Number";
    private static final String TAG_END = "DateEnd";
    private static final String QUESTION_TYPE = "Type";

    private static AnswerArray A;

    public static ArrayList<Question> getQuestionary(String JSONString) {

        try {
            return parse(JSONString);
        } catch (JSONException e) {
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

    public static ArrayList<Question> parse(String JSONString) throws JSONException {
        try {
            JSONObject json = new JSONObject(JSONString);
            //Checks for initial JSON Array
            return readFile(json);
        }  catch (JSONException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<Question> readFile(JSONObject json) throws XmlPullParserException, IOException, JSONException {

        JSONArray formatos, sections, quest, answers;
        String idSection;
        dependencies = new Hashtable<String, ArrayList<Question>>();
        ArrayList<Question> questions = new ArrayList<Question>();
        //Checks for initial JSON Array
        if (json.has(DATA)) {
            formatos = json.getJSONArray(DATA);

            // Getting Array
            for (int h = 0; h < formatos.length(); h++) {
                JSONObject f = formatos.getJSONObject(h);
                //FORM ID
                A = new AnswerArray();
                A.putPKForm((f.getInt(FORM_ID)));

                if (f.has(TAG_SECTIONS)) {
                    Answer B = new Answer();
                    sections = f.getJSONArray(TAG_SECTIONS);
                    // looping through All Content
                    for (int i = 0; i < sections.length(); i++) {
                        JSONObject s = sections.getJSONObject(i);
                        //B.putPKSection(s.getInt(SECTION_ID));
                        idSection=(s.getString(SECTION_ID));

                        if (s.has(TAG_QUESTION)) {
                            dependencies.put(idSection, readQuestions(s));
                            questions = readQuestions(s);
                            /*quest = s.getJSONArray(TAG_QUESTION);

                            // Storing each json item in variable
                            for (int j = 0; j < quest.length(); j++) {

                                JSONObject q = quest.getJSONObject(j);

                                dependencies.put(idSection, readQuestions(q));
                                questions = readQuestions(q);
                                //B.putPKQuestion(q.getInt(QUESTION_ID));

                                if (q.has(TAG_ANSWER)) {
                                    answers = q.getJSONArray(TAG_ANSWER);


                                    for (int k = 0; k < answers.length(); k++) {
                                        JSONObject a = answers.getJSONObject(k);
                                        //B.putPKAnswer(a.getInt(Answer_ID));
                                    }
                                }
                            }*/
                        }
                    }
                }
            }
        }
        return questions;
    }

    private static ArrayList<Question> readQuestions(JSONObject json) throws JSONException {
        ArrayList<Question> questions = new ArrayList<Question>();

        JSONArray quest = json.getJSONArray(TAG_QUESTION);
        for (int j = 0; j < quest.length(); j++) {

            JSONObject q = quest.getJSONObject(j);
            questions.add(readQuestion(q));
        }

        return questions;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private static Question readQuestion(JSONObject json) throws JSONException {


        Question question = null;

        String tag = json.getString(QUESTION_TYPE);
        if(tag.equals("Simple"))    question = SingleOptionQuestion.createFromJSON(json);
        if(tag.equals("Multiple"))  question = MultiOptionQuestion.createFromJSON(json);
        if(tag.equals("Abierta"))   question = TextQuestion.createFromJSON(json);

        return question;
    }

}