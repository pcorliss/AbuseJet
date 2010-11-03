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
			} else if(req.getRequestURI().equals("/admin/")){
				// /admin/block?level=X&IP=Y	- Sets block level on IP Y
			} else {
			
				RequestHandler rh = new RequestHandler(req);
				ArrayList<String> actions = rh.MemcacheStore();
				
				resp.getWriter().println(actions.size() > 0 ? StringUtils.join(actions.toArray()," ") : "OK");
				// if action contains status code or tarpit
				if(actions.contains("tarpit")){
					try {
						Thread.currentThread().sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(actions.contains("404")){
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else if(actions.contains("500")){
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
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
