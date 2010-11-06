package com.fiftyprojects.abusejet;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class Memcache {
	private static MemcachedClient c;
	
	public static long get(String key){
		if(!initClient()){ return -1; }
		return Long.parseLong((String) c.get(key));
	}
	
	public static long incr(String key, int ttl, int value){
		if(!initClient()){ return -1; }
		long retVal = c.incr(key, value, value, ttl);
		//System.out.println("MC:"+key+":"+retVal);
		return retVal;
	}
	
	public static void flush(){
		if(!initClient()){ return; }
		c.flush();
	}

	public static void releaseClient(){
		if(c != null){
			c.shutdown();
			c = null;
		}
	}
	
	private static Boolean initClient(){
		if(c == null){
			try {
				c = new MemcachedClient(AddrUtil.getAddresses(StringUtils.join(AbuseJet.conf.getMemcached()," ")));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}

	public static Boolean set(String key, int ttl, long memVal) {
		if(!initClient()){ return false; }
		c.set(key, ttl, String.valueOf(memVal));
		return true;
	}
	
	
}
