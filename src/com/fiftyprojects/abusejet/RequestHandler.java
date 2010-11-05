package com.fiftyprojects.abusejet;

import de.xeinfach.util.IpSubnet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {
	private HttpServletRequest req;
	private HashSet<String> actions = new HashSet<String>();
	
	public RequestHandler(HttpServletRequest req) {
		this.req = req;
	}
	
	public HashSet<String> MemcacheStore(){
		Map<String, String[]> map = req.getParameterMap();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            for(int i=0;i<values.length;i++){
            	//System.out.println("Data:"+name + ":" + values[i]);
            	storKeyVal(name,values[i]);
            }
        }
        return actions;
        
        //String var = req.getParameter("Doesn't Exist");
	}
	
	private void storKeyVal(String key, String val){
		
		//Check if manual block applies
		/*Blocks block = AbuseJet.conf.getBlock(key,val);
		if(block != null){
			actions.add(block.getAction());
			long memVal = Memcache.incr(key+"_"+val+"_86400", 86400, 1);
			if(AbuseJet.conf.getAlerts()){
				String memKey = "AFB_"+block.getAction()+"_"+key+"_"+val+"_"+AbuseJet.conf.getAlertFrequency();
				AbuseJet.alertHash.put(memKey, new ReportingEntry(AbuseJet.conf.getAlertFrequency(),memVal));
			}
		}*/
		Blocks block = AbuseJet.conf.getBlock(key,val);
		if(block != null){
			actions.add(block.applyBlock());
		}
		
		Tracked track = AbuseJet.conf.getTracked(key);
		if(track == null){
			return;
		}
		
		
		
		for(Threshold thresh: track.getThresholds()){
			actions.add(thresh.applyThreshold(key, val));
		}
	}

	
	
}
