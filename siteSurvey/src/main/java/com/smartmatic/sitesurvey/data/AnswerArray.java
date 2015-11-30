package com.smartmatic.sitesurvey.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * <p>
 *     This class represents the array of the Answers objects represented with JSON objects
 *     generated in the Answer class. The class receives n JSON objects, that puts them on a JSON
 *     array, and then encapsulates in another JSON object which then returns.
 * </p>
 * @author Reynaldo
 */

public class AnswerArray {
    private JSONObject obj;
    private JSONArray array;

    public AnswerArray() {
        obj = new JSONObject();
        array = new JSONArray();
    }

    /**
     * <p>
     *     This function fills the array of JSON objects.
     * </p>
     * @param json The JSON object generated in the Answer class that contains the survey answers.
     * @throws JSONException if the JSON object is null.
     * @author Reynaldo
     */
    public void fillArray(JSONObject json) throws JSONException {
        array.put(json);
    }

    /**
     * <p>
     *     This function fills the array of JSON objects.
     * </p>
     * @param PKForm The Public Key Id of the form that was used to create the survey.
     * @throws JSONException if the obj object is null.
     * @author Reynaldo
     */
   /* public void putPKForm (int PKForm) throws JSONException {
        obj.put("FKForm", PKForm);
    }*/

    /**
     * <p>
     *     This function puts the array of JSON objects inside the JSON Object that encapsulates it.
     *     Once all the JSON Objects were put inside the JSON Array, we need to "close" the array,
     *     and encapsulate it on a JSON object that carries additional information that will be used
     *     in the service that consumes this object.
     * </p>
     * @throws JSONException if the obj object is null.
     * @return obj Returns the obj that has a PKForm, and the array "Data" that contains all the
     * processed JSON Objects.
     * @author Reynaldo
     */
    public JSONArray getAnswerObject () throws JSONException {
        //obj.put("Data",array);
        return array;
    }
}
