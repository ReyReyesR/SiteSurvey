package com.smartmatic.sitesurvey.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Reynaldo on 16/11/2015.
 */
public class Answer {

    JSONObject ProcessedAnswers=new JSONObject();
    int PKSection;
    int PKQuestion;
    int PKAnswer;
    int Device=0;
    int IdPerson;
    String Value;
    double CoordinatesX;
    double CoordinatesY;

    public Answer(){
    }

    public void fillObject() throws JSONException {

        JSONObject ProcessedAnswers=new JSONObject();

        ProcessedAnswers.put("PKSection",PKSection);
        ProcessedAnswers.put("PKQuestion",PKQuestion);
        ProcessedAnswers.put("PKAnswer",PKAnswer);
        ProcessedAnswers.put("Device",Device);
        ProcessedAnswers.put("IdPerson",IdPerson);
        ProcessedAnswers.put("Value",Value);
        ProcessedAnswers.put("CoordinatesX",CoordinatesX);
        ProcessedAnswers.put("CoordinatesY",CoordinatesY);
    }

    public JSONObject returnObject() throws JSONException {
      return ProcessedAnswers;
    }

    public void putPKSection (int PKSection){
        this.PKSection=PKSection;
    }
    public void putPKQuestion (int PKQuestion){
        this.PKQuestion=PKQuestion;
    }
    public void putPKAnswer (int PKAnswer){
        this.PKAnswer=PKAnswer;
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
