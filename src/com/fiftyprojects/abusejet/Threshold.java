package com.fiftyprojects.abusejet;

import de.xeinfach.util.IpSubnet;

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

	public String applyThreshold(String key, String val){
		if(modifier == null){
			long memVal = Memcache.incr(key+"_"+val+"_"+ttl, ttl, 1);
			if(memVal > value){
				AbuseJet.storeAlert(action, key, modifier, memVal);
				return action;
			}
		} else {
			Boolean ipMatch = false;
			if(key.equals("ip") && modifier.contains("/")){
				IpSubnet ips = new IpSubnet(modifier);
				ipMatch = ips.contains(val);
			}
			if(ipMatch == true || val.matches(modifier)){
				long memVal = Memcache.incr("M_"+key+"_"+modifier+"_"+ttl, ttl, 1);
				if(memVal > value){
					AbuseJet.storeAlert(action, key, modifier, memVal);
					return action;
				}
			}
		}
		return null;
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
