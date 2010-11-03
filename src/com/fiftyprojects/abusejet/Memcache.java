package com.fiftyprojects.abusejet;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class Memcache {
	private static MemcachedClient c;
	//private static long starttime = System.currentTimeMillis() / 1000L;
	
	public static long get(String key){
		if(!initClient()){ return -1; }
		return Long.parseLong((String) c.get(key));
	}
	
	public static long incr(String key, int ttl, int value){
		if(!initClient()){ return -1; }
		//System.out.println("Incr:"+key+":"+ttl+":"+value);
		long retVal = c.incr(key, value);
		if(retVal == -1){
			//System.out.println("NewVal"+key+":"+ttl+":"+value);
			c.set(key, ttl, Integer.toString(value));
			//System.out.println("MC:"+key+":"+value);
			return value;
		}
		System.out.println("MC:"+key+":"+retVal);
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
				//c = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"));
				c = new MemcachedClient(AddrUtil.getAddresses(StringUtils.join(AbuseServlet.conf.getMemcached()," ")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return true;
		}
	}
	
	
}
