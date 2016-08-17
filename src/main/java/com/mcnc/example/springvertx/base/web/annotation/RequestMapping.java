package com.mcnc.example.springvertx.base.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

/**
 * 
 * @author sotheara.leang
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

	String[] value() default {};
	
	HttpMethod[] method() default {HttpMethod.GET};
	
	String[] headers() default {};
	
	String[] consumes() default {"text/html"};
	
	String[] produces() default {"text/html"};
	
	boolean bocking() default false;
}
