package com.ugia.seckill.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.ugia.seckill.dao.MiaoshaUserDao;
import com.ugia.seckill.domain.MiaoshaUser;
import com.ugia.seckill.exception.GlobalException;
import com.ugia.seckill.redis.MiaoshaUserKey;
import com.ugia.seckill.redis.RedisService;
import com.ugia.seckill.result.CodeMsg;
import com.ugia.seckill.util.MD5Util;
import com.ugia.seckill.util.UUIDUtil;
import com.ugia.seckill.vo.LoginVo;

@Service
public class MiaoshaUserService {
	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;

	@Autowired
	RedisService redisService;

	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}

		String mobile = loginVo.getMobile();
		String formPassword = loginVo.getPassword();

		MiaoshaUser user = getById(Long.parseLong(mobile));
		if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}

		// 验证密码
		String dbPassword = user.getPassword();
		String saltDB = user.getSalt();
		String calcPas = MD5Util.formPassToDBPass(formPassword, saltDB);
		if (!calcPas.equals(dbPassword)) {
			throw new GlobalException(CodeMsg.PASSWORD_WRONG);
		}
		// 生成cookie
		addCookie(response, user);
		return true;
	}

	private void addCookie(HttpServletResponse response, MiaoshaUser user) {
		
		String token = UUIDUtil.uuid();
		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		// 延长token有效期
		if(user != null) {
			addCookie(response, user);
		}
		return user;
	}
}
