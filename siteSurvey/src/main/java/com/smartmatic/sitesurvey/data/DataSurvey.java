package com.smartmatic.sitesurvey.data;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DataSurvey implements KvmSerializable {

	String User;
	String StartDate;
    String FinishDate; 
    int CodeAlert;
    AnswersVector Answers;

    public DataSurvey(String _user, String _startDate, String _finishDate, int _code, AnswersVector _answers){
    	User = _user;
    	StartDate = _startDate;
    	FinishDate = _finishDate;
    	CodeAlert = _code;
    	Answers = _answers;
    }
    
    @Override
    public Object getProperty(int arg0) {
    switch (arg0){
    	case 0:
    		return CodeAlert;
        case 1:
            return StartDate;
        case 2:
        	return FinishDate;
        case 3:
            return Answers;
        case 4:
            return User;	    
        default:
            return null;
            }
    }

    @Override
    public int getPropertyCount() {
        return 5;//because you have 3 parameters
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
	    switch(arg0)
	    {
	        case 0:
	            arg2.type = PropertyInfo.INTEGER_CLASS;
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
	            arg2.type = PropertyInfo.VECTOR_CLASS;
	            arg2.name = "arg3";
	            break;
	        case 4:
	        	arg2.type = PropertyInfo.STRING_CLASS;
	            arg2.name = "arg4";
	        default:break;
	    }

    }

    @Override
    public void setProperty(int arg0, Object arg1) {
	    switch(arg0)
	    {
	        case 1:
	            StartDate =  (String)arg1;
	            break;
	        case 2:
	            FinishDate =  (String)arg1;
	            break;    
	        case 0:
	        	CodeAlert =  (Integer)arg1;           
	            break;
	        case 3:
	        	Answers = (AnswersVector)arg1;           
	            break;
	        case 4:
	            User =  (String)arg1;
	            break;        
	        default:
	            break;
	    }
    }
}