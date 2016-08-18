package com.mcnc.example.springvertx.base.web.handler.impl;

import org.springframework.beans.factory.InitializingBean;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * 
 * @author sotheara.leang
 *
 */
public class TemplateRequestHandler implements RequestHandler, InitializingBean {

	private TemplateEngine engine;
	
	@Override
	public void handle(Router router) {
		TemplateHandler templateHandler = TemplateHandler.create(
				engine, 
				AppContextHolder.getConfiguration("web.server.template"),
				"text/html");
		
		router.getWithRegex(".+\\.html").handler(templateHandler);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		engine = AppContextHolder.getAppContext().getBean(TemplateEngine.class);
	}
}
