package com.mcnc.example.springvertx.base.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * 
 * @author sotheara.leang
 *
 */

public class VerticleBootstrap implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(VerticleBootstrap.class);
	
	private Vertx vertx;
	
	private List<Verticle> verticles;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (verticles != null) {
			for (Verticle verticle : verticles) {
				logger.debug(">>> Deploy verticle " + verticle.getClass() + " <<<");
				
				vertx.deployVerticle(verticle);
			}
		}
	}

	public Vertx getVertx() {
		return vertx;
	}

	public void setVertx(Vertx vertx) {
		this.vertx = vertx;
	}

	public List<Verticle> getVerticles() {
		return verticles;
	}

	public void setVerticles(List<Verticle> verticles) {
		this.verticles = verticles;
	}
}
