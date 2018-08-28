package com.ugia.seckill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.result.Result;
import com.ugia.seckill.service.MiaoshaUserService;
import com.ugia.seckill.service.UserService;
import com.ugia.seckill.util.ValidatorUtil;
import com.ugia.seckill.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	RedisService redisService;
	@Autowired
	MiaoshaUserService miaoshaUserService;
	
	@RequestMapping("/to_login")
	public String toLogin(){
		return "login";
	}
	
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
		log.info(loginVo.toString());
		// 参数校验
		String passInput = loginVo.getPassword();
		String mobile = loginVo.getMobile();
		if(StringUtils.isEmpty(passInput)) {
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		}
		if(StringUtils.isEmpty(mobile)) {
			return Result.error(CodeMsg.MOBILE_EMPTY);
		}
		if(!ValidatorUtil.isMobile(mobile)) {
			return Result.error(CodeMsg.MOBILE_ERROR);
		}
		// 登录
		CodeMsg cMsg = miaoshaUserService.login(loginVo);
		if(cMsg.getCode() == 0) {
			return Result.success(true);
		} else {
			return Result.error(cMsg);
		}
	}
}
