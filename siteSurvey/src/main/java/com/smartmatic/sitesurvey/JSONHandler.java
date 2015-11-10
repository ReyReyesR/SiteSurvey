package com.smartmatic.sitesurvey;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Reynaldo on 10/11/2015.
 */


public class JSONHandler {
    private static final String DATA = "Data";

    private static final String TAG_NAME = "Name";
    private static final String TAG_CREATION = "DateCreation";
    private static final String TAG_END = "DateEnd";
    private static final String TAG_DEVICE = "Device";
    private static final String TAG_DESCRIPTION = "Description";
    private static final String TAG_SECTIONS = "Sections";

    private static final String TAG_SECTION_NAME = "Name";
    private static final String TAG_QUESTION = "Questions";
    private static final String TAG_QUESTION_CREATION = "DateCreation";
    private static final String TAG_TITLE = "Title";
    private static final String TAG_ANSWER = "Answers";
    private static final String TAG_ANSWER_NAME = "Name";
    private static final String TAG_ANSWER_TYPE = "Type";
    private static final String TAG_NUMBER = "Number";

    static Formats 	[] forms;
    static Sections 	[] A;
    static Questions  	[] B;
    static Answers 	[] C;

    private JSONArray formatos, sections, questions, answers;

    public boolean PopulateStruct (String jsonStr){

        if (jsonStr != null) {
            try {

                JSONObject json = new JSONObject(jsonStr);
                //Checks for initial JSON Array
                if(json.has(DATA)) {
                    formatos = json.getJSONArray(DATA);
                    forms= new Formats [formatos.length()];

                    // Getting Array
                    for (int h = 0; h < formatos.length(); h++){
                        JSONObject f = formatos.getJSONObject(h);
                        sections = f.getJSONArray(TAG_SECTIONS);
                        A= new Sections [sections.length()];
                        forms [h]= new Formats(f.getString(TAG_NAME),f.getString(TAG_CREATION),f.getString(TAG_END),f.getString(TAG_DESCRIPTION),f.getString(TAG_DEVICE),A);

                        // looping through All Content
                        for (int i = 0; i < sections.length(); i++) {
                            JSONObject s = sections.getJSONObject(i);
                            questions = s.getJSONArray(TAG_QUESTION);
                            B= new Questions[questions.length()];
                            // Storing each json item in variable
                            forms[h].A[i]= new Sections (s.getString(TAG_SECTION_NAME),B);

                            for (int j = 0; j < questions.length(); j++) {
                                JSONObject q = questions.getJSONObject(j);
                                answers = q.getJSONArray(TAG_ANSWER);
                                C=  new Answers[answers.length()];

                                forms[h].A[i].A[j] = new Questions (q.getString(TAG_TITLE), q.getString(TAG_QUESTION_CREATION),C);

                                for (int k = 0; k < answers.length(); k++) {
                                    JSONObject a = answers.getJSONObject(k);

                                    forms[h].A[i].A[j].A[k] = new Answers(a.getString(TAG_ANSWER_NAME),a.getString (TAG_ANSWER_TYPE),a.getString(TAG_NUMBER));
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