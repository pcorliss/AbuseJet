package com.fiftyprojects.abusejet;

public class Threshold {
	private String action;
	private int value;
	private int ttl;
	
	public Threshold(){
		
	}
	
	public Threshold(String action, int value, int ttl) {
		super();
		this.action = action;
		this.value = value;
		this.ttl = ttl;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
}
