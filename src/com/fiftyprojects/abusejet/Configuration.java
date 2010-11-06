package com.fiftyprojects.abusejet;

import java.util.ArrayList;
import java.util.Arrays;

public class Configuration {
	private ArrayList<String> memcached = new ArrayList<String>();
	private ArrayList<Tracked> tracked = new ArrayList<Tracked>();
	private Boolean status = true;
	private Boolean tarpit = true;
	private Boolean alerts = true;
	private int alertFrequency = 300;
	

	public Configuration(){
		
	}

	public Configuration(String[] memcached, Tracked[] tracked, Boolean status, Boolean tarpit) {
		super();
		this.memcached = (ArrayList<String>) Arrays.asList(memcached);
		this.tracked = (ArrayList<Tracked>) Arrays.asList(tracked);
		this.status = status;
		this.tarpit = tarpit;
	}
	
	public Configuration(String[] memcached, Tracked[] tracked) {
		super();
		this.memcached = (ArrayList<String>) Arrays.asList(memcached);
		this.tracked = (ArrayList<Tracked>) Arrays.asList(tracked);
	}
	
	public Tracked getTracked(String name){
		for(Tracked t: tracked){
			if(t.getName().equals(name)){
				return t;
			}
		}
		return null;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getTarpit() {
		return tarpit;
	}

	public void setTarpit(Boolean tarpit) {
		this.tarpit = tarpit;
	}

	public void setAlertFrequency(int alertFrequency) {
		this.alertFrequency = alertFrequency;
	}

	public int getAlertFrequency() {
		return alertFrequency;
	}

	public void setAlerts(Boolean alerts) {
		this.alerts = alerts;
	}

	public Boolean getAlerts() {
		return alerts;
	}

	public ArrayList<String> getMemcached() {
		return memcached;
	}

	public void setMemcached(ArrayList<String> memcached) {
		this.memcached = memcached;
	}

	public ArrayList<Tracked> getTracked() {
		return tracked;
	}

	public void setTracked(ArrayList<Tracked> tracked) {
		this.tracked = tracked;
	}

	
}
