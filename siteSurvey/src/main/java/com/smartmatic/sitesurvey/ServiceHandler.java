package com.smartmatic.sitesurvey;

/**
 * Created by Reynaldo on 29/10/2015.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class ServiceHandler {

    static String response = "";
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        if(method == 1) {
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
        }
        if(method == 2){
            //TO DO POST METHOD
        }
        Log.e("response", response);
        return response;

    }
}