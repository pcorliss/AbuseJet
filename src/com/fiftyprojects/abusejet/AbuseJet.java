package com.fiftyprojects.abusejet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class AbuseJet {
	public static Configuration conf;
	public static ConcurrentHashMap<String,ReportingEntry> alertHash = new ConcurrentHashMap<String,ReportingEntry>();
	
	public static void initConf() {
		Yaml yaml = new Yaml(new Constructor(Configuration.class));
		try {
			conf = (Configuration) yaml.load(new FileInputStream("abusejet.yaml"));
			System.out.println("Configuration Loaded\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void storeAlert(String action, String key, String modifier, long memVal){
		if(conf.getAlerts()){
			String memKey = (modifier == null ? "AF_" : "AFM_")+action+"_"+key+(modifier == null ? "" : "_"+modifier)+"_"+conf.getAlertFrequency();
			alertHash.put(memKey, new ReportingEntry(conf.getAlertFrequency(),memVal));
		}
	}

}
