package com.mcnc.example.springvertx.web;

import com.mcnc.example.springvertx.base.web.annotation.Controller;
import com.mcnc.example.springvertx.base.web.annotation.RequestMapping;

import io.vertx.ext.web.RoutingContext;

@Controller
public class HomeController {

	@RequestMapping(value = "/", view = "index")
	public void getUser(RoutingContext context) {
		context.put("message", "Welcome to homepage!!!");
	}
}
