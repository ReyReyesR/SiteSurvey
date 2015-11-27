package com.smartmatic.sitesurvey.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 *     This class consists of private vars that are filled with public calls from a instantiated
 *     Answer object. It contains all the data that will be fed to the service that fills the DB
 *     with the answers to the surveys.
 * </p>
 * @author Reynaldo
 */
public class Answer {

    JSONObject processedAnswers=new JSONObject();
    private int FkSection;
    private int FkQuestion;
    private int FkAnswer;
    private int IdPerson;
    private String Value;
    private double CoordinatesX;
    private double CoordinatesY;
    int device = 1;

    public Answer(){
    }

    /**
     * <p>
     *     This function puts the info in the JSON Object, the info is contained in the object
     *     variables.
     * </p>
     * @author Reynaldo
     */
    public void fillObject() throws JSONException {

        processedAnswers.put("FkSection",FkSection);
        processedAnswers.put("FkQuestion",FkQuestion);
        processedAnswers.put("FkAnswer",FkAnswer);
        processedAnswers.put("Device", device);
        processedAnswers.put("IdPerson",IdPerson);
        processedAnswers.put("Value",Value);
        processedAnswers.put("CoordinatesX",CoordinatesX);
        processedAnswers.put("CoordinatesY", CoordinatesY);
    }

    /**
     * <p>
     *     This function returns the JSON Object filled with the answers, since the object is
     *     instantiated when the object is filled, the mere existence of the object secures it has
     *     valid data in it.
     * </p>
     * @returns ProcessedAnswers The JSON Object containing the survey answers.
     * @returns null if the JSON Object is not instantiated
     * @throws JSONException if the JSON object is null, hence returning null.
     * @author Reynaldo
     */

    public JSONObject returnObject() throws JSONException {

        if (processedAnswers!=null) return processedAnswers;
        else return null;
    }

    public void putFKSection (int FkSection){
        this.FkSection=FkSection;
    }
    public void putFKQuestion (int FkQuestion){
        this.FkQuestion=FkQuestion;
    }
    public void putFKAnswer (int FkAnswer){
        this.FkAnswer=FkAnswer;
    }
    public void putIdPerson (int IdPerson){
        this.IdPerson=IdPerson;
    }
    public void putValue (String Value){
        this.Value=Value;
    }
    public void putCoordinatesX (int CoordinatesX){
        this.CoordinatesX=CoordinatesX;
    }
    public void putCoordinatesY (int CoordinatesY){
        this.CoordinatesY=CoordinatesY;
    }
}
