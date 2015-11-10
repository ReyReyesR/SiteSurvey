package com.smartmatic.sitesurvey.data;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class StringVector implements KvmSerializable {

	public String question;
	public String answer;
	
	public StringVector(String _question, String _answer){
		question = _question;
		answer = _answer;
	}
	
    @Override
    public Object getProperty(int arg0) {
    	switch (arg0){
        case 0:
            return question;
        case 1:
            return answer;
        default:
            return null;
        }
    }

    @Override
    public int getPropertyCount() {
            return 2;
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
            arg2.name = "string";
            arg2.type = PropertyInfo.STRING_CLASS;
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
    	switch(arg0)
	    {
	        case 0:
	            question =  (String)arg1;
	            break;
	        case 1:
	            answer =  (String)arg1;           
	            break;
	        default:
	            break;
	    }
    }

}