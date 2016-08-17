package com.mcnc.example.springvertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author sotheara.leang
 *
 */
public class AppBootstrap {
	
	private static final Logger logger = LoggerFactory.getLogger(AppBootstrap.class);
	
	@SuppressWarnings("unused")
	private static ApplicationContext appContext;

	public static void main(String[] args) {
		logger.debug(">>> Initialize Application Context <<<");
		
		appContext = new ClassPathXmlApplicationContext("classpath:config/spring/appcontext-root.xml");
		
		logger.debug(">>> Finish initializing Application Context <<<");
	}
}
