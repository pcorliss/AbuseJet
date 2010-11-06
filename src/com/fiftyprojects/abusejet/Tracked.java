package com.fiftyprojects.abusejet;

import java.util.ArrayList;
import java.util.Arrays;

public class Tracked {
	private String name;
	private ArrayList<Threshold> thresholds = new ArrayList<Threshold>();
	
	
	public Tracked(){
		
	}
	
	public Tracked(String name) {
		super();
		this.name = name;
	}
	
	public Tracked(String name, Threshold[] thresholds) {
		super();
		this.name = name;
		this.thresholds = (ArrayList<Threshold>) Arrays.asList(thresholds);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Threshold> getThresholds() {
		return thresholds;
	}
	public void setThresholds(ArrayList<Threshold> thresholds) {
		this.thresholds = thresholds;
	}
}
