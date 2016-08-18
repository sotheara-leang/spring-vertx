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

			Controller controller = AnnotationUtils.findAnnotation(clazz, Controller.class);
			String baseUrl = controller.value();
			
			ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {

				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
					if (requestMapping != null) {
						String[] mappedPatterns = requestMapping.value();
						
						// Prepare URL
						if (mappedPatterns.length == 0) {
							if (!"".equals(baseUrl)) {
								mappedPatterns = new String[]{baseUrl};
							} else {
								logger.error(String.format("Invalid request method %s of %s", method, clazz));
								throw new RuntimeException(String.format("Invalid request method"));
							}
						} else {
							for (String mappedPattern : mappedPatterns) {
								if (!"".equals(baseUrl)) {
									mappedPattern = baseUrl + mappedPattern;
								}
							}
						}
						
						// Create route and bind route parameters with request method
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
												routingContext.reroute(getViewFullName(view));
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
												routingContext.reroute(getViewFullName(view));
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
	protected String getViewFullName(String view) {
		String fullName = view;
		if (!fullName.endsWith(".html")) {
			fullName = view + ".html";
		}
		return fullName;
	}
}
