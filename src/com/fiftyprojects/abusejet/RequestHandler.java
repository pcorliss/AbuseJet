package com.fiftyprojects.abusejet;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {
	private HttpServletRequest req;
	private ArrayList<String> actions = new ArrayList<String>();
	
	public RequestHandler(HttpServletRequest req) {
		this.req = req;
	}
	
	public ArrayList<String> MemcacheStore(){
		/*for (Enumeration e = req.getHeaderNames() ; e.hasMoreElements() ;) {
			String element = (String) e.nextElement();
			String value = req.getHeader(element);
			System.out.println("Header:"+element + ": " + value);
		}*/
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
		Tracked track = AbuseServlet.conf.getTracked(key);
		if(track == null){
			//System.out.println("No Match on Key:"+key);
			return;
		}
		//System.out.println("Matched:"+key);
		for(int i=0;i<track.getThresholds().length;i++){
			Threshold thresh = track.getThresholds()[i];
			int ttl = thresh.getTtl();
			if(Memcache.incr(key+"_"+val+"_"+ttl, ttl, 1) > thresh.getValue() && !actions.contains(thresh.getAction())){
				actions.add(thresh.getAction());
			}
		}
	}

	
	
}
