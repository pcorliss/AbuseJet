package com.fiftyprojects.abusejet;

public class Threshold {
	private String action;
	private int value;
	private int ttl;
	private String modifier;
	
	public Threshold(){
		
	}

	public Threshold(String action, int value, int ttl, String modifier) {
		super();
		this.action = action;
		this.value = value;
		this.ttl = ttl;
		this.modifier = modifier;
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
	
	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
