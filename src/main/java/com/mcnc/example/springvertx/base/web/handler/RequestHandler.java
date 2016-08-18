package com.mcnc.example.springvertx.base.web.handler;

import io.vertx.ext.web.Router;

/**
 * 
 * @author sotheara.leang
 *
 */
public interface RequestHandler {

	void handle(Router router);
}
