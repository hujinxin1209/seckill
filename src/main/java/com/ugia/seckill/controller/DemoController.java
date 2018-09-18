package com.ugia.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ugia.seckill.domain.User;
import com.ugia.seckill.rabbitmq.MQSender;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.redis.UserKey;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
import com.ugia.seckill.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	MQSender sender;
	
	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "ugia";
	}

	@RequestMapping("/mq")
	@ResponseBody
	public Result<String> mq() {
		sender.send("hello, mq");
		return Result.success("Hello mq");
	}

	
	@RequestMapping("/wshello")
	@ResponseBody
	public Result<String> hello() {
		return Result.success("sucess");
	}

	@RequestMapping("/error")
	@ResponseBody
	public Result<String> error() {
		return Result.error(CodeMsg.SERVER_ERROR);
	}

	@RequestMapping("/hello2")
	public String thymeleaf(ModelMap model) {
		// model.addAttribute("name", "222");
		model.put("name", "uugia");
		return "hello2";
	}

	@RequestMapping("/db/get")
	@ResponseBody
	public Result<User> dbGet() {
		User user = userService.getById(1);
		userService.tx();
		return Result.success(user);
	}

	@RequestMapping("/redis/get")
	@ResponseBody
	public Result<User> redisGet() {
		User user = redisService.get(UserKey.getById, "" + 1, User.class);
		return Result.success(user);
	}

	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<Boolean> redisSet() {
		User user = new User(2, "222");
		redisService.set(UserKey.getById, "" + 2, user);// UserKey:id2
		return Result.success(true);
	}
}
