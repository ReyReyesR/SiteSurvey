package com.smartmatic.sitesurvey.core;

import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartmatic.sitesurvey.data.*;

/**<p>
 *      This class parses a JSON Object which is received from the Main Activity.
 *      The methods in this class are private except for GetQuestionary(),which is the main call for
 *      this class to parse,fill the questions objects and return an ArrayList consisting of several
 *      question objects that later are used to construct the survey.
 * </p>
 * @author Reynaldo
 */

public class SurveyParser {

    private static final String TAG_SECTIONS = "Sections";
    private static final String FORM_ID = "PkForm";
    private static final String SECTION_ID = "PkSection";
    private static final String QUESTION_ID = "PkQuestion";
    private static final String TAG_QUESTION = "Questions";
    private static final String QUESTION_TYPE = "Type";
    public static int fullSize = 12;
    public static int withKeyboardSize = 5;
    public static Hashtable<String, ArrayList<Question>> dependencies;

    public static ArrayList<Question> getQuestionary(String JSONString) {

        try {
            return parse(JSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

   private static ArrayList<Question> parse(String JSONString) throws JSONException {
        try {
            JSONObject json = new JSONObject(JSONString);
            return readFile(json);
        }  catch (JSONException | IOException e) {
            e.printStackTrace();
        }
       return null;
    }

    /** <p>
     *      This function receives a JSON object and parses trough all the elements until a Question
     *      tag is reached. The JSON object contains various nested JSON Arrays that represent
     *      different levels of the survey info, such as Forms, Sections, Questions and Answers,
     *      these arrays must be parsed in order to reach the questions that are needed to construct
     *      the survey. Once the Question tag is reached, the function calls
     *      readQuestions(JSON Object).
     *  </p>
     *
     * @param json a JsonObject containing Forms info.
     * @return an ArrayList containing various question object.
     * @throws JSONException if JSON object is empty or null.
     */

    private static ArrayList<Question> readFile(JSONObject json) throws IOException, JSONException {

        JSONArray forms, sections;
        final String DATA = "Data";
        String idSection,idForm;
        dependencies = new Hashtable<>();
        ArrayList<Question> questions = new ArrayList<>();
        if (json.has(DATA)) {
            forms = json.getJSONArray(DATA);
            for (int h = 0; h < forms.length(); h++) {
                JSONObject f = forms.getJSONObject(h);
                idForm=(f.getString(FORM_ID));
                if (f.has(TAG_SECTIONS)) {
                    sections = f.getJSONArray(TAG_SECTIONS);
                    for (int i = 0; i < sections.length(); i++) {
                        JSONObject s = sections.getJSONObject(i);
                        idSection=(s.getString(SECTION_ID));
                        if (s.has(TAG_QUESTION)) {
                            dependencies.put(idSection, readQuestions(s,idSection,idForm));
                            questions = readQuestions(s,idSection,idForm);
                        }
                    }
                }
            }
        }
        return questions;
    }

    /** <p>
     *      This function parses all the question elements inside the JSON object, once a question
     *      is found the JSON array containing the questions is passed to readQuestion so each
     *      individual question gets created.
     * </p>
    *@param json receives a JSON Object.
    *@return an ArrayList of question objects.
    *@throws JSONException if JSON object is empty or null.
    */

    private static ArrayList<Question>
    readQuestions(JSONObject json, String idSection, String idForm) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();

        JSONArray quest = json.getJSONArray(TAG_QUESTION);
        for (int j = 0; j < quest.length(); j++) {

            JSONObject q = quest.getJSONObject(j);
            questions.add(readQuestion(q,idSection,idForm));
        }

        return questions;
    }

    /** <p>
     *      This function parses the contents of a question tag. In here the type of question is
     *      discerned through the QUESTION_TYPE tag, this tag determines which question class is
     *      invoked, there are three possible types: Simple Selection, Multiple Selection and
     *      Open Question.
     * </p>
    *@param json receives a JSON Object
    *@return a question object.
    *@throws JSONException if JSON object is empty or null.
    */
    private static Question readQuestion(JSONObject json, String idSection,String idForm)
            throws JSONException {
        String idQuestion;
        Question question = null;

        String tag = json.getString (QUESTION_TYPE);
        idQuestion = json.getString (QUESTION_ID);
        if(tag.equals("Simple"))    question = SingleOptionQuestion.createFromJSON
                (json,idForm,idSection,idQuestion);
        if(tag.equals("Multiple"))  question = MultiOptionQuestion.createFromJSON
                (json,idForm,idSection,idQuestion);
        if(tag.equals("Abierta"))   question = TextQuestion.createFromJSON
                (json,idForm,idSection,idQuestion);

        return question;
    }
}