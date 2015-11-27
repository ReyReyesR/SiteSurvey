package com.smartmatic.sitesurvey;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p>
 *     This class creates a URL connection to a host, and GETs or POSTs a JSON object containing
 *     survey's information.
 * </p>
 * @author Reynaldo
 */

public class ServiceHandler {

    static String response;

    public ServiceHandler() {

    }
    /**
     * <p>
     *     This function receives a URL direction and a method of connection, connects to a host
     *     through the url and receives a JSON Object.
     * </p>
     * @param url url to establish a connection.
     * @return a String response containing the JSON Object.
     * @throws UnsupportedEncodingException if bad encoding
     * @throws IOException  if the InputStream is null.
     * @author Reynaldo
     */
    public String getServiceCall(String url) throws IOException {
        response="";
        try {
            URL urlUp = new URL(url);
            URLConnection conn = urlUp.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = "";

            while ((data = reader.readLine()) != null) {
                response += data;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("response", response);
              response=" {\n" +
                "    \"ContentEncoding\": null,\n" +
                "    \"ContentType\": null,\n" +
                "    \"Data\": [\n" +
                "       {\n" +
                "     \"PkForm\": 1089,\n" +
                "     \"Name\": \"Pruebas\",\n" +
                "     \"DateCreation\": \"2015-11-04T00:00:00\",\n" +
                "     \"DateEnd\": \"2015-11-04T00:00:00\",\n" +
                "     \"Description\": \"Pruebas\",\n" +
                "     \"Device\": 1,\n" +
                "     \"Design_Forms\": [],\n" +
                "     \"Sections\": [\n" +
                "       {\n" +
                "         \"PkSection\": 1133,\n" +
                "         \"FkForm\": 1089,\n" +
                "         \"Name\": \"Seccion1\",\n" +
                "         \"Questions\": [\n" +
                "           {\n" +
                "             \"PkQuestion\": 1227,\n" +
                "             \"FkSection\": 1133,\n" +
                "             \"Type\": \"Simple\",\n" +
                "             \"Title\": \"Cual es su Sexo?\",\n" +
                "             \"DateCreation\": \"2015-11-04T00:00:00\",\n" +
                "             \"Answers\": [\n" +
                "               {\n" +
                "                 \"PkAnswer\": 1314,\n" +
                "                 \"FkQuestion\": 1227,\n" +
                "                 \"Name\": \"Masculino\",\n" +
                "                 \"Number\": 1\n" +
                "               },\n" +
                "               {\n" +
                "                 \"PkAnswer\": 1315,\n" +
                "                 \"FkQuestion\": 1227,\n" +
                "                 \"Name\": \"Femenino\",\n" +
                "                 \"Number\": 2\n" +
                "               }\n" +
                "             ]\n" +
                "           }\n" +
                "         ]\n" +
                "       }\n" +
                "     ]\n" +
                "   }   " +
                " ],\n" +
                "    \"JsonRequestBehavior\": 1,\n" +
                "    \"MaxJsonLength\": null,\n" +
                "    \"RecursionLimit\": null\n" +
                "}\n";
        return response;
    }

    /**
     * <p>
     *     This function receives a URL direction and a JSON object, connects to a host
     *     through the url and sends the JSON Object.
     * </p>
     * @param url url to establish a connection.
     * @return a String response containing the JSON Object.
     * @throws UnsupportedEncodingException if bad encoding
     * @throws IOException  if the InputStream is null.
     * @author Reynaldo
     */

    public boolean postServiceCall(String url, JSONObject jsonObject) throws IOException {
        InputStream inputStream = null;
        response="";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            response = convertInputStreamToString(inputStream);
            if (response.equals("200")) return true;
            else return false;

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return false;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}