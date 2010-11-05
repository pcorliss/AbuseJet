package com.fiftyprojects.abusejet;

public class ReportingEntry {
	private long expiration;
	private long value;

	
	public ReportingEntry(int ttl, long value) {
		super();
		//60 second grace period on reports
		this.expiration = System.currentTimeMillis()/1000+ttl+60;
		this.value = value;
	}
	
	public long getExpiration() {
		return expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	
	
}
