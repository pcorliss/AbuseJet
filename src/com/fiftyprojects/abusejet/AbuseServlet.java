package com.fiftyprojects.abusejet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class AbuseServlet extends HttpServlet {
		//static Yaml yaml = initYaml();
		
		
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			handle(req, resp);
		}
		
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			handle(req, resp);
		}
		
		public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			if(AbuseJet.conf == null){ AbuseJet.initConf(); }
			//System.out.println("Req Path:"+req.getRequestURI());
			resp.setContentType("text/plain");
			if(req.getRequestURI().equals("/admin/reload_config")){
				AbuseJet.initConf();
				Memcache.releaseClient();
				resp.getWriter().println("Reloaded Config");
				Yaml yaml = new Yaml();
				resp.getWriter().println(yaml.dump(AbuseJet.conf));
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/alert_report")){
				alert_report(resp);
			} else if(req.getRequestURI().equals("/admin/clear_cache")){
				Memcache.flush();
				AbuseJet.alertHash.clear();
				resp.getWriter().println("Cleared Cache");
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/dump_config")){
				Yaml yaml = new Yaml();
				resp.getWriter().println("Dumping Conf");
				resp.getWriter().println(yaml.dump(AbuseJet.conf));
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/block")){
				String type = req.getParameter("type");
				String value = req.getParameter("value");
				String action = req.getParameter("action");
				if(type != null && value != null && action != null){
					ArrayList<Blocks> blocks = (ArrayList<Blocks>) AbuseJet.conf.getBlocks();
					//blocks[blocks.length] = new Blocks(type,value,action);
					blocks.add(new Blocks(type,value,action));
					Yaml yaml = new Yaml();
					resp.getWriter().println("New Block Added, please add to the config if you would like it to be persisted.");
					resp.getWriter().println(yaml.dump(AbuseJet.conf));
					resp.setStatus(HttpServletResponse.SC_OK);
				} else {
					resp.getWriter().println("Unable to add new block. Please provide a type, value, and action variable");
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
			
				RequestHandler rh = new RequestHandler(req);
				HashSet<String> actions = rh.MemcacheStore();

				resp.getWriter().println(actions.size() > 0 ? StringUtils.join(actions.toArray()," ") : "OK");
				// if action contains status code or tarpit
				
				for(String action: actions){
					//if(tarpits && action.startsWith("tarpit_")){
					if(AbuseJet.conf.getTarpit() && action.startsWith("tarpit-")){
						try {
							Thread.currentThread().sleep(Integer.parseInt(action.substring(7)) * 1000);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(AbuseJet.conf.getStatus() && action.startsWith("status-")){
						try {
							resp.setStatus(Integer.parseInt(action.substring(7)));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
			}
			
		}
	
		private void alert_report(HttpServletResponse resp) throws IOException {
			//System.out.println("Alert Report");
			resp.getWriter().println("Alert Report");
			resp.getWriter().println("Code_action_type_offender_ttl: Val\n");
			//System.out.println("Size:"+alertHash.size());
			for (Entry<String, ReportingEntry> entry : AbuseJet.alertHash.entrySet()){
				if(entry.getValue().getExpiration() < System.currentTimeMillis()/1000){
					//System.out.println("Current:"+(System.currentTimeMillis()/1000)+":Expired:"+entry.getValue().getExpiration());
					AbuseJet.alertHash.remove(entry.getKey());					
				} else {
					resp.getWriter().println(entry.getKey()+": "+entry.getValue().getValue());
				}
			}
			resp.setStatus(HttpServletResponse.SC_OK);
		}


}
