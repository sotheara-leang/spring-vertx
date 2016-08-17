package com.mcnc.example.springvertx.base.common;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author sotheara.leang
 *
 */
public class AppContextHolder implements ApplicationContextAware {

	private static ApplicationContext appContext;
	
	private static Properties configuration;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
		configuration = applicationContext.getBean("config", Properties.class);
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}	
	
	public static String getConfiguration(String key) {
		return configuration.getProperty(key);
	}
}
