package com.example.sebu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
@RestController
public class TestController {

		@GetMapping("/home")
		public String goHome(HttpServletRequest request) {
			Map map = new HashMap<>();
			//System.out.println("enter");
			return "hello home";
		}
	
}
