package com.fiftyprojects.abusejet;

public class Tracked {
	private String name;
	private Threshold[] thresholds;
	
	
	public Tracked(){
		
	}
	
	public Tracked(String name, Threshold[] thresholds) {
		super();
		this.name = name;
		this.thresholds = thresholds;
	}
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Threshold[] getThresholds() {
		return thresholds;
	}
	public void setThresholds(Threshold[] thresholds) {
		this.thresholds = thresholds;
	}
}
