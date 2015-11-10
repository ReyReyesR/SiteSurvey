package com.smartmatic.sitesurvey.core;
import java.net.Proxy;

import org.ksoap2.*;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.smartmatic.sitesurvey.data.AnswersVector;
import com.smartmatic.sitesurvey.data.DataAlert;
import com.smartmatic.sitesurvey.data.DataSurvey;
import com.smartmatic.sitesurvey.data.StringVector;

public class WebServiceClient {

	final static String NAMESPACE = "http://localhost/MobileSurveyWebService/";
    static String URL="http://%s/MobileSurveyWebService/CensusService.asmx";
    final static String IP = "10.10.2.51";    

    //final
    static String METHOD_NAME = "receiveSurvey";
    static String SOAP_ACTION = "http://localhost/MobileSurveyWebService/receiveSurvey";
   
	public static void SendDummySurvey(Context c){
	
		StringVector sv = new StringVector("hola","chao");
        AnswersVector av = new AnswersVector();
        av.add(sv);
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        		
        SendSurvey(c, new DataSurvey("admin",format.format(new Date()), format.format(new Date()), 1, av));
	}
    
	public static boolean SendSurvey(Context context, DataSurvey survey) {
		
		METHOD_NAME = "receiveSurvey";
	    SOAP_ACTION = "http://localhost/MobileSurveyWebService/receiveSurvey";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	   
		//---------------------------------------------------------------------------------------
        //      SETTING DATA:   
        //---------------------------------------------------------------------------------------

		PropertyInfo pi = new PropertyInfo();
		pi.setName("receiveSurvey1");
		pi.setValue(survey);
        pi.setType(DataSurvey.class);
        request.addProperty(pi);
         
		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        //---------------------------------------------------------------------------------------
        //      MAPPINGS:   
        //---------------------------------------------------------------------------------------

		envelope.addMapping(NAMESPACE, DataSurvey.class.getSimpleName(), DataSurvey.class);

        //---------------------------------------------------------------------------------------
        //      MARSHALLING: 
        //---------------------------------------------------------------------------------------

        Marshal floatMarshal = new MarshalFloat();
        floatMarshal.register(envelope);
			
        //---------------------------------------------------------------------------------------
        //      SENDING REQUEST: 
        //---------------------------------------------------------------------------------------


		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context); 
		
		HttpTransportSE transport = getHttpTransportSE(String.format(URL, preferences.getString("webserviceip_preference", "")));
		try
	    {
			System.setProperty("http.keepAlive", "false");
			
			transport.call(SOAP_ACTION, envelope);
			   Log.d("dump Request: " ,transport.requestDump);
			   Log.d("dump response: " ,transport.responseDump);
	        SoapObject resSoap =(SoapObject)envelope.getResponse();
	        
	        return resSoap.getPropertyAsString(0).equals("0");
	
	    }
	    catch (Exception e)
	    {
	    	Log.d("Exception sending", e.toString());
	    	return false;
	    }

	}
	
	public static boolean SendAlert(Context context, DataAlert alert) {
		
		METHOD_NAME = "receiveAlert";
		SOAP_ACTION = "http://localhost/MobileSurveyWebService/receiveAlert";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		//---------------------------------------------------------------------------------------
        //      SETTING DATA:   
        //---------------------------------------------------------------------------------------

		PropertyInfo pi = new PropertyInfo();
		pi.setName("receiveAlert1");
		pi.setValue(alert);
        pi.setType(DataAlert.class);
        request.addProperty(pi);
         
		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        //---------------------------------------------------------------------------------------
        //      MAPPINGS:   
        //---------------------------------------------------------------------------------------

		envelope.addMapping(NAMESPACE, DataAlert.class.getSimpleName(), DataAlert.class);

        //---------------------------------------------------------------------------------------
        //      MARSHALLING: 
        //---------------------------------------------------------------------------------------

        Marshal floatMarshal = new MarshalFloat();
        floatMarshal.register(envelope);
			
        //---------------------------------------------------------------------------------------
        //      SENDING REQUEST: 
        //---------------------------------------------------------------------------------------


		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context); 
		
		HttpTransportSE transport = getHttpTransportSE(String.format(URL, preferences.getString("webserviceip_preference", "")));
		try
	    {
			System.setProperty("http.keepAlive", "false");
			
			transport.call(SOAP_ACTION, envelope);
			   Log.d("dump Request: " ,transport.requestDump);
			   Log.d("dump response: " ,transport.responseDump);
	        SoapObject resSoap =(SoapObject)envelope.getResponse();
	        
	        return resSoap.getPropertyAsString(0).equals("0");
	
	    }
	    catch (Exception e)
	    {
	    	Log.d("Exception sending ", e.toString());
	    	return false;
	    }

	}	
	
	private final static SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.dotNet = true;
	    envelope.implicitTypes = true;
	    envelope.setAddAdornments(false);
	    envelope.setOutputSoapObject(request);
	 
	    return envelope;
	}
	
	private final static HttpTransportSE getHttpTransportSE(String fullURL) {
		
		Log.d("SERVICIO", fullURL);
	    HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,fullURL,60000);
	    ht.debug = true;
	    ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
	    return ht;
	}
}
