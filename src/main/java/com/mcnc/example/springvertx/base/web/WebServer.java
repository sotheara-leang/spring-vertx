package com.mcnc.example.springvertx.base.web;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.annotation.Controller;
import com.mcnc.example.springvertx.base.web.annotation.RequestMapping;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * 
 * @author sotheara.leang
 *
 */
@Component
public class WebServer extends AbstractVerticle {
	
	private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
	
	@Autowired
	private Router mainRouter;
	
	@Override
	public void start() throws Exception {
		// Initialize Static handler
		logger.debug(">>> Initialize static handler <<<");
		initStaticHandler();
		
		// Initialize Controller handler
		logger.debug(">>> Initialize controller mapping <<<");
		initControllerMapping();
		
		vertx.createHttpServer()
			 .requestHandler(mainRouter::accept)
			 .listen(Integer.valueOf(AppContextHolder.getConfiguration("web.server.port")));
	}
	
	/**
	 * Initialize Controller Mapping
	 */
	public void initControllerMapping() {
		ApplicationContext appContext = AppContextHolder.getAppContext();
		String[] beanNames = appContext.getBeanNamesForAnnotation(Controller.class);
		
		for (String beanName : beanNames) {
			Object verticle = appContext.getBean(beanName);
			Class<?> clazz = verticle.getClass();
			ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {

				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
					if (requestMapping != null) {
						String[] mappedPatterns = requestMapping.value();
						
						if (mappedPatterns.length > 0) {
							for (String mappedPattern : mappedPatterns) {
								// URL mapping
								if (!mappedPattern.startsWith("/")) {
									mappedPattern = "/" + mappedPattern;
								}
								
								HttpMethod[] httpMethods = requestMapping.method();
								for (HttpMethod httpMethod : httpMethods) {
									Route route = mainRouter.route(httpMethod, mappedPattern);
									
									// Consumes mapping
									String[] consumes = requestMapping.consumes();
									for (String consume : consumes) {
										route.consumes(consume);
									}
									
									// Produce mapping
									String[] produces = requestMapping.produces();
									for (String produce : produces) {
										route.produces(produce);
									}
									
									// Blocking mapping
									boolean blocking = requestMapping.bocking();
									if (blocking) {
										route.blockingHandler(routingContext -> {
											try {
												method.invoke(verticle, routingContext);
											} catch (Exception e) {
												logger.error(e.getMessage(), e);
												routingContext.fail(e);
											}
										});
									} else {
										route.handler(routingContext -> {
											try {
												method.invoke(verticle, routingContext);
											} catch (Exception e) {
												logger.error(e.getMessage(), e);
												routingContext.fail(e);
											}
										});
									}
								}
							}
						}
					}
				}
			});
		}
	};
	
	public void initStaticHandler() {
		mainRouter.route().handler(StaticHandler.create(AppContextHolder.getConfiguration("web.server.webapp")));
	}
}
