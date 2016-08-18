package com.mcnc.example.springvertx.web;

import com.mcnc.example.springvertx.base.web.annotation.Controller;
import com.mcnc.example.springvertx.base.web.annotation.RequestMapping;

import io.vertx.ext.web.RoutingContext;

@Controller("/users")
public class UserController {

	@RequestMapping
	public void getUser(RoutingContext context) {
		 context.response()
	        .setStatusCode(200)
	        .end("Hello from users");
	}
}
