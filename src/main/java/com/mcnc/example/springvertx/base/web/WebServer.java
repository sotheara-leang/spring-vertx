package com.mcnc.example.springvertx.base.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * 
 * @author sotheara.leang
 *
 */
@Component
public class WebServer extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
	
	@Autowired
	private Router router;
	
	@Autowired
	private List<RequestHandler> requestHandlers;
	
	@Override
	public void start() throws Exception {
		// Initialize request handlers
		if (requestHandlers != null) {
			for (RequestHandler requestHandler : requestHandlers) {
				logger.debug(String.format(">>> Initialize request handler: %s <<<", requestHandler.getClass()));
				requestHandler.handle(router);
			}
		}
				
		vertx.createHttpServer()
			 .requestHandler(router::accept)
			 .listen(Integer.valueOf(AppContextHolder.getConfiguration("web.server.port")));
	}

}
