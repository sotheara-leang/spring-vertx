package com.mcnc.example.springvertx.base.web.handler.impl;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * 
 * @author sotheara.leang
 *
 */
public class StaticResourceHandler implements RequestHandler {

	@Override
	public void handle(Router router) {
		StaticHandler staticResourceHandler = StaticHandler.create(AppContextHolder.getConfiguration("web.server.webroot")); 
		router.getWithRegex(".+\\.[html|css|png|js]").handler(staticResourceHandler);
	}
}
