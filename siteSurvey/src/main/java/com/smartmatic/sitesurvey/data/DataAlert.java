package com.smartmatic.sitesurvey.data;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DataAlert implements KvmSerializable {

	String User;
    String Date; 
    String TypeAlert;
    String Arg;

    public DataAlert(String _date, String _typeAlert, String _user, String _arg){
    	Date = _date;
    	TypeAlert = _typeAlert;
		User = _user;
		Arg = _arg;
    }
    
    @Override
    public Object getProperty(int arg0) {
    switch (arg0){
    	case 0:
    		return User;
        case 1:
            return Date;
        case 2:
            return TypeAlert;   
        case 3:
            return Arg; 
        default:
            return null;
            }
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
	    switch(arg0)
	    {
	        case 0:
	            arg2.type = PropertyInfo.STRING_CLASS;
	            arg2.name = "arg0";
	            break;
	        case 1:
	            arg2.type = PropertyInfo.STRING_CLASS;
	            arg2.name = "arg1";
	            break;
	        case 2:
	        	arg2.type = PropertyInfo.STRING_CLASS;
	            arg2.name = "arg2";
	            break;
	        case 3:
	        	arg2.type = PropertyInfo.STRING_CLASS;
	            arg2.name = "arg3";
	            break;
	        default:break;
	    }

    }

    @Override
    public void setProperty(int arg0, Object arg1) {
	    switch(arg0)
	    {
	        case 1:
	            User =  (String)arg1;
	            break;
	        case 0:
	            Date =  (String)arg1;           
	            break;
	        case 2:
	            TypeAlert = (String)arg1;           
	            break;
	        case 3:
	            Arg = (String)arg1;           
	            break;
	        default:
	            break;
	    }
    }
}