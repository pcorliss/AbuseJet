package com.fiftyprojects.abusejet;

import java.util.ArrayList;
import java.util.Arrays;

public class Configuration {
	private String[] memcached;
	private Tracked[] tracked;
	private ArrayList<Blocks> blocks;
	private Boolean status = true;
	private Boolean tarpit = true;
	

	public Configuration(){
		
	}

	public Configuration(String[] memcached, Tracked[] tracked, Blocks[] blocks, Boolean status, Boolean tarpit) {
		super();
		this.memcached = memcached;
		this.tracked = tracked;
		this.blocks = (ArrayList<Blocks>) Arrays.asList(blocks);
		this.status = status;
		this.tarpit = tarpit;
	}
	
	public Configuration(String[] memcached, Tracked[] tracked, Blocks[] blocks) {
		super();
		this.memcached = memcached;
		this.tracked = tracked;
		this.blocks = (ArrayList<Blocks>) Arrays.asList(blocks);
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
	
	/*public Blocks getBlock(String type,String val){
		for(int i=0;i<blocks.length;i++){
			if(blocks[i].getType().equals(type) && blocks[i].getValue().equals(val)){
				return blocks[i];
			}
		}
		return null;
	}*/
	
	public Blocks getBlock(String type, String val){
		for(Blocks b: blocks){
			if(b.getType().equals(type) && b.getValue().equals(val)){
				return b;
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

	public ArrayList<Blocks> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Blocks> blocks) {
		this.blocks = blocks;
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

	
}
