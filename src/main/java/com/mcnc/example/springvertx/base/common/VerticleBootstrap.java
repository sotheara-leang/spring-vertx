package com.mcnc.example.springvertx.base.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * 
 * @author sotheara.leang
 *
 */
@Component
public class VerticleBootstrap {
	
	private static final Logger logger = LoggerFactory.getLogger(VerticleBootstrap.class);
	
	@Autowired
	public void init(Vertx vertx, List<Verticle> verticleList) {
		if (verticleList != null) {
			for (Verticle verticle : verticleList) {
				logger.debug(">>> Deploy verticle " + verticle.getClass() + " <<<");
				vertx.deployVerticle(verticle);
			}
		}
	}
}
