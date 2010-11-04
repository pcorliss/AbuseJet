package com.fiftyprojects.abusejet;

public class Blocks {
	private String type;
	private String value;
	private String action;
	
	public Blocks(){
		
	}
	
	public Blocks(String type, String value, String action) {
		super();
		this.type = type;
		this.value = value;
		this.action = action;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
