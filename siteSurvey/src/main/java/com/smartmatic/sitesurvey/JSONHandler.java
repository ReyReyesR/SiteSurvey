package com.smartmatic.sitesurvey;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Reynaldo on 10/11/2015.
 */


public class JSONHandler {
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

    int IdAnswer,DateEnd,PKForm,PKSection,PKQuestion,PKAnswer;
    Date EndDate;

    static Formats 	[] forms;
    static Sections 	[] A;
    static Questions  	[] B;
    static Answers 	[] C;

    private JSONArray formatos, sections, questions, answers;

    public boolean populateStruct (String jsonStr){

        if (jsonStr != null) {
            try {

                JSONObject json = new JSONObject(jsonStr);
                //Checks for initial JSON Array
                if(json.has(DATA))
                {
                    formatos = json.getJSONArray(DATA);

                    // Getting Array
                    for (int h = 0; h < formatos.length(); h++)
                    {
                        JSONObject f = formatos.getJSONObject(h);
                        //FORM ID
                        PKForm=(f.getInt(FORM_ID));

                        if(f.has(TAG_SECTIONS))
                        {

                            sections = f.getJSONArray(TAG_SECTIONS);
                            A= new Sections [sections.length()];

                            // looping through All Content
                            for (int i = 0; i < sections.length(); i++)
                            {
                                JSONObject s = sections.getJSONObject(i);

                                PKSection=(s.getInt(SECTION_ID));

                                if (s.has(TAG_QUESTION))
                                {
                                    questions = s.getJSONArray(TAG_QUESTION);

                                    // Storing each json item in variable

                                    for (int j = 0; j < questions.length(); j++) {
                                        JSONObject q = questions.getJSONObject(j);

                                        PKQuestion=(q.getInt(QUESTION_ID));

                                        if (q.has(TAG_ANSWER)){
                                            answers = q.getJSONArray(TAG_ANSWER);


                                            for (int k = 0; k < answers.length(); k++)
                                            {
                                                JSONObject a = answers.getJSONObject(k);
                                                PKQuestion=(a.getInt(QUESTION_ID));


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            Log.e("PopulateStruct", "Couldn't populate the structure");
        }
        return false;
    }

    class Formats {
        String TAG_NAME;
        String TAG_CREATION;
        String TAG_END;
        String TAG_DEVICE;
        String TAG_DESCRIPTION;
        Sections [] A;
        public Formats (String TAG_NAME, String TAG_CREATION, String TAG_END, String TAG_DEVICE, String TAG_DESCRIPTION, Sections []A){
            this.TAG_NAME=TAG_NAME;
            this.TAG_CREATION=TAG_CREATION;
            this.TAG_END=TAG_END;
            this.TAG_DEVICE=TAG_DEVICE;
            this.TAG_DESCRIPTION=TAG_DESCRIPTION;
            this.A = A;

        }
    };

    class Sections{
        String TAG_SECTION_NAME;
        Questions  [] A;
        public Sections (String TAG_SECTION_NAME, Questions A[]){
            this. TAG_SECTION_NAME = TAG_SECTION_NAME;
            this.A = new Questions[5];
            this.A=B;
        }
    };

    class Questions{
        String TAG_TITLE;
        String TAG_QUESTION_CREATION;
        Answers [] A;
        public Questions(String TAG_TITLE,String TAG_QUESTION_CREATION, Answers A[]){
            this.TAG_TITLE=TAG_TITLE;
            this.TAG_QUESTION_CREATION=TAG_QUESTION_CREATION;
            this.A = new Answers[5];
            this.A=C;
        }
    };

    class Answers{
        String TAG_ANSWER_NAME;
        String TAG_ANSWER_TYPE ;
        String TAG_NUMBER;
        public Answers(String TAG_ANSWER_NAME,String TAG_ANSWER_TYPE,String TAG_NUMBER){
            this.TAG_ANSWER_NAME=TAG_ANSWER_NAME;
            this.TAG_ANSWER_TYPE=TAG_ANSWER_TYPE;
            this.TAG_NUMBER=TAG_NUMBER;
        }
    };



}