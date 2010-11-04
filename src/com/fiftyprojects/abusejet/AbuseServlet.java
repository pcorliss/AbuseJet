package com.fiftyprojects.abusejet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class AbuseServlet extends HttpServlet {
		//static Yaml yaml = initYaml();
		public static Configuration conf;
		
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			if(conf == null){ loadConf(); }
			//System.out.println("Req Path:"+req.getRequestURI());
			resp.setContentType("text/plain");
			if(req.getRequestURI().equals("/admin/reload_config")){
				loadConf();
				Memcache.releaseClient();
				resp.getWriter().println("Reloaded Config");
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/clear_cache")){
				Memcache.flush();
				resp.getWriter().println("Cleared Cache");
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/dump_conf")){
				Yaml yaml = new Yaml();
				resp.getWriter().println("Dumping Conf");
				resp.getWriter().println(yaml.dump(conf));
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if(req.getRequestURI().equals("/admin/block")){
				String type = req.getParameter("type");
				String value = req.getParameter("value");
				String action = req.getParameter("action");
				if(type != null && value != null && action != null){
					ArrayList<Blocks> blocks = (ArrayList<Blocks>) conf.getBlocks();
					//blocks[blocks.length] = new Blocks(type,value,action);
					blocks.add(new Blocks(type,value,action));
					Yaml yaml = new Yaml();
					resp.getWriter().println("New Block Added, please add to the config if you would like it to be persisted.");
					resp.getWriter().println(yaml.dump(conf));
					resp.setStatus(HttpServletResponse.SC_OK);
				} else {
					resp.getWriter().println("Unable to add new block. Please provide a type, value, and action variable");
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else if(req.getRequestURI().equals("/admin/")){
				// /admin/block?level=X&IP=Y	- Sets block level on IP Y
			} else {
			
				RequestHandler rh = new RequestHandler(req);
				ArrayList<String> actions = rh.MemcacheStore();
				
				resp.getWriter().println(actions.size() > 0 ? StringUtils.join(actions.toArray()," ") : "OK");
				// if action contains status code or tarpit
				
				for(String action: actions){
					//if(tarpits && action.startsWith("tarpit_")){
					if(conf.getTarpit() && action.startsWith("tarpit_")){
						try {
							Thread.currentThread().sleep(Integer.parseInt(action.substring(7)) * 1000);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(conf.getStatus() && action.startsWith("status_")){
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


		
		
		private void loadConf() {
			Yaml yaml = new Yaml(new Constructor(Configuration.class));
			try {
				AbuseServlet.conf = (Configuration) yaml.load(new FileInputStream("abusejet.yaml"));
				System.out.println("Configuration Loaded\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	

}
