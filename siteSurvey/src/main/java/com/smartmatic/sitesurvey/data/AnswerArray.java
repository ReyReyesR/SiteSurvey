package com.smartmatic.sitesurvey.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Reynaldo on 16/11/2015.
 * Instatiate, fill and return a JSON Array with JSONObject elements.
 * @return JSONObject containing a PKForm and an array of JSON Objects.
 * @throws JSONException if JSONObject is null.
 */

public class AnswerArray {
    private JSONObject obj;
    private JSONArray array;

    public AnswerArray() {
        obj = new JSONObject();
        array = new JSONArray();
    }

    public void fillArray(JSONObject json) throws JSONException {
        array.put(json);
    }

    public void putPKForm (int PKForm) throws JSONException {
        obj.put("FKForm", PKForm);
    }

    public JSONObject getAnswerObject () throws JSONException {
        obj.put("Data",array);
        return obj;
    }
}
