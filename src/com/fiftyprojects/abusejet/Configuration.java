package com.fiftyprojects.abusejet;

public class Configuration {
	private String[] memcached;
	private Tracked[] tracked;
	
	public Configuration(){
		
	}

	public Configuration(String[] memcached, Tracked[] tracked) {
		super();
		this.memcached = memcached;
		this.tracked = tracked;
	}
	
	public Tracked getTracked(String name){
		for(int i=0;i<tracked.length;i++){
			if(tracked[i].getName().equals(name)){
				return tracked[i];
			}
		}
		return null;
	}

	public Tracked[] getTracked() {
		return tracked;
	}

	public void setTracked(Tracked[] tracked) {
		this.tracked = tracked;
	}

	public String[] getMemcached() {
		return memcached;
	}

	public void setMemcached(String[] memcached) {
		this.memcached = memcached;
	}
	
}
