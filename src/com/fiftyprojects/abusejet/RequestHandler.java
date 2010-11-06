package com.fiftyprojects.abusejet;

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
            	storKeyVal(name,values[i]);
            }
        }
        return actions;
	}
	
	private void storKeyVal(String key, String val){
		Tracked track = AbuseJet.conf.getTracked(key);
		if(track != null){
			for(Threshold thresh: track.getThresholds()){
				String actionStr = thresh.applyThreshold(key, val);
				if(actionStr != null){
					actions.add(actionStr);
				}
			}
		}
	}

}
