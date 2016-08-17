package com.mcnc.example.springvertx.web;

import com.mcnc.example.springvertx.base.web.annotation.Controller;
import com.mcnc.example.springvertx.base.web.annotation.RequestMapping;

import io.vertx.ext.web.RoutingContext;

@Controller
public class UserController {

	@RequestMapping(value = "/users")
	public void getUser(RoutingContext context) {
		 context.response()
	        .setStatusCode(200)
	        .end("Hello from users");
	}
}
