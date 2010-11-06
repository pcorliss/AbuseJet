package com.fiftyprojects.abusejet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

public class AbuseServlet extends HttpServlet {
		
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			handle(req, resp);
		}
		
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			handle(req, resp);
		}
		
		public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			if(AbuseJet.conf == null){ AbuseJet.initConf(); }
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
				String valueStr = req.getParameter("value");
				String action = req.getParameter("action");
				String modifier = req.getParameter("modifier");
				String ttlStr = req.getParameter("ttl");
				int ttl = ttlStr == null ? 0 : Integer.parseInt(ttlStr);
				int value = valueStr == null ? 0 : Integer.parseInt(valueStr);
				if(type != null && action != null){
					Tracked tracker = AbuseJet.conf.getTracked(type);
					if(tracker == null){
						tracker = new Tracked(type);
						AbuseJet.conf.getTracked().add(tracker);
					}
					tracker.getThresholds().add(new Threshold(action, value, ttl, modifier));
					Yaml yaml = new Yaml();
					resp.getWriter().println("New Block Added, please add it to the config if you would like it to be persisted.");
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
				
				for(String action: actions){
					if(AbuseJet.conf.getTarpit() && action.startsWith("tarpit-")){
						try {
							Thread.currentThread().sleep(Integer.parseInt(action.substring(7)) * 1000);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if(AbuseJet.conf.getStatus() && action.startsWith("status-")){
						try {
							resp.setStatus(Integer.parseInt(action.substring(7)));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			
			}
			
		}
	
		private void alert_report(HttpServletResponse resp) throws IOException {
			resp.getWriter().println("Alert Report");
			resp.getWriter().println("Code_action_type_offender_ttl: Val\n");
			long curTime = System.currentTimeMillis()/1000;
			for (Entry<String, ReportingEntry> entry : AbuseJet.alertHash.entrySet()){
				if(entry.getValue().getExpiration() < curTime){
					AbuseJet.alertHash.remove(entry.getKey());					
				} else {
					resp.getWriter().println(entry.getKey()+": "+entry.getValue().getValue());
				}
			}
			resp.setStatus(HttpServletResponse.SC_OK);
		}


}
