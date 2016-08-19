package com.mcnc.example.springvertx.base.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * 
 * @author sotheara.leang
 *
 */
public class WebServerVerticle extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(WebServerVerticle.class);
	
	private Router router;
	
	private List<RequestHandler> handlers;
	
	@Override
	public void start() throws Exception {
		// Initialize request handlers
		if (handlers != null) {
			for (RequestHandler requestHandler : handlers) {
				logger.debug(String.format(">>> Initialize request handler: %s <<<", requestHandler.getClass()));
				requestHandler.handle(router);
			}
		}
		
		// Create http server
		vertx.createHttpServer()
			 .requestHandler(router::accept)
			 .listen(Integer.valueOf(AppContextHolder.getConfiguration("web.server.port")));
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public List<RequestHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<RequestHandler> handlers) {
		this.handlers = handlers;
	}
}
