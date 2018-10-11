package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping("/index")
	public String getIndex(ModelAndView m){ 
		return "index";
	}
	@RequestMapping("/get")
	@ResponseBody
	public String getTest(){ 
		return "请求成功";
	}
}
