package com.ugia.seckill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.domain.User;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
import com.ugia.seckill.service.MiaoshaUserService;
import com.ugia.seckill.service.UserService;
import com.ugia.seckill.util.ValidatorUtil;
import com.ugia.seckill.vo.LoginVo;

import javassist.expr.NewArray;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	RedisService redisService;
	@Autowired
	MiaoshaUserService miaoshaUserService;
	
	@RequestMapping("/to_list")
	public String list(HttpServletResponse response, Model model,
			@CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN, required=false)String cookieToken,
			@RequestParam(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required=false)String paramToken){
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return "login";
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken : paramToken;
		MiaoshaUser user = miaoshaUserService.getByToken(response, token);
		model.addAttribute("user", user);
		return "goods_list";
	}
	
	@RequestMapping("/to_detail")
	public String detail(HttpServletResponse response, Model model,
//			@CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN, required=false)String cookieToken,
//			@RequestParam(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required=false)String paramToken
			MiaoshaUser user){
//		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//			return "login";
//		}
//		String token = StringUtils.isEmpty(paramToken)?cookieToken : paramToken;
//		MiaoshaUser user = miaoshaUserService.getByToken(response, token);
		model.addAttribute("user", user);
		return "goods_list";
	}
}
