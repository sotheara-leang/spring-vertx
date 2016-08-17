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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContextHolder.appContext = applicationContext;
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}	
	
	public static String getConfiguration(String key) {
		Properties props = appContext.getBean("config", Properties.class);
		return props.getProperty(key);
	}
}
