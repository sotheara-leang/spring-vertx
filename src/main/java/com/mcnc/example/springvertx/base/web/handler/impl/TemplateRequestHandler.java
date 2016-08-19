package com.mcnc.example.springvertx.base.web.handler.impl;

import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;

/**
 * 
 * @author sotheara.leang
 *
 */
public class TemplateRequestHandler implements RequestHandler {

	private TemplateHandler handler;
	
	@Override
	public void handle(Router router) {
		router.getWithRegex(".+\\.html").handler(handler);
	}

	public TemplateHandler getHandler() {
		return handler;
	}

	public void setHandler(TemplateHandler handler) {
		this.handler = handler;
	}
}
