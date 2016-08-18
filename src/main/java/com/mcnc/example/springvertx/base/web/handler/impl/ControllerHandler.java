package com.mcnc.example.springvertx.base.web.handler.impl;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.mcnc.example.springvertx.base.common.AppContextHolder;
import com.mcnc.example.springvertx.base.web.annotation.Controller;
import com.mcnc.example.springvertx.base.web.annotation.RequestMapping;
import com.mcnc.example.springvertx.base.web.handler.RequestHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * 
 * @author sotheara.leang
 *
 */
public class ControllerHandler implements RequestHandler {

	private static final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);
	
	@Override
	public void handle(Router router) {
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
								
								// Create route
								Route route = router.routeWithRegex(mappedPattern);
								
								// Method mapping
								HttpMethod[] httpMethods = requestMapping.method();
								for (HttpMethod httpMethod : httpMethods) {
									route.method(httpMethod);
								}
								
								// Consumes mapping
								String[] consumes = requestMapping.consumes();
								if (consumes != null) {
									for (String consume : consumes) {
										route.consumes(consume);
									}
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
											
											// View mapping
											String view = requestMapping.view();
											if (!"".equals(view)) {
												routingContext.reroute(getViewName(view));
											}
											
										} catch (Exception e) {
											logger.error(e.getMessage(), e);
											routingContext.fail(e);
										}
									});
								} else {
									route.handler(routingContext -> {
										try {
											method.invoke(verticle, routingContext);
											
											// View mapping
											String view = requestMapping.view();
											if (!"".equals(view)) {
												routingContext.reroute(getViewName(view));
											}
											
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
			});
		}
	}
	
	/**
	 * Get view full name
	 * @param view
	 * @return
	 */
	protected String getViewName(String view) {
		return view + ".html";
	}
}
