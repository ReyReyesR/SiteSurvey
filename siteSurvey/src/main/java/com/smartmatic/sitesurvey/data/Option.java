package com.smartmatic.sitesurvey.data;

public class Option {
	public String Name;
	public String Label;
	
	public Option(String name, String label){
		Name = name;
		Label = label;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Label;
	}
}
