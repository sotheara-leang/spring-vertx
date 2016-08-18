package com.mcnc.example.springvertx.base.web.handler.impl;

import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;

/**
 * 
 * @author sotheara.leang
 *
 */
public class ErrrorRequestHandler implements RequestHandler {

	@Override
	public void handle(Router router) {
		router.route().failureHandler(ErrorHandler.create());
	}
}
